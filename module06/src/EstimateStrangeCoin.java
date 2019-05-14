public class EstimateStrangeCoin {

    public static void main(String[] argv) {
        // Flip a coin 5 times.
        StrangeCoin coin = new StrangeCoin();
        double sum = 0;
        int trial = 10000;
        for (int i = 0; i < trial; i++) {
            sum += coin.flip();
        }
        System.out.println("Pr[heads] = " + sum / trial);
    }
}
