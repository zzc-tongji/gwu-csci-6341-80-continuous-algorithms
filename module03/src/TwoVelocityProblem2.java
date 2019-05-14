public class TwoVelocityProblem2 {

    public static void main(String[] argv) {
        double y = findBestY(1, 1.5, 10, 10, 5);
        System.out.println("best y=" + y);
    }


    static double findBestY(double v1, double v2, double a, double b, double x) {
        // FILL IN YOUR CODE HERE.
        double optimalY = a;
        double optimaltime = Double.MAX_VALUE;
        for (double y = a; y > 0; y -= 0.001) {
            double temp = timeTaken(v1, v2, a, b, x, y);
            if (temp <= optimaltime) {
                optimaltime = temp;
                optimalY = y;
            }
        }
        return optimalY;
    }


    static double timeTaken(double v1, double v2, double a, double b, double x, double y) {
        // FILL IN YOUR CODE HERE.
        return distance(0, a, x, y) / v1 + distance(x, y, b, 0) / v2;
    }


    static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

}
