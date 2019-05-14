public class UniformMeanExample3 {

    public static void main(String[] argv) {
        // We'll build a density-histogram in the range [-2,2] with 20 intervals.
        DensityHistogram hist = new DensityHistogram(-2, 2, 20);

        // Number of runs over which to collect samples of S'_n/n
        double numRuns = 100000;

        for (int r = 0; r < numRuns; r++) {

            double n = 5;
            double total = 0;
            for (int i = 1; i <= n; i++) {
                double x = RandTool.uniform();   // X_i = outcome of i-th call to uniform()
                total += x;
            }
            double avg = total / n;       // S_n / n
            hist.add(avg - 0.5);           // S'_n / n
        }

        hist.display();
    }

}
