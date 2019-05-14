public class Poisson {

    public static void main(String[] argv) {
        double prob = poisson(2, 5);
        System.out.println("Pr[X=5] = " + prob);
    }

    static double poisson(double gamma, int k) {
        // INSERT YOUR CODE HERE.
        return Math.pow(Math.E, -gamma) * Math.pow(gamma, k) * factorial(k);
    }

    static double factorial(int k) {
        int temp = 1;
        for (int i = 1; i < k; i++) {
            temp *= i;
        }
        return temp;
    }

}
