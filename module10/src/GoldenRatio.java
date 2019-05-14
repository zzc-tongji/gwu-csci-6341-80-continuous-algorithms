public class GoldenRatio {

    public static void main(String[] argv) {
        goldenRatioSearch(0, 10);
    }


    static void goldenRatioSearch(double a, double b) {
        int numIterations = 50;
        int iteration = 1;
        double r = 0.618;
        double c = r * a + (1 - r) * b;
        double d = (1 - r) * a + r * b;
        double fc = computef(c);
        double fd = computef(d);
        double epsilon = 0.01;
        int numEvals = 0;

        // INSERT YOUR CODE HERE.

        double f = computef((c + d) / 2);
        System.out.println("c=" + a + " c=" + b + " bestf=" + f + " numEvals=" + numEvals);
    }


    static double computef(double x) {
        double f = 2.5 + (x - 4.71) * (x - 4.71);
        return f;
    }

}
