public class DiceExample4 {

    public static void main(String[] argv) {
        double numTrials = 100000;
        double numSuccesses = 0;
        DodgyDice dice = new DodgyDice();
        PropHistogram hist = new PropHistogram(0.5, 6.5, 6);
        for (int n = 0; n < numTrials; n++) {
            dice.roll();
            // INSERT YOUR CODE HERE
        }
        hist.display();
    }

}
