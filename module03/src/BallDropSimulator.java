public class BallDropSimulator {

    public double run(double stopTime, double height) {
        // Initialize variables.
        double a = 9.8;

        // We'll use "y" for distance (falling along y-axis) and "vy" for velocity.
        double y = height;
        double vy = 0;

        double t = 0;
        double delT = 0.01;

        while (true) {
            // Update time.
            t += delT;

            // Decrease velocity by downward acceleration.
            vy = vy - delT * a;

            // Increase distance (y) according to velocity (which will reduce height)
            y = y + delT * vy;

            if ((t >= stopTime) || (y <= 0)) {   // Also stop if it's hit the ground.
                break;
            }
        }

        return y;
    }

}
