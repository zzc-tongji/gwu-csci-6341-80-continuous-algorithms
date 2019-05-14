public class RandomSequence4 {

    public static void main(String[] argv) {
        for (int sample = 1; sample <= 20; sample++) {
            double u = computeU(493);
            System.out.println(u);
        }
    }

    static double computeU(int n) {
        return RandTool.uniform();
    }

}
