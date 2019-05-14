public class InclineSegmentSimulator {

    // Separate acceleration, velocity and distance for x and y shadows.
    double ax = 0, ay = 0;
    double vx = 0, vy = 0;
    double x = 0, y = 0;

    // Time and time-step.
    double t = 0;
    double delT = 0.001;

    // Vertical acceleration.
    double g = 9.8;


    public double run(double x1, double y1, double x2, double y2, double initvx, double initvy) {
        // Acceleration along incline: g*sin(alpha).
        double hyp = distance(x1, y1, x2, y2);
        double xDiff = Math.abs(x1 - x2);
        double yDiff = Math.abs(y1 - y2);
        double sineOfAngle = yDiff / hyp;
        double cosineOfAngle = xDiff / hyp;

        // Acceleration along incline.
        double a = g * sineOfAngle;

        // Acceleration along x and y.
        ax = a * cosineOfAngle;
        ay = a * sineOfAngle;

        // Initialize velocity components.
        vx = initvx;
        vy = initvy;

        // Distance components.
        x = x1;
        y = y1;

        t = 0;

        while (true) {
            // Update time.
            t += delT;

            // Increase velocity according to appropriate acceleration.
            vx = vx + delT * ax;
            vy = vy - delT * ay;

            // Increase distance in each direction according to appropriate velocity.
            x = x + delT * vx;
            y = y + delT * vy;

            if ((x > x2) && (y < y2)) {
                // NOTE: this assumes that (x2,y2) is to the right and below.
                break;
            }
        }

        return t;
    }


    double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }


    public double getVx() {
        return vx;
    }


    public double getVy() {
        return vy;
    }

}
