public class QueueOpt {

    public static void main(String[] argv) {
        QueueControl queue = new QueueControl();

        double alpha = 0.1;
        double x = 0.5;
        double s = 0.1;

        int numIterations = 100;
        for (int n = 1; n <= numIterations; n++) {
            // Simulate with x+s
            queue.currentX = x + s;
            queue.simulate(1000);
            double fxs = queue.avgSystemTime;
            // Simulate with x
            queue.currentX = x;
            queue.simulate(1000);
            double fx = queue.avgSystemTime;
            // Apply gradient descent.
            double deriv = (fxs - fx) / s;
            x = x - alpha * deriv;
            // Print status.
            System.out.println("n=" + n + ": x=" + x + " f'=" + deriv + " f=" + fx);
        }
    }

}
