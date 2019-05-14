public class Harmonic {

    public static void main(String[] argv) {
        System.out.println("sumDouble(100): " + sumDouble(100));
        System.out.println("sumFloat(100):  " + sumFloat(100));
    }

    static double sumDouble(int n) {
        // INSERT YOUR CODE HERE.
        double res = 0;
        for (double i = 1; i <= n; i += 1) {
            res += 1 / i;
        }
        return res;
    }

    static float sumFloat(int n) {
        // INSERT YOUR CODE HERE.
        float res = 0;
        for (float i = 1; i <= n; i += 1) {
            res += 1 / i;
        }
        return res;
    }

}
