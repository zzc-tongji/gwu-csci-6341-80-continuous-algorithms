// DieRollExample.java
//
// Author: Rahul Simha
// Feb, 2008.
//
// Pr[die-roll results in odd#]

public class DieRollExample {

    public static void main(String[] argv) {
        double numTrials = 1000000;
        double numSuccesses = 0;

        Die die = new Die();

        for (int n = 0; n < numTrials; n++) {
            int k = die.roll();
            if ((k == 1) || (k == 3) || (k == 5)) {
                numSuccesses++;
            }
        }

        double prob = numSuccesses / numTrials;

        System.out.println("Pr[odd]=" + prob + "  theory=" + 0.5);
    }

}
