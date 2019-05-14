public class DieRollExample2 {

    public static void main(String[] argv) {
        double numTrials = 1000000;
        double numSuccesses = 0;

        Die die = new Die();

        for (int n = 0; n < numTrials; n++) {
            // WRITE YOUR CODE HERE
            int d1 = die.roll();
            int d2 = die.roll();
            if (((d1 == 1) || (d1 == 3) || (d1 == 5)) && ((d2 == 2) || (d2 == 4) || (d2 == 6))) {
                numSuccesses++;
            }
        }

        double prob = numSuccesses / numTrials;

        System.out.println("Pr[odd+even]=" + prob + "  theory=" + 0.25);
    }

}
