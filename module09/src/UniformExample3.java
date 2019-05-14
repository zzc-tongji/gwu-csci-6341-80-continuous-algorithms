public class UniformExample3 {

    public static void main(String[] argv) {
        int numTrials = 100000;
        DensityHistogram hist = new DensityHistogram(0, 2, 20);
        DensityHistogram hist0 = new DensityHistogram(0, 2, 20);
        double sum0 = 0;
        double sum = 0;
        for (int n = 0; n < numTrials; n++) {
            double temp0 = RandTool.uniform();
            double temp = Math.pow(temp0, 2);
            sum0 += temp0;
            sum += temp;
            hist0.add(temp0);
            hist.add(temp);
        }
        System.out.println("E(x)        = " + sum0 / numTrials);
        System.out.println("E(g(x)=x^2) = " + sum / numTrials);
        hist0.display();
        hist.display();
    }

}
