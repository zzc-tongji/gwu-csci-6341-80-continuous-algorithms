public class ProjectileSimulator {

    double x = 0, y = 0;             // Track (x,y)
    double vx, vy;               // Velocity components
    double d;                    // Distance traveled


    public double run(double mass, double angle, double initialVelocity, double endTime, double timeInterval) {
        // Initialize values.
        double delT = timeInterval;               // Interval of time.
        double t = 0;                             // Current time.
        double g = 9.8;                           // 9.8 N - to be explained.
        double theta = Math.toRadians(angle);    // Convert degrees to radians.
        d = 0;
        x = 0;
        y = 0;

        // Initial vx = component of initialVelocity in x-direction.
        vx = initialVelocity * Math.cos(theta);
        // Initial vy = component of initialVelocity in y-direction.
        vy = initialVelocity * Math.sin(theta);

        while (t < endTime) {
            // Apply g to vy. Note: vx stays the same.
            vy = vy - g * delT;

            // Apply vx to x.
            double nextX = x + vx * delT;

            // Apply vy to y.
            double nextY = y + vy * delT;

            // Update total distance travelled.
            d = d + distance(x, y, nextX, nextY);

            // Set new (x,y)
            x = nextX;
            y = nextY;

            t = t + delT;
        }

        return d;
    }


    double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }


    public double getD() {
        return d;
    }

    public double getX() {
        return x;
    }


    public double getY() {
        return y;
    }

}
