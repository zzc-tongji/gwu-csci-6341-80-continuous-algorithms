public class UnknownFunctionDerivative {

    public static void main(String[] argv) {
        // Make an instance of the object.
        Simulator S = new Simulator();

        double d = 0.01;

        for (double x = 0; x <= 10; x += 1) {
            // Compute derivative at x.
            double f = S.getValue(x);
            double fd = S.getValue(x + d);
            double g = (fd - f) / d;
            System.out.println("x=" + x + "  g(x)=" + g);
        }

    }

}
