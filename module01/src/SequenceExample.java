public class SequenceExample {

    public static void main(String[] argv) {
        System.out.println("An, n=" + 10 + ": " + computeA(10));
        System.out.println("An, n=" + 10000 + ": " + computeA(10000));
        System.out.println();
        System.out.println("Cn, n=" + 10 + ": " + computeC(10));
        System.out.println("Cn, n=" + 100 + ": " + computeC(100));
    }

    static double computeA(double n) {
        // INSERT YOUR CODE HERE.
        return Math.pow((1 + 1 / n), n);
    }

    static double computeC(double n) {
        return Math.sin(n) / n;
    }

}
