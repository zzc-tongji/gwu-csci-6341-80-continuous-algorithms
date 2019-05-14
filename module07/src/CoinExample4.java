// CoinExample4.java
//
// #flips needed for first heads

public class CoinExample4 {

    public static void main(String[] argv) {
        int range = 1000;
        double numTrials = 1000000;
        int[] hit = new int[range];
        Coin coin = new Coin(0.1);
        for (int t = 0; t < numTrials; t++) {
            for (int i = 0; i < range; i++) {
                if (coin.flip() == 1) {
                    hit[i] += 1;
                    break;
                }
            }
        }
        int S_n = 0;
        for (int i = 0; i < range; i++) {
            S_n += (i + 1) * hit[i];
        }
        System.out.println("range: 0 - " + range);
        System.out.println("average value of X: " + (S_n / numTrials));
        System.out.println("Pr[X=1] = " + (hit[0] / numTrials));
        System.out.println("Pr[X=2] = " + (hit[1] / numTrials));
        System.out.println("Pr[X=3] = " + (hit[2] / numTrials));
        double sum_k = 0;
        for (int i = 0; i < range; i++) {
            sum_k += (i + 1) * hit[i] / numTrials;
        }
        System.out.println("\\sum_{k}k\\frac{n_{k}}{n} = " + sum_k);
    }

}