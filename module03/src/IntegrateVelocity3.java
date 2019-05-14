public class IntegrateVelocity3 {

    public static void main(String[] argv) {
        // No need to compute velocity function.

        // Compute distance at t=5:
        double finalDistance = computeFinalDistance(0, 0, 0, 5, 0.01);
        System.out.println("t=5  d=" + finalDistance);

        // Compute distance at t=9:
        finalDistance = computeFinalDistance(0, 0, 0, 9, 0.01);
        System.out.println("t=9  d=" + finalDistance);
    }


    public static double computeFinalDistance(double initialVelocity, double initialDistance, double initialTime, double endTime, double s) {
        // Constant acceleration.
        double a = 4.9;

        // Set initial values.
        double v = initialVelocity;
        double d = initialDistance;
        double t = initialTime;

        while (t < endTime) {
            // First update distance:
            d = d + s * v;
            // Then update velocity:
            v = v + s * a;
            // Update time:
            t = t + s;
        }

        return d;
    }

}


