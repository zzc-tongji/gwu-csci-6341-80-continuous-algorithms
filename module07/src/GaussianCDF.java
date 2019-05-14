public class GaussianCDF {


    public static void main(String[] argv) {
        Function F = makeGaussianCDF();
        F.show();
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
        Function F = new Function("Gaussian cdf");
        for (int k = 0; k < M; k++) {
            double midPoint = a + k * delta + delta / 2;
            F.add(midPoint, cdf[k]);
        }

        // Get derivative
        Function F_prime = new Function("Gaussian");
        for (double x = F.minX(); x < F.maxX() - delta; x += delta) {
            F_prime.add(x, (F.get(x + delta) - F.get(x)) / delta);
        }

        return F_prime;
    }

}
