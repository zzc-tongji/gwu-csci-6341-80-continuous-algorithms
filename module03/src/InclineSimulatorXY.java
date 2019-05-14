public class InclineSimulatorXY {

    // Mass and angle.
    public double mass = 1;
    public double angle = 30;

    // Separate acceleration, velocity and distance for x and y shadows.
    double ax = 0, ay = 0;
    double vx = 0, vy = 0;
    double x = 0, y = 0;

    // Time and time-step.
    double t = 0;
    double delT = 0.001;

    // Vertical acceleration.
    double g = 9.8;


    public double run(double stopTime) {
        // Acceleration along incline: g*sin(alpha).
        double angleRadians = 2 * Math.PI * angle / 360.0;
        double a = g * Math.sin(angleRadians);

        // Initialize variables.
        vx = 0;
        vy = 0;
        x = 0;
        y = 0;
        t = 0;

        // We'll track total distance traveled along incline.
        double d = 0;

        while (true) {
            // Update time.
            t += delT;

            // Acceleration along x and y.
            ax = a * Math.cos(angleRadians);
            ay = a * Math.sin(angleRadians);

            // Increase velocity according to appropriate acceleration.
            vx = vx + delT * ax;
            vy = vy - delT * ay;

            // Increase distance in each direction according to appropriate velocity.
            double nextX = x + delT * vx;
            double nextY = y + delT * vy;

            // Update total distance traveled.
            d = d + Math.sqrt((nextX - x) * (nextX - x) + (nextY - y) * (nextY - y));

            x = nextX;
            y = nextY;

            if (t >= stopTime) {
                break;
            }
        }

        return d;
    }

}
