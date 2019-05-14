// CoinExample2.java
//
// Author: Rahul Simha
// Mar, 2008.
//
// Estimate Pr[3 heads in 5 flips]

public class CoinExample2 {

    public static void main(String[] argv) {
        // "Large" # trials.
        double numTrials = 1000000;

        // Count # times desired outcome shows up.
        double numSuccesses = 0;

        Coin coin = new Coin(0.6);           // Pr[heads]=0.6

        for (int n = 0; n < numTrials; n++) {
            // INSERT YOUR CODE HERE
            int temp = 0;
            for (int i = 0; i < 10; i++) {
                temp += coin.flip();
            }
            if (temp == 3) {
                numSuccesses++;
            }
        }

        // Estimate. (No need to cast into double's)
        double prob = numSuccesses / numTrials;

        System.out.println("Pr[3 H in 10 flips]=" + prob);
    }

}
