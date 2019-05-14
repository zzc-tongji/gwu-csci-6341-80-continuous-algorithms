public class ExponentialGenerator {

    static double x = 1;

    public static void main(String[] argv) {
        int numTrials = 100000;

        // Exponential parameter.
        double gamma = 4.0;

        // Make a density histogram. NOTE: change code in PropHistogram accordingly.
        PropHistogram hist = new PropHistogram(0, 5, 20);
        for (int n = 0; n < numTrials; n++) {
            double x = generateNext(gamma);
            hist.add(x);
        }

        hist.display();
    }


    static double generateNext(double gamma) {
        // INSERT YOUR CODE HERE.
        return -Math.log((1 - uniform()) / gamma);
    }

    static double uniform() {
        int M = 1 << 16 - 1;
        int a = 48271;
        x = (a * x) % M;
        return x / (double) M;
    }

}
