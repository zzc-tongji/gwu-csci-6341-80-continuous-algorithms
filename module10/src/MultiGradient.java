public class MultiGradient {

    public static void main(String[] argv) {
        gradientDescent(0, 10, 0, 10);
    }

    static void gradientDescent(double a1, double b1, double a2, double b2) {
        int n = 1;
        double x1 = a1, x2 = a2;
        double alpha = 1;
        double epsilon = 0.001;
        double f1 = 2 * epsilon, f2 = 2 * epsilon;

        while ((Math.abs(f1) > epsilon) || (Math.abs(f2) > epsilon)) {
            f1 = computeDeriv1(x1, x2);
            f2 = computeDeriv2(x1, x2);
            x1 = x1 - alpha * f1;
            x2 = x2 - alpha * f2;
            n++;
        }

        System.out.println("Gradient descent: after n=" + n + " iterations: x1=" + x1 + " x2=" + x2);
    }

    static double computef(double x1, double x2) {
        return (x1 - 4.71) * (x1 - 4.71) + (x2 - 3.2) * (x2 - 3.2) + 2 * (x1 - 4.71) * (x1 - 4.71) * (x2 - 3.2) * (x2 - 3.2);
    }

    static double computeDeriv1(double x1, double x2) {
        //  INSERT YOUR CODE HERE.
        return 0;
    }

    static double computeDeriv2(double x1, double x2) {
        //  INSERT YOUR CODE HERE.
        return 0;
    }

}
