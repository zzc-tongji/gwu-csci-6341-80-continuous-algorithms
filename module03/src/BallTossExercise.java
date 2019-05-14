
public class BallTossExercise {

    public static void main(String[] argv) {
        // Make an instance of our new simulator.
        BallTossSimulator2 sim = new BallTossSimulator2();

        // We'll start by trying 1 second.
        double stopTime = 1;
        double y = sim.run(stopTime, 50);

        // As long as we haven't hit ground, keep trying higher values of t.
        while (y > 0) {
            stopTime += 10;
            y = sim.run(stopTime, 50);
        }

        // This should be sufficient:
        System.out.println("stopTime=" + stopTime + " y=" + y);
    }

}
