public class GaussianCDF2 {


    public static void main(String[] argv) {
        Function F = makeGaussianCDF();

        // INSERT YOUR CODE HERE
        double a = -2, b = 2;
        int M = 50;                   // Number of intervals.
        double delta = (b - a) / M;     // Interval size.
        double exValue = 0;
        for (int k = 0; k < M; k++) {
            double midPoint = a + k * delta + delta / 2;
            double x_i = a + k * delta;
            exValue += midPoint * (F.get(x_i + delta) - F.get(x_i));
        }
        System.out.println("Gaussian ex: " + exValue);
    }

    static Function makeGaussianCDF() {
        double a = -2, b = 2;
        int M = 50;                   // Number of intervals.
        double delta = (b - a) / M;     // Interval size.

        double[] intervalCounts = new double[M];
        double numTrials = 1000000;

        for (int t = 0; t < numTrials; t++) {
            // Random sample:
            double y = RandTool.gaussian();
            // Truncate:
            if (y < a) {
                y = a;
            }
            if (y > b) {
                y = b;
            }

            // Find the right interval:
            int k = (int) Math.floor((y - a) / delta);
            // Increment the count for every interval above and including k.
            if (k < 0) {
                System.out.println("k=" + k + " y=" + y + " (y-a)=" + (y - a));
            }

            for (int i = k; i < M; i++) {
                intervalCounts[i]++;
            }
        }

        // Now compute probabilities for each interval.
        double[] cdf = new double[M];
        for (int k = 0; k < M; k++) {
            cdf[k] = intervalCounts[k] / numTrials;
        }

        // Build the CDF. Use mid-point of each interval.
        Function F = new Function("Uniform cdf");
        for (int k = 0; k < M; k++) {
            double midPoint = a + k * delta + delta / 2;
            F.add(midPoint, cdf[k]);
        }

        return F;
    }

}
