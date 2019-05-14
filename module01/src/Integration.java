public class Integration {

    public static void main(String[] argv) {
        // The value of d is fixed throughout.
        double d = 0.01;

        // Initial value is assumed known:
        double f = 0;

        // Compute for x-values in [0,10]
        for (double x = 0.01; x <= 2; x += 0.01) {
            // We are given g, so we can compute it.
            double g = 6 * x;

            // Find the new value at f(x+d) that becomes the new f.
            f = f + d * g;

            // Print.
            System.out.println("x=" + x + "   f(x)=" + f);
        }
    }

}
