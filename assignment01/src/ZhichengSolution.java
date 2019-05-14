import java.util.ArrayList;

public class ZhichengSolution {

    private int A; // unit: 1/100 rad, must be multiples of 5
    private int v1; // range: [0, 10]
    private int v2; // range: [0, 10]
    private int globalTime; // unit: ms
    private ArrayList<ZhichengStep> stepList;
    private DubinCarSimulator simulator;

    public ZhichengSolution(int A, int v1, int v2) {
        // set input
        this.A = A;
        this.v1 = v1;
        this.v2 = v2;
        // initialize output
        this.globalTime = 0;
        this.stepList = new ArrayList<>();
        // initialize simulator
        this.simulator = new DubinCarSimulator(false);
        this.simulator.init(50, 50, 0, null);
    }

    public ArrayList<ZhichengStep> getStepList() {
        return stepList;
    }

    public int getGlobalTime() {
        return globalTime;
    }

    public boolean run() {
        // restriction #1: invalid `v1` and `v2`
        if (v1 < v2 || v2 < 0) {
            return false;
        }
        // phase #1: rotate
        if (!phase1()) {
            return false;
        }
        // phase #2: move
        if (!phase2()) {
            return false;
        }
        // phase #3: rotate
        if (!phase3()) {
            return false;
        }
        // check if arrived
        if (isArrived()) {
            return true;
        }
        // phase #4: move
        if (!phase4()) {
            return false;
        }
        return true;
    }

    private boolean phase1() {
        // turn left to `A` rad
        //
        // calculate
        int move10Count = A / 50;
        int moveX = (A % 50) / 5;
        // turn
        for (int i = move10Count; i > 0; i--) {
            if (!move(-10, 10)) {
                return false;
            }
        }
        if (moveX != 0) {
            if (!move(-moveX, moveX)) {
                return false;
            }
        }
        return true;
    }


    private boolean phase2() {
        // move by `v1` and `v2`
        while (true) {
            if (!move(v1, v2)) {
                return false;
            }
            if (simulator.getY() <= 50) {
                // 45 <= y <= 50
                return true;
            }
        }
    }

    private boolean phase3() {
        // turn left or right to face right (0 rad)
        double theta = simulator.getTheta();
        double rotate;
        double moveTotal;
        int move10Counter;
        double moveX;
        if (theta > Math.PI) {
            // calculate
            rotate = 2 * Math.PI - theta;
            moveTotal = rotate / 0.05;
            move10Counter = (int) Math.floor(moveTotal / 10);
            moveX = moveTotal - 10 * move10Counter;
            // turn left
            for (int i = move10Counter; i > 0; i--) {
                if (!move(-10, 10)) {
                    return false;
                }
            }
            if (moveX != 0) {
                if (!move(-moveX, moveX)) {
                    return false;
                }
            }
        } else {
            // calculate
            rotate = theta;
            moveTotal = rotate / 0.05;
            move10Counter = (int) Math.floor(moveTotal / 10);
            moveX = moveTotal - 10 * move10Counter;
            // turn right
            for (int i = move10Counter; i > 0; i--) {
                if (!move(10, -10)) {
                    return false;
                }
            }
            if (moveX != 0) {
                if (!move(moveX, -moveX)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean phase4() {
        // move straightforward at top speed
        while (true) {
            if (!move(10, 10)) {
                return false;
            }
            if (isArrived()) {
                return true;
            }
        }
    }

    private boolean move(double left, double right) {
        // move and verify
        simulator.nextStep(left, right, 0.1);
        globalTime += 100;
        stepList.add(new ZhichengStep(left, right, 100, globalTime));
        return verify();
    }

    private boolean verify() {
        // verify whether the coordinate exceeds the restriction
        double x = simulator.getX();
        double y = simulator.getY();
        // restriction #2: exceed the border
        if (x < 50 || x > 500 || y < 45 || y > 475) {
            return false;
        }
        // restriction #3: hit the obstacle
        if (x >= 140 && x <= 260 && y >= -10 && y <= 110) {
            return false;
        }
        // time overflow
        if (globalTime > Integer.MAX_VALUE - 100) {
            return false;
        }
        return true;
    }

    private boolean isArrived() {
        // verify whether the coordinate is in target area
        double x = simulator.getX();
        double y = simulator.getY();
        return (x >= 495 && x <= 500 && y >= 45 && y <= 55);
    }
}
