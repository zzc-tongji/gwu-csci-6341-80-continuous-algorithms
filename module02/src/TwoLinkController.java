public class TwoLinkController implements ArmController {

    int numLinks;
    boolean isTorque;
    int state = 0;

    public void init(int numLinks, boolean isTorque) {
        this.numLinks = numLinks;
        this.isTorque = isTorque;
    }

    public double[] getDeltaAngles() {
        // The angles of the two joints:
        double theta0 = ArmSimulator.simulator.getAngle(0);
        double theta1 = ArmSimulator.simulator.getAngle(1);
        // Get the same in degrees, for convenience:
        double linkAngle0 = ArmSimulator.simulator.getAngleDegrees(0);
        double linkAngle1 = ArmSimulator.simulator.getAngleDegrees(1);

        // The coords of the end of the arm:
        double x = ArmSimulator.simulator.getX(1);
        double y = ArmSimulator.simulator.getY(1);

        // Get the lengths of each arm segment for the calculations:
        double L0 = ArmSimulator.simulator.getLinkLength(0);
        double L1 = ArmSimulator.simulator.getLinkLength(1);

        // The change of angle at each step for delTheta0:
        double delTheta = 2 * Math.PI * 1.0 / 360.0;

        // This is what we need to return: two angle-changes
        double[] delAngles = new double[2];

        // Initially in state 0. We'll first get the two links straight up.
        if (state == 0) {
            // First, heck if we need to change state.
            if ((linkAngle0 > 85) && (linkAngle1 < 5)) {
                // If we're nearly vertical, go to state=1.
                state = 1;
            } else {
                // If not, increase the first angle, decrease the second.
                delAngles[0] = delTheta;
                delAngles[1] = -2 * delTheta;
            }
        } else if (state == 1) {
            // Bring the second link down until y=10
            if (y <= 10) {
                // If y is approx 10, go to state 2
                state = 2;
            } else {
                // First link stays fixed, only the second moves.
                delAngles[0] = 0;
                delAngles[1] = -delTheta;
            }
        } else if (state == 2) {
            // y is now to remain at 10 until x = 30
            if (x >= 30) {
                state = 3;
            } else {
                // Move the first link clockwise:
                delAngles[0] = -delTheta;
                // The second angle comes from inverse-kinematics:
                double newTheta0 = theta0 + delAngles[0];

                // INSERT YOUR CODE HERE:
                // Use Math.asin for sin-inverse
                // y=10 and L0, L1 are already set for you.
                double newTheta1 = Math.asin((y - L0 * Math.sin(newTheta0)) / L1) - newTheta0;

                double delTheta1 = newTheta1 - theta1;
                delAngles[1] = delTheta1;
            }
        } else if (state == 3) {
            // Stay here.
        }

        return delAngles;
    }


    public double[] getTorques() {
        return null;
    }

}
