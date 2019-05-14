public class RandomSequence5 {

    public static void main(String[] argv) {
        for (int n = 1; n <= 10000; n += 1) {
            double Wn = computeW(n);
            System.out.println("Wn, n=" + n + ": " + Wn);
        }
    }

    static double computeW(int n) {
        return Math.sqrt(n) * (computeV(n) - 0.5);
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
