// LabTest.java
//
// Author: Rahul Simha
// Feb, 2008
//
// Simulate a disease test.


public class LabTest {

    double probSick = 0.05;           // 5% of population is infected.
    double probSickTestsPos = 0.99;   // Pr[Pos|Sick]
    double probWellTestsPos = 0.03;   // Pr[Pos|Well]

    boolean isSick;

    public LabTest(double probSick, double probSickTestsPos, double probWellTestsPos) {
        this.probSick = probSick;
        this.probSickTestsPos = probSickTestsPos;
        this.probWellTestsPos = probWellTestsPos;
    }


    public void nextPatient() {
        if (RandTool.uniform() < probSick) {
            isSick = true;
        } else {
            isSick = false;
        }
    }

    public boolean isSick() {
        return isSick;
    }

    public boolean testedPositive() {
        if (isSick) {
            if (RandTool.uniform() < probSickTestsPos) {
                return true;
            }
            return false;
        }
        if (RandTool.uniform() < probWellTestsPos) {
            return true;
        }
        return false;
    }

}
