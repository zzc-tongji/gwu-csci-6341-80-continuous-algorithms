public class BallTossExercise3 {

    public static void main(String[] argv) {
        double targetHeight = 1000;
        double error = 1;

        double low = 0;
        double high = 1000;
        double middle;
        double heightReached;
        while (true) {
            middle = (low + high) / 2;
            heightReached = getMaxHeight(middle);
            System.out.println("initV=" + middle + "  height=" + heightReached);
            if (heightReached > targetHeight + error) {
                high = middle;
            } else if (heightReached < targetHeight - error) {
                low = middle;
            } else {
                break;
            }
        }
        System.out.println("initV=" + middle + "  height=" + heightReached);
    }

    static double getMaxHeight(double initVelocity) {
        // Make an instance of the simulator.
        BallTossSimulator2 sim = new BallTossSimulator2();

        // Find the height reached at t=1 and t=2
        double stopTime = 1;
        double prevY = sim.run(stopTime, initVelocity);
        stopTime = 2;
        double nextY = sim.run(stopTime, initVelocity);

        while (nextY > prevY) {
            // As long as y(t+1) > y(t), repeat.
            prevY = sim.run(stopTime, initVelocity);
            stopTime = stopTime + 1;
            nextY = sim.run(stopTime, initVelocity);
        }

        // Now we're sure that y(t+1) < y(t)
        System.out.println("initV=" + initVelocity + "  stopTime=" + stopTime + " prevY=" + prevY + " nextY=" + nextY);
        return prevY;
    }

}
