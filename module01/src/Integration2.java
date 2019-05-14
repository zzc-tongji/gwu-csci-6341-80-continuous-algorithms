public class Integration2 {

    public static void main(String[] argv) {
        // We'll compute f(x) at x=0, 0.01, ..., 6.28 and put that in a graph.
        Function F = new Function("f");

        // Notice: we are computing f(0), f(0.01), ..., and NOT f(d), f(2d), ...
        for (double x = 0; x <= 2 * Math.PI; x += 0.01) {
            // Our method compute_f() does all the dirty work.
            double f = compute_f(x);
            F.add(x, f);
        }

        // Display.
        F.show();
    }


    static double compute_f(double x) {
        // The value of d is fixed throughout.
        double d = 0.01;

        // Initial value is assumed known:
        double f = 0;

        // Compute for x-values in [0,10]. We are now using a variable called z.
        // Notice: goes over the range 0.01 up through x (which is input to the method).
        for (double z = 0.01; z <= x; z += 0.01) {
            // We are given g, so we can compute it.
            double g = -Math.sin(z);

            // Find the new value at f(z+d) that becomes the new f.
            f = f + d * g;

            // No need to print intermediate values.
        }

        // Return f(x)
        return f;
    }

}
