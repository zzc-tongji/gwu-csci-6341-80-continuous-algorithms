// CoinExample3.java
//
// Estimate Pr[X=k] for 3-coin-flip example.

public class CoinExample3 {

    public static void main(String[] argv) {
        double numTrials = 1000000;
        int[] hit = new int[4];
        Coin coin = new Coin(0.6);
        for (int t = 0; t < numTrials; t++) {
            int temp = coin.flip() + coin.flip() + coin.flip();
            switch (temp) {
                case 0:
                    hit[0] += 1;
                    break;
                case 1:
                    hit[1] += 1;
                    break;
                case 2:
                    hit[2] += 1;
                    break;
                case 3:
                    hit[3] += 1;
                default:
                    break;
            }
        }
        int S_n = 0 * hit[0] + 1 * hit[1] + 2 * hit[2] + 3 * hit[3];
        System.out.println("average value of X: " + (S_n / numTrials));
        System.out.println("Pr[X=0] = " + (hit[0] / numTrials));
        System.out.println("Pr[X=1] = " + (hit[1] / numTrials));
        System.out.println("Pr[X=2] = " + (hit[2] / numTrials));
        System.out.println("Pr[X=3] = " + (hit[3] / numTrials));
        double sum_k = 0 * hit[0] / numTrials + 1 * hit[1] / numTrials + 2 * hit[2] / numTrials + 3 * hit[3] / numTrials;
        System.out.println("\\sum_{k}k\\frac{n_{k}}{n} = " + sum_k);
    }

}
