public class GeometricGenerator {

    public static void main(String[] argv) {
        int numTrials = 100000;

        // Geometric parameter.
        double p = 0.6;

        int m = 5;  // Limit for the purpose of testing.

        // Generate regular histogram.
        double[] counts = new double[m];
        for (int n = 0; n < numTrials; n++) {
            // int k = generateNext(0.6);
            // if (k < m) {
            //    counts[k]++;
            // }
        }

        // Make into proportions.
        double[] probEst = new double[m];
        for (int k = 1; k < m; k++) {
            probEst[k] = counts[k] / numTrials;
            double t = Math.pow(1 - p, k - 1) * p;
            System.out.println("Estimate of Pr[X=" + k + "]: " + probEst[k] + "   theory: " + t);
        }
    }


    // static int generateNext(double p) {
    //     INSERT YOUR CODE HERE.
    // }

}
