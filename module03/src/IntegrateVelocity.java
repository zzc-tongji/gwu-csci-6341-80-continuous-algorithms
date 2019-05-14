public class IntegrateVelocity {

    public static void main(String[] argv) {
        // Compute velocity function as before.
        Function velocityCurve = new Function("velocity");
        // for (double end = 0.1; end <= 10; end += 0.1) {
        //     double finalVelocity = computeFinalVelocity(4.9, 0, 0, end, 0.1);
        //     velocityCurve.add(end, finalVelocity);
        // }
        double s = 0.1;
        double a = 4.9;
        velocityCurve.add(0, 0);
        for (double end = 0.1; end <= 10; end += s) {
            velocityCurve.add(end, velocityCurve.get(end - 0.1) + s * a);
        }

        // Compute distance at t=5:
        double finalDistance = computeFinalDistance(velocityCurve, 0, 0, 5, 0.01);
        System.out.println("t=5  d=" + finalDistance);

        // Compute distance at t=10:
        finalDistance = computeFinalDistance(velocityCurve, 0, 0, 9, 0.01);
        System.out.println("t=9  d=" + finalDistance);
    }


    // This is the same as before.

    public static double computeFinalVelocity(double a, double initialVelocity, double initialTime, double endTime, double s) {
        double v = initialVelocity;
        double t = initialTime;

        while (t < endTime) {
            // Update velocity:
            v = v + s * a;
            // Update time:
            t = t + s;
        }

        return v;
    }


    // This is similar to how we computed velocity.

    public static double computeFinalDistance(Function velocity, double initialDistance, double initialTime, double endTime, double s) {
        double d = initialDistance;
        double t = initialTime;

        while (t < endTime) {
            // Update distance:
            double v = velocity.get(t);
            d = d + s * v;
            // Update time:
            t = t + s;
        }

        return d;
    }


}
