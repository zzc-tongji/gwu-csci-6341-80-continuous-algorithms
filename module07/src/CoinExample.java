// CoinExample.java
//
// Author: Rahul Simha
// Mar, 2008.
//
// Estimate Pr[1st heads appears on 3rd flip]

public class CoinExample {

    public static void main(String[] argv) {
        // "Large" # trials.
        double numTrials = 1000000;

        // Count # times desired outcome shows up.
        double numSuccesses = 0;

        Coin coin = new Coin(0.6);           // Pr[heads]=0.6

        for (int n = 0; n < numTrials; n++) {
            // INSERT YOUR CODE HERE
            if (coin.flip() == 0 && coin.flip() == 0 && coin.flip() == 1) {
                numSuccesses++;
            }
        }

        // Estimate. (No need to cast into double's)
        double prob = numSuccesses / numTrials;

        System.out.println("Pr[1st h appears on 3rd flip]=" + prob);
    }

}
