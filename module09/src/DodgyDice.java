// DodgyDice.java
//
// Author: Rahul Simha
// Mar 2008
// 
// Dice example with dependence.


public class DodgyDice {

    int first;
    int second;

    public void roll() {
        first = RandTool.uniform(1, 6);
        if (RandTool.uniform() < 0.5) {
            second = first;
        } else {
            second = RandTool.uniform(1, 6);
        }
    }

    public int first() {
        return first;
    }

    public int second() {
        return second;
    }

}
