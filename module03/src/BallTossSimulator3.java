public class BallTossSimulator3 {

    double vy;

    public double run(double stopTime, double initVelocity, double targetHeight, boolean beforeMaxHeight) {
        // Initialize variables.
        double a = 9.8;
        vy = initVelocity;
        double y = 0;
        double t = 0;
        double delT = 0.01;

        boolean maxReached = false;

        while (true) {
            // Update time.
            t += delT;

            // Increase velocity accordingly.
            vy = vy - delT * a;

            // Increase distance according to velocity.
            double nextY = y + delT * vy;

            double prevY = y;
            y = nextY;

            if (y < prevY) {
                maxReached = true;
            }

            if (beforeMaxHeight) {
                if (y > targetHeight) {
                    // Done.
                    break;
                }
            } else if (maxReached) {
                if (y < targetHeight) {
                    // Done.
                    break;
                }
            }

            if ((t >= stopTime) || (y <= 0)) {
                break;
            }
        }

        return y;
    }


    public double getV() {
        return vy;
    }

}