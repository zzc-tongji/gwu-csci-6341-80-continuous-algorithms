public class QueueGradientDescent {

    static double mu1 = 1;
    static double mu2 = 1.5;
    static double lambda = 0.9;

    public static void main(String[] argv) {
        gradientDescent(0, 1);
    }

    static void gradientDescent(double a, double b) {
        double x = a;
        double alpha = 0.01;
        double epsilon = 0.0001;
        double deriv = 2 * epsilon;
        while (Math.abs(deriv) > epsilon) {
            deriv = computeDeriv(x);
            x = x - alpha * deriv;
        }
        System.out.println("Gradient descent: x*=" + x + " f(x*)=" + computef(x));
    }


    static double computef(double x) {
        // INSERT YOUR CODE HERE.
        return 0;
    }

    static double computeDeriv(double x) {
        // INSERT YOUR CODE HERE.
        return 0;
    }

}