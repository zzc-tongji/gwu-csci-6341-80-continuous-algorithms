public class BracketSearch {


    public static void main(String[] argv) {
        bracketSearch(0, 10);
    }


    static void bracketSearch(double a, double b) {
        int M = 3;
        int N = 8;
        double bestx = a;
        double bestf = computef(bestx);
        for (int i = 0; i < N; i++) {
            double delta = (b - a) / M;
            for (double x = a + delta; x <= b; x += delta) {
                double f = computef(x);
                if (f < bestf) {
                    bestf = f;
                    bestx = x;
                }
            }
            a = bestx - delta;
            b = bestx + delta;
        }
        System.out.println("a=" + a + " b=" + b + " bestf=" + bestf);
    }


    static double computef(double x) {
        double f = 2.5 + (x - 4.71) * (x - 4.71);
        return f;
    }

}
