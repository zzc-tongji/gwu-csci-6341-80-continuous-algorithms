public class FunctionComparison6 {

    public static void main(String[] argv) {
        // Initialize sum.
        double sum = 0;

        // Compute interval = range/number-of-points:
        double interval = (10.0 - 0.0) / 50.0;

        // Generate 50 values in the range [0,10]
        for (double x = 0; x <= 10; x += interval) {
            double f = 3 * x + 5;
            double z = 0;
            sum += interval * Math.abs(f - z);
        }

        System.out.println("Distance f to x-axis: " + sum);
    }

}
