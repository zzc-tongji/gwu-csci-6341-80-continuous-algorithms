import java.util.*;
import java.awt.geom.*;
import java.awt.*;


public class MySimpleCarController implements CarController {

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

    boolean phase1 = true;
    boolean phase2 = false;
    boolean phase3 = false;
    boolean phase4 = false;
    boolean phase5 = false;

    // INSERT YOUR CODE HERE
    double alpha = 10;
    double delta = 40;
    double kP = 0.05;
    double kD = kP / 20;
    double ePrevious = 0;
    double e = 0;
    double tPrevious = 0;
    double t = -0.001;
    double ePrime = 0;

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

        if (phase1) {
            if (dN > 50) {
                vel = 10;
            } else {
                vel = 0;
                phase1 = false;
                phase2 = true;
            }
        }
        if (phase2) {
            if (sPack.getTheta() < Math.PI / 2) {
                vel = 2;
                phi = 1;
            } else {
                phase2 = false;
                phase3 = true;
            }

        }
        if (phase3) {
            if (dNE - dSE < alpha && dN > delta) {
                vel = 10;
                phi = 0;
            } else {
                vel = 2;
                phi = kP * e - kD * ePrime;
            }
            if (sPack.getX() > 250 && sPack.getY() < 50) {
                phase3 = false;
                phase4 = true;
            }
        }
        if (phase4) {
            if (sPack.getTheta() > Math.PI) {
                vel = 2;
                phi = 1;
            } else {
                phase4 = false;
                phase5 = true;
            }
        }
        if (phase5) {
            if (dNE - dSE < alpha && dN > delta) {
                vel = 10;
                phi = 0;
            } else {
                vel = 2;
                phi = kP * e - kD * ePrime;
            }
            if (Math.abs(sPack.getX() - endX) < 30 && Math.abs(sPack.getY() - endY) < 30) {
                phase5 = false;
                vel = 0;
                phi = 0;
            }
        }
    }


    public void draw(Graphics2D g2, Dimension D) {
    }

}
