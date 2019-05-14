public class CoinExample {

    public static void main(String[] argv) {
        // Flip a coin 5 times.
        Coin coin = new Coin();
        for (int i = 0; i < 5; i++) {
            int c = coin.flip();                           // Returns 1 (heads) or 0 (tails).
            System.out.println("Flip #" + i + ": " + c);
        }
    }

}
