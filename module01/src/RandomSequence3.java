public class RandomSequence3 {

    public static void main(String[] argv) {
        double sum = 0;
        for (int n = 1; n <= 10; n++) {
            double Un = computeU(n);
            sum = sum + Un;
            double Vn = (1.0 / n) * sum;
            System.out.println("Vn, n=" + n + ": " + Vn);
        }
    }

    static double computeU(int n) {
        return RandTool.uniform();
    }

}
