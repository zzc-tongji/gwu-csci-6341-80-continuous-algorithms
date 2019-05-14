public class CoinExample3 {

    public static void main(String[] argv) {
        // "Large" # trials.
        double numTrials = 1000000;

        // Count # times desired outcome shows up.
        double numSuccesses = 0;

        Coin coin = new Coin(0.6);           // Pr[heads]=0.6

        // INSERT YOUR CODE HERE, similar to CoinExample2.java,
        // but to solve this problem.
        for (int n = 0; n < numTrials; n++) {
            int c1 = coin.flip();
            int c2 = coin.flip();
            int c3 = coin.flip();
            if (c1 + c2 + c3 == 2) {
                numSuccesses++;
            }
        }

        // Estimate. (No need to cast into double's)
        double prob = numSuccesses / numTrials;

        System.out.println("Pr[Exactly 2 h in 3 flips]=" + prob + "  theory=" + 0.432);
    }

}
