public class MultiBracketSearch {

    public static void main(String[] argv) {
        bracketSearch(0, 10, 0, 10);
    }


    static void bracketSearch(double a1, double b1, double a2, double b2) {
        int numEvals = 0;
        int M = 6;
        int N = 4;
        double bestx1 = a1, bestx2 = a2;

        // INSERT YOUR CODE HERE
        double bestf = computef(bestx1, bestx2);
        for (int i = 0; i < N; i++) {
            double delta1 = (b1 - a1) / M, delta2 = (b2 - a2) / M;
            for (double x1 = a1 + delta1; x1 <= b1; x1 += delta1) {
                for (double x2 = a2 + delta2; x2 <= b2; x2 += delta2) {
                    double f = computef(x1, x2);
                    if (f < bestf) {
                        bestf = f;
                        bestx1 = x1;
                        bestx2 = x2;
                    }
                    numEvals += 1;
                }
            }
            a1 = bestx1 - delta1;
            b1 = bestx1 + delta1;
            a2 = bestx2 - delta2;
            b2 = bestx2 + delta2;
        }

        System.out.println("Bracketing search: x1=" + bestx1 + " x2=" + bestx2 + " numFuncEvals=" + numEvals);
    }


    static double computef(double x1, double x2) {
        // INSERT YOUR CODE HERE
        return (x1 - 4.71) * (x1 - 4.71) + (x2 - 3.2) * (x2 - 3.2) + 2 * (x1 - 4.71) * (x1 - 4.71) * (x2 - 3.2) * (x2 - 3.2);
    }

}
