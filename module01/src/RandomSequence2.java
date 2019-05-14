public class RandomSequence2 {

    public static void main(String[] argv) {
        for (int n = 1; n <= 10; n++) {
            System.out.println("Vn, n=" + n + ": " + computeV(n));
        }
    }

    static double computeV(int n) {
        double sum = 0;
        for (int i = 1; i <= n; i++) {
            sum += computeU(i);
        }
        return sum / n;
    }

    static double computeU(int n) {
        return RandTool.uniform();
    }

}
