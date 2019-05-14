public class Binomial {

    public static void main(String[] argv) {
        double prob = binomial(10, 0.6, 3);
        System.out.println("Pr[X=3] = " + prob);
    }

    static double binomial(int n, double p, int k) {
        // INSERT YOUR CODE HERE.
        double a = 1;
        for (int i = 0; i < k; i++) {
            a *= n - i;
        }
        double b = 1;
        for (int i = 1; i <= k; i++) {
            b *= i;
        }
        return a / b * Math.pow(p, k) * Math.pow(1 - p, n - k);
    }

}
