public class CoinExample4 {

    public static void main(String[] argv) {
        // "Large" # trials.
        double numTrials = 1000000;

        // Count # times desired outcome shows up.
        double numSuccesses = 0;

        Coin coin = new Coin(0.6);           // Pr[heads]=0.6

        for (int n = 0; n < numTrials; n++) {
            // WRITE YOUR CODE HERE
            int c1 = coin.flip();
            int c2 = coin.flip();
            int c3 = coin.flip();
            if (c1 == 0 && c2 == 0 && c3 == 1) {
                numSuccesses++;
            }
        }

        // Estimate. (No need to cast into double's)
        double prob = numSuccesses / numTrials;

        System.out.println("Pr[only 3rd is heads]=" + prob + "  theory=" + 0.096);
    }

}
