// GaussianExample.java
//
// Histogram for Gaussian data

public class GaussianExample {

    public static void main(String[] argv) {
        // "Large" # trials.
        double numTrials = 100000;

        PropHistogram hist = new PropHistogram(-2, 2, 10);

        for (int t = 0; t < numTrials; t++) {
            double x = RandTool.gaussian();
            hist.add(x);
        }

        // View the histogram, text and graph.
        System.out.println(hist);
        hist.display();
    }

}
