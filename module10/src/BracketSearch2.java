public class BracketSearch2 {

    public static void main(String[] argv) {
        bracketSearch(0, 100);
    }


    static void bracketSearch(double a, double b) {
        int numEvals = 0;
        int M = 6;
        double bestx = a;
        double bestf = Double.MAX_VALUE;
        double prevBestf = Double.MIN_VALUE;
        double epsilon = 0.1;
        while (Math.abs((bestf - prevBestf) / bestf) > epsilon || bestf == prevBestf) {
            prevBestf = bestf;
            double delta = (b - a) / M;
            for (double x = a + delta; x <= b; x += delta) {
                double f = computef(x);
                if (f < bestf) {
                    bestf = f;
                    bestx = x;
                }
                numEvals += 1;
            }
            a = bestx - delta;
            b = bestx + delta;
        }

        System.out.println("numEvals=" + numEvals + " bestx=" + bestx + " bestf=" + bestf + " prevBestf=" + prevBestf);
    }


    static double computef(double x) {
        double f = 2.5 + (x - 4.71) * (x - 4.71);
        return f;
    }

}
