// Coin.java
//
// Author: Rahul Simha
// Feb, 2008.
//
// A simple coin.


public class Coin {

    double p = 0.5;

    public Coin() {
    }

    public Coin(double probHeads) {
        p = probHeads;
    }

    public int flip() {
        if (RandTool.uniform() < p) {
            return 1;
        }
        return 0;
    }

}
