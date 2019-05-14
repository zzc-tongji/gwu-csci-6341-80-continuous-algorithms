public class DiscreteGenExample {

    static int x = 1;

    public static void main(String[] argv) {
        // INSERT YOUR CODE FOR TESTING (HISTOGRAM) HERE.
        PropHistogram histogram = new PropHistogram(-0.5, 3.5, 4);
        for (int i = 0; i < 1000000; i++) {
            histogram.add(generateNext());
        }
        histogram.display();
    }


    static int generateNext() {
        // INSERT YOUR CODE HERE.
        double u = uniform();
        if (u < 0.064) {
            return 0;
        } else if (u < 0.064 + 0.288) {
            return 1;
        } else if (u < 0.064 + 0.288 + 0.432) {
            return 2;
        }
        return 3;
    }

    static double uniform() {
        int M = 1 << 16 - 1;
        int a = 48271;
        x = (a * x) % M;
        return x / (double) M;
    }

}
