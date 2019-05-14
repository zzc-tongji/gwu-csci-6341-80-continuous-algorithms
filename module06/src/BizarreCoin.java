// File: BizarreCoin.java
//
// Author: Rahul Simha
// Feb, 2008
//
// To illustrate dependence.


public class BizarreCoin {

    int prevFlip = -1;

    double p = 0.5;

    public int flip() {
        int flip = 1;
        if (prevFlip < 0) {
            // First time through.
            if (RandTool.uniform() < p) {
                flip = 1;
            } else {
                flip = 0;
            }
        } else if (RandTool.uniform() < 0.5) {
            flip = prevFlip;
        } else if (RandTool.uniform() < p) {
            flip = 1;
        } else {
            flip = 0;
        }

        prevFlip = flip;
        return flip;
    }

}
