public class ProjectileSimulator2 {

    double x = 0, y = 0;             // Track (x,y)
    double vx, vy;               // Velocity components


    public double run(double angle, double initialVelocity) {
        // Initialize values.
        double delT = 0.001;                      // Interval of time.
        double t = 0;                             // Current time.
        double g = 9.8;                           // 9.8 N 
        double theta = Math.toRadians(angle);    // Convert degrees to radians.

        x = 0;
        y = 0;

        // Initial vx = component of initialVelocity in x-direction.
        vx = initialVelocity * Math.cos(theta);

        // Initial vy = component of initialVelocity in y-direction.
        vy = initialVelocity * Math.sin(theta);

        while (y >= 0) {
            // Apply g to vy. Note: vx stays the same.
            vy = vy - g * delT;

            // Apply vx to x.
            x = x + vx * delT;

            // Apply vy to y.
            y = y + vy * delT;

            t = t + delT;
        }

        return t;
    }


    public double getX() {
        return x;
    }


    public double getY() {
        return y;
    }

}
