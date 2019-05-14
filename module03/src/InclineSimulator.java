public class InclineSimulator {

    // Mass and angle.
    public double mass = 1;
    public double angle = 30;

    // Acceleration, velocity, distance, and time.
    double a = 0;
    double v = 0;
    double d = 0;
    double t = 0;

    // Each time step.
    double delT = 0.001;

    // We'll explain "g" later.
    double g = 9.8;


    public double run(double stopTime) {
        // Acceleration along incline: g*sin(alpha).
        double angleRadians = 2 * Math.PI * angle / 360.0;
        a = g * Math.sin(angleRadians);

        // Initialize variables.
        v = 0;
        d = 0;
        t = 0;

        while (true) {
            // Update time.
            t += delT;

            // Increase velocity according to (constant) acceleration a.
            v = v + delT * a;

            // Increase distance according to velocity.
            d = d + delT * v;

            if (t >= stopTime) {
                break;
            }
        }

        // This is the distance moved in time t=stopTime.
        return d;
    }


    public double getV() {
        return v;
    }


    public double getA() {
        return a;
    }

    public double getX() {
        double angleRadians = 2 * Math.PI * angle / 360.0;
        return d * Math.cos(angleRadians);
    }

    public double getY() {
        double angleRadians = 2 * Math.PI * angle / 360.0;
        return d * Math.sin(angleRadians);
    }

}
