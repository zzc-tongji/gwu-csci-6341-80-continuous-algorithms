// CardDeck.java
//
// Author: Rahul Simha
// Feb, 2008
//
// A deck of cards, and some operations.
//
// Convention: 
//   Spades: 0..12
//   Hearts: 13..25
//   Diamonds: 26..38
//   Clubs: 39..51

public class CardDeck {

    int size = 52;
    int[] cards;


    public CardDeck() {
        cards = new int[size];
        for (int i = 0; i < size; i++) {
            cards[i] = i;
        }
        shuffle();
    }


    public void shuffle() {
        for (int i = 0; i < size; i++) {
            // Pick a random card for the i-th position.
            int k = RandTool.uniform(0, size - 1);
            // Swap.
            int temp = cards[i];
            cards[i] = cards[k];
            cards[k] = temp;
        }
    }


    public int drawWithReplacement() {
        return RandTool.uniform(0, size - 1);
    }


    public int drawWithoutReplacement() {
        int k = RandTool.uniform(0, size - 1);
        int drawnCard = cards[k];
        // Shift left.
        for (int i = k; i < size - 1; i++) {
            cards[i] = cards[i + 1];
        }
        size--;
        return drawnCard;
    }


    public boolean isSpade(int c) {
        if ((c >= 0) && (c <= 12)) {
            return true;
        }
        return false;
    }


    public boolean isHeart(int c) {
        if ((c >= 13) && (c <= 25)) {
            return true;
        }
        return false;
    }


    public boolean isClub(int c) {
        if ((c >= 26) && (c <= 38)) {
            return true;
        }
        return false;
    }


    public boolean isDiamond(int c) {
        if ((c >= 39) && (c <= 51)) {
            return true;
        }
        return false;
    }

}
