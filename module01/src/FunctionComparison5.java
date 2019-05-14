public class FunctionComparison5 {

    public static void main(String[] argv) {
        // Initialize sum.
        double sum = 0;

        // Compute interval = range/number-of-points:
        double interval = (10.0 - 0.0) / 50.0;

        // Generate 50 values in the range [0,10]
        for (double x = 0; x <= 10; x += interval) {
            double f = 3 * x + 5;
            double g = 3 * x + 10;
            sum += interval * Math.abs(f - g);
        }

        System.out.println("Distance f to g: " + sum);
    }

}
