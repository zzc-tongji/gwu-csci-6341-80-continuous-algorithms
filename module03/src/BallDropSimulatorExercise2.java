public class BallDropSimulatorExercise2 {

    public static void main(String[] argv) {
        BallDropSimulator2 sim = new BallDropSimulator2();

        // Drop the ball from a height of 1000.
        double height = 1000;
        sim.run(height);
        int loop = 0;
        double finalVelocity = sim.getV();

        while (Math.abs(finalVelocity + 200) > 1) {
            ++loop;
            // Try different heights systematically.
            height += 1;
            sim.run(height);
            finalVelocity = sim.getV();
        }
        System.out.println("Loop times: " + loop);
        System.out.println("Height required: " + height);
    }

}
