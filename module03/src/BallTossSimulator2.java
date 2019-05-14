public class BallTossSimulator2 {

    double vy;

    public double run(double stopTime, double initVelocity) {
        // Initialize variables.
        double a = 9.8;
        vy = initVelocity;
        double y = 0;
        double t = 0;
        double delT = 0.01;

        while (true) {
            // Update time.
            t += delT;

            // Increase velocity accordingly.
            vy = vy - delT * a;

            // Increase distance according to velocity.
            y = y + delT * vy;

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
