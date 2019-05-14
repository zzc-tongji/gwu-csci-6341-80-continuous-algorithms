public class BallTossExercise2 {

    public static void main(String[] argv) {
        // Make an instance of our new simulator.
        BallTossSimulator2 sim = new BallTossSimulator2();

        // Find the height reached at t=1 and t=2
        double stopTime = 1;
        double prevY = sim.run(stopTime, 50);
        stopTime = 2;
        double nextY = sim.run(stopTime, 50);

        while (nextY > prevY) {
            // As long as y(t+1) > y(t), repeat.
            prevY = sim.run(stopTime, 50);
            stopTime = stopTime + 1;
            nextY = sim.run(stopTime, 50);
        }

        // Now we're sure that y(t+1) < y(t)
        System.out.println("stopTime=" + stopTime + " prevY=" + prevY + " nextY=" + nextY);
    }

}
