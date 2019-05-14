import java.util.*;
import java.awt.geom.*;
import java.awt.*;


public class MySimpleCarController3 implements CarController {

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
    int turnToGoal = 1;
    int moveForward = 2;
    int state = turnToGoal;
    double delta = 50;
    double epsilon = 0.5;
    double kP1 = 0.05;
    double kD1 = kP1 / 20;
    double kP2 = 2;
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
        double gamma = Math.atan2(endY - sPack.getY(), endX - sPack.getX());
        double theta = sPack.getTheta();
        double gammaInTheta = gamma - theta;
        double gammaInThetaDelta;

        while (gammaInTheta > 2 * Math.PI) {
            gammaInTheta -= 2 * Math.PI;
        }
        while (gammaInTheta < 0) {
            gammaInTheta += 2 * Math.PI;
        }
        if (gammaInTheta <= Math.PI) {
            gammaInThetaDelta = gammaInTheta;
        } else {
            gammaInThetaDelta = gammaInTheta - 2 * Math.PI;
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

        if (Math.abs(sPack.getX() - endX) < 10 && Math.abs(sPack.getY() - endY) < 10) {
            vel = 0;
            phi = 0;
            return;
        }

        if (state == turnToGoal) {

            // Turn towards goal.
            vel = 2;
            phi = kP2 * gammaInThetaDelta;
            if (Math.abs(gammaInThetaDelta) < epsilon) {
                state = moveForward;
            }
        } else if (state == moveForward) {
            if (dN > delta && Math.abs(gammaInThetaDelta) < epsilon) {
                // move forward
                vel = 10;
                phi = 0;
            } else {
                vel = 2;
                phi = kP1 * e - kD1 * ePrime;
            }
        }
        if (openToGoal(Math.atan2(endY - sPack.getY(), endX - sPack.getX()), sPack.dist(sPack.getX(), sPack.getY(), endX, endY), sPack.getTheta(), sPack.sonarDistances)) {
            state = turnToGoal;
        }
    }


    boolean openToGoal(double goalAngle, double distToGoal, double theta, double[] sonarDistances) {
        double sectorAngle = 2 * Math.PI / sonarDistances.length;
        for (int i = 0; i < sonarDistances.length; i++) {

            // Get the angle of the i-th range sensor.
            double a1 = angleFix(theta + i * sectorAngle);
            int j = i + 1;
            if (j >= sonarDistances.length) {
                j = 0;
            }

            // Get the angle of the next (successive) sensor in anti-clockwise direction.
            double a2 = angleFix(theta + j * sectorAngle);

            // If the goal angle is between
            if (isBetween(a1, goalAngle, a2)) {
                // Check if the ranges are farther than goal.
                if ((sonarDistances[i] > distToGoal) && (sonarDistances[j] > distToGoal)) {
                    // If so, the goal is assumed to be visible.
                    return true;
                } else {
                    // Else not.
                    return false;
                }
            }
        }

        // Shouldn't reach here.
        return false;
    }


    double angleFix(double a) {
        // Make each angle an angle between 0 and 2*PI.
        //** Note: this code can be optimized.
        if (a < 0) {
            while (a < 0) {
                a = a + 2 * Math.PI;
            }
        } else if (a > 2 * Math.PI) {
            while (a > 2 * Math.PI) {
                a = a - 2 * Math.PI;
            }
        }
        return a;
    }


    boolean isBetween(double a1, double a, double a2) {
        // Adjust so that all are between 0 and 2*pi.
        a1 = angleFix(a1);
        a = angleFix(a);
        a2 = angleFix(a2);

        if (a1 <= a2) {
            // This is the simple case.
            if ((a1 <= a) && (a <= a2)) {
                return true;
            }
            return false;
        }

        if ((a <= a2) || (a >= a1)) {
            return true;
        }

        return false;
    }


    public void draw(Graphics2D g2, Dimension D) {
    }

}
