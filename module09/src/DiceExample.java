public class DiceExample {

    public static void main(String[] argv) {
        double numTrials = 100000;
        double numSuccesses = 0;
        Dice dice = new Dice();
        for (int n = 0; n < numTrials; n++) {
            dice.roll();
            if (dice.first() == dice.second()) {   // Win?
                numSuccesses++;                     // Count # wins.
            }
        }
        double prob = numSuccesses / numTrials;     // Estimate.
        System.out.println("Pr[win] = " + prob);
    }

}
