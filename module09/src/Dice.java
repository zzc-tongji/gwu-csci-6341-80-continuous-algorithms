// Dice.java
//
// Author: Rahul Simha
// Mar 2008
//
// Simple pair of dice.

public class Dice {

    int first;
    int second;

    public void roll() {
        first = RandTool.uniform(1, 6);
        second = RandTool.uniform(1, 6);
    }

    public int first() {
        return first;
    }

    public int second() {
        return second;
    }

}
