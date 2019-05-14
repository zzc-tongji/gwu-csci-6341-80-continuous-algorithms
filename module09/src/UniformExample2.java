public class UniformExample2 {

    public static void main(String[] argv) {
        int numTrials = 100000;
        DensityHistogram hist = new DensityHistogram(0, 2, 20);
        double sum = 0;
        for (int n = 0; n < numTrials; n++) {
            // INSERT YOUR CODE HERE
        }
        System.out.println("Avg: " + sum / numTrials);
        hist.display();
    }

}
