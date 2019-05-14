public class RandomSequence {

    public static void main(String[] argv) {
        for (int n = 1; n <= 10; n++) {
            System.out.println("Un, n=" + n + ": " + computeU(n));
        }
    }

    static double computeU(int n) {
        return RandTool.uniform();
    }

}
