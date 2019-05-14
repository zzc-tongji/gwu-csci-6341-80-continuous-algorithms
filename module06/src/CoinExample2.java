// CoinExample2.java
//
// Author: Rahul Simha
// Feb, 2008.
//
// Pr[At least 1 heads in two flips]

public class CoinExample2 {

    public static void main(String[] argv) {
        // "Large" # trials.
        double numTrials = 1000000;

        // Count # times desired outcome shows up.
        double numSuccesses = 0;

        Coin coin = new Coin(0.6);           // Pr[heads]=0.6

        for (int n = 0; n < numTrials; n++) {
            int c1 = coin.flip();            // The two flips.
            int c2 = coin.flip();

            if ((c1 == 1) || (c2 == 1)) {
                // If either resulted in heads, that's a successful outcome.
                numSuccesses++;
            }
        }

        // Estimate. (No need to cast into double's)
        double prob = numSuccesses / numTrials;

        System.out.println("Pr[At least 1 h in 2 flips]=" + prob + "  theory=" + 0.84);
    }

}
