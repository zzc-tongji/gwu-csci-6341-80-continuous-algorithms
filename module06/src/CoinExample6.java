// CoinExample6.java
//
// Author: Rahul Simha
// Feb, 2008.
//
// Pr[2nd is heads], Pr[2nd heads | 1st heads]

public class CoinExample6 {

    public static void main(String[] argv) {
        double numTrials = 1000000;

        double numSuccesses = 0;                   // #times C2 occurs.
        double numFirstHeads = 0;                  // #times C1 occurs.
        double numBothHeads = 0;                   // #times both are heads.

        for (int n = 0; n < numTrials; n++) {
            //BizarreCoin coin = new BizarreCoin ();
            Coin coin = new Coin();

            int c1 = coin.flip();
            int c2 = coin.flip();

            if (c2 == 1) {
                numSuccesses++;
            }

            if (c1 == 1) {
                numFirstHeads++;
                if (c2 == 1) {
                    numBothHeads++;
                }
            }
        }

        double prob1 = numSuccesses / numTrials;
        double prob2 = numBothHeads / numFirstHeads;

        System.out.println("Pr[c2=1]=" + prob1 + "  Pr[c2=1|c1=1]=" + prob2);
    }

}
