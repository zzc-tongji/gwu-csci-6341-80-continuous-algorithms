public class Zeno {

    public static void main(String[] argv) {
        System.out.println("sum(5)   = " + sum(5));
        System.out.println("sum(10)  = " + sum(10));
        System.out.println("sum(100) = " + sum(100));
        System.out.println();
        System.out.println("sumFormula(5)   = " + sumFormula(5));
        System.out.println("sumFormula(10)  = " + sumFormula(10));
        System.out.println("sumFormula(100) = " + sumFormula(100));
    }

    static double sum(int n) {
        // Write your code here.
        double x = 0.5;
        double res = 0;
        for (int i = 0; i < n; i++) {
            res += x;
            x /= 2;
        }
        return res;
    }

    static double sumFormula(int n) {
        return 1 - Math.pow(0.5, n);
    }

}
