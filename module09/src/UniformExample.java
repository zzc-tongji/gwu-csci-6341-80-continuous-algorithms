public class UniformExample {

    public static void main(String[] argv) {
        int numTrials = 100000;
        DensityHistogram hist0 = new DensityHistogram(0, 2, 20);
        DensityHistogram hist = new DensityHistogram(0, 2, 20);
        double sum0 = 0;
        double sum = 0;
        for (int n = 0; n < numTrials; n++) {
            double temp0 = RandTool.uniform() > 0.5 ? 1 : 0;
            double temp = Math.pow(temp0, 2);
            sum0 += temp0;
            sum += temp;
            hist0.add(temp0);
            hist.add(temp);
        }
        System.out.println("E(X)        = " + sum0 / numTrials);
        System.out.println("E(g(X)=X^2) = " + sum / numTrials);
        hist0.display();
        hist.display();
    }

}