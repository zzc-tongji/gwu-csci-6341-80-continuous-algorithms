public class ExponentialCDF {


    public static void main(String[] argv) {
        Function F = makeExponentialCDF();
        F.show();
    }

    static Function makeExponentialCDF() {
        // INSERT YOUR CODE HERE.
        double a = 0, b = 3;
        int M = 150;                   // Number of intervals.
        double delta = (b - a) / M;     // Interval size.

        double[] intervalCounts = new double[M];
        double numTrials = 1000000;

        BusStop busStop= new BusStop(true);
        for (int t = 0; t < numTrials; t++) {
            busStop.nextBus();
            // Random sample:
            double y = busStop.getInterarrivalTime();
            // Find the right interval:
            int k = (int) Math.floor((y - a) / delta);
            // Increment the count for every interval above and including k.
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
        Function F = new Function("Exponential cdf");
        for (int k = 0; k < M; k++) {
            double midPoint = a + k * delta + delta / 2;
            F.add(midPoint, cdf[k]);
        }

        // Get derivative
        Function F_prime = new Function("Exponential");
        for (double x = F.minX(); x < F.maxX() - delta; x += delta) {
            F_prime.add(x, (F.get(x + delta) - F.get(x)) / delta);
        }

        return F_prime;
    }

}
