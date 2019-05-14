// CoinExample5.java
//
// Histogram for 3-coin-flip example.

public class CoinExample5 {

    public static void main(String[] argv) {
        // "Large" # trials.
        double numTrials = 10000;

        Coin coin = new Coin(0.6);
        PropHistogram hist = new PropHistogram(-0.5, 3.5, 4);

        for (int t = 0; t < numTrials; t++) {
            // #heads in 3 flips.
            int numHeads = coin.flip() + coin.flip() + coin.flip();
            hist.add(numHeads);
        }

        // View the histogram, text and graph.
        System.out.println(hist);
        hist.display();
    }

}
