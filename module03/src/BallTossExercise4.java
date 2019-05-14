public class BallTossExercise4 {

    public static void main(String[] argv) {
        // Make an instance of modified simulator.
        BallTossSimulator3 sim = new BallTossSimulator3();

        sim.run(1000, 50, 100, true);
        double beforeV = sim.getV();
        sim.run(1000, 50, 100, false);
        double afterV = sim.getV();

        System.out.println("before: " + beforeV + "  after: " + afterV);
    }

}
