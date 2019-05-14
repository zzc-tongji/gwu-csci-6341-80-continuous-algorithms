public class Histogram {

    static int numBins = 10;

    public static void main(String[] argv) {
        // U
        histogramForU(5, 10);
        histogramForU(5, 100);
        histogramForU(5, 10000);
        histogramForU(493, 10000);
        // V
        histogramForV(5, 10);
        histogramForV(5, 100);
        histogramForV(5, 10000);
        histogramForV(493, 10000);
        histogramForV(10000, 10000);
        // W
        histogramForW(5, 10);
        histogramForW(5, 100);
        histogramForW(5, 10000);
        histogramForW(493, 10000);
        histogramForW(10000, 10000);
    }

    static double computeU(int n) {
        return RandTool.uniform();
    }

    static double computeV(int n) {
        double sum = 0;
        for (int i = 1; i <= n; i++) {
            sum += computeU(i);
        }
        return sum / n;
    }

    static double computeW(int n) {
        return Math.sqrt(n) * (computeV(n) - 0.5);
    }


    // You don't need to modify any code below this but it's worth reading.


    static void histogramForU(int n, int numSamples) {
        // Generate the data set
        double[] data = new double[numSamples];
        for (int k = 0; k < numSamples; k++) {
            double u = computeU(n);
            data[k] = u;
        }

        // Do histogram.
        System.out.println("Histogram for U");
        makeHistogram(data);
    }

    static void histogramForV(int n, int numSamples) {
        // Generate the data set
        double[] data = new double[numSamples];
        for (int k = 0; k < numSamples; k++) {
            double v = computeV(n);
            data[k] = v;
        }

        // Do histogram.
        System.out.println("Histogram for V");
        makeHistogram(data);

    }


    static void histogramForW(int n, int numSamples) {
        // Generate the data set
        double[] data = new double[numSamples];
        for (int k = 0; k < numSamples; k++) {
            double w = computeW(n);
            data[k] = w;
        }

        // Do histogram.
        System.out.println("Histogram for W");
        makeHistogram(data);
    }

    static void makeHistogram(double[] data) {
        // Create space: one counter per interval.
        int[] bins = new int[numBins];

        // Find min/max of data.
        double min = Double.MAX_VALUE, max = Double.MIN_VALUE;
        for (int i = 0; i < data.length; i++) {
            if (data[i] < min) min = data[i];
            if (data[i] > max) max = data[i];
        }

        // Interval size.
        double interval = (max - min) / numBins;

        // Now count.
        for (int i = 0; i < data.length; i++) {
            double distFromMin = (data[i] - min);
            int whichBin = (int) Math.floor(distFromMin / interval);
            if ((whichBin >= 0) && (whichBin < numBins)) {
                bins[whichBin]++;
            }
        }

        // Print histogram.
        for (int b = 0; b < numBins; b++) {
            // Interval:
            double leftEnd = min + b * interval;
            double rightEnd = leftEnd + interval;
            System.out.printf("  b=%4d: [%5.3f, %5.3f] %6d\n", b, leftEnd, rightEnd, bins[b]);
        }

    }

}
