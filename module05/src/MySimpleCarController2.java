import java.util.*;
import java.awt.geom.*;
import java.awt.*;


public class MySimpleCarController2 implements CarController {

    // The two controls: either (vel,phi) or (acc,phi)
    double acc;       // Acceleration.
    double vel;       // Velocity.
    double phi;       // Steering angle.

    ArrayList<Rectangle2D.Double> obstacles;
    SensorPack sensors;

    // Is the first control an accelerator?
    boolean isAccelModel = false;
    boolean isUnicycle = true;

    // Forward velocity for accelerative model.
    double v;


    double prevTheta = 0;
    double prevTime = 0;
    boolean firstTime = true;

    double endX, endY;

    // INSERT YOUR CODE HERE
    double alpha = 10;
    double delta = 50;
    double epsilon = 0.5;
    double kP = 0.05;
    double kD = kP / 20;
    double ePrevious = 0;
    double e = 0;
    double tPrevious = 0;
    double t = -0.001;
    double ePrime = 0;
    double thetaAccumulate = 0;


    public void init(double initX, double initY, double initTheta, double endX, double endY, double endTheta, ArrayList<Rectangle2D.Double> obstacles, SensorPack sensors) {
        firstTime = true;
        this.obstacles = obstacles;
        this.sensors = sensors;
        this.endX = endX;
        this.endY = endY;
    }


    public double getControl(int i) {
        if (i == 1) {
            if (isAccelModel) {
                return acc;
            } else {
                return vel;
            }
        } else if (i == 2) {
            return phi;
        }
        return 0;
    }


    public void move() {
        if (!(sensors instanceof BasicSensorPack)) {
            System.out.println("ERROR: Incorrect sensor pack selection");
            return;
        }
        BasicSensorPack sPack = (BasicSensorPack) sensors;

        double dN = sPack.sonarDistances[0];   // Forward distance.

        // Use these in later exercises.
        double dNE = sPack.sonarDistances[7];  // Distance along NE direction.
        double dSE = sPack.sonarDistances[5];  // Distance along SE direction.

        // INSERT YOUR CODE HERE
        double gamma = Math.atan2(endY - sPack.getY(), endX - sPack.getX());
        double theta = sPack.getTheta();
        double gammaInTheta = gamma - theta;
        while (gammaInTheta > 2 * Math.PI) {
            gammaInTheta -= 2 * Math.PI;
        }
        while (gammaInTheta < 0) {
            gammaInTheta += 2 * Math.PI;
        }

        ePrevious = e;
        if (dSE > 2 * dNE) {
            dSE = 2 * dNE;
        } else if (dNE > 2 * dSE) {
            dNE = 2 * dSE;
        }
        e = dSE - dNE;
        tPrevious = t;
        t = sPack.getTime();
        ePrime = (e - ePrevious) / (t - tPrevious);

        if (Math.abs(sPack.getX() - endX) < 30 && Math.abs(sPack.getY() - endY) < 30) {
            vel = 0;
            phi = 0;
            return;
        }
        if (dN > delta && !sPack.isBetween(theta, gamma - epsilon, gamma + epsilon)) {
            // There's no obstacle, but we're not in the right direction.
            //
            // turn towards goal
            if (gammaInTheta < Math.PI) {
                vel = 1;
                phi = 1;
            } else {
                vel = 1;
                phi = -1;
            }
        } else if (dN > delta && sPack.isBetween(theta, gamma - epsilon, gamma + epsilon)) {
            // We're in the right direction, and there's no obstacle.
            //
            // move forward
            vel = 10;
            phi = 0;
        } else {
            // As in previous section.
            //
            // follow-wall
            if (dNE - dSE < alpha && dN > delta) {
                vel = 10;
                phi = 0;
            } else {
                vel = 2;
                phi = kP * e - kD * ePrime;
            }
        }

    }


    public void draw(Graphics2D g2, Dimension D) {
    }

}
