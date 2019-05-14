// CardExample.java
//
// Author: Rahul Simha
// Feb, 2008.
//
// Pr[2 card = 13]

public class CardExample {

    public static void main(String[] argv) {
        double numTrials = 100000;
        double numSuccesses = 0;

        for (int n = 0; n < numTrials; n++) {
            // Note: we use a fresh pack each time because the cards aren't replaced.
            CardDeck deck = new CardDeck();
            int c1 = deck.drawWithoutReplacement();
            int c2 = deck.drawWithoutReplacement();
            if (c2 == 13) {
                numSuccesses++;
            }
        }
        double prob = numSuccesses / numTrials;
        System.out.println("Pr[c2=13]=" + prob + "  theory=" + (1.0 / 52.0));
    }

}
