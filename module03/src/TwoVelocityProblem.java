public class TwoVelocityProblem {

    public static void main(String[] argv) {
        double t = timeTaken(1, 1.5, 10, 10, 5, 6);
        System.out.println("time=" + t);
    }


    static double timeTaken(double v1, double v2, double a, double b, double x, double y) {
        // FILL IN YOUR CODE HERE.
        return distance(0, a, x, y) / v1 + distance(x, y, b, 0) / v2;
    }


    static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

}
