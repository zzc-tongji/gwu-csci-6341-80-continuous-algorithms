public class BallDropSimulator2 {

    double vy;
    double t;


    public double run(double height) {
        // Initialize variables.
        double a = 9.8;

        // We'll use "y" for distance (falling along y-axis) and "vy" for velocity.
        double y = height;
        vy = 0;

        t = 0;
        double delT = 0.001;

        while (true) {
            // Update time.
            t += delT;

            // Decrease velocity by downward acceleration.
            vy = vy - delT * a;

            // Increase distance (y) according to velocity (which will reduce height)
            y = y + delT * vy;

            if (y <= 0) {   // Stop if it's hit the ground.
                break;
            }
        }

        return y;
    }


    public double getV() {
        return vy;
    }


    public double getT() {
        return t;
    }

}
