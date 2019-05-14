public class BallDropSimulatorExercise {

    public static void main(String[] argv) {
        BallDropSimulator2 sim = new BallDropSimulator2();

        // Drop the ball from a height of 1000.
        sim.run(1000);

        // Obtain final velocity and time taken for the fall.
        double finalVelocity = sim.getV();
        double timeTaken = sim.getT();

        System.out.println("finalVelocity=" + finalVelocity + " time=" + timeTaken);
    }

}
