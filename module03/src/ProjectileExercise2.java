public class ProjectileExercise2 {

    public static void main(String[] argv) {
        // Make an instance of our new simulator.
        ProjectileSimulator2 sim = new ProjectileSimulator2();

        // Launch velocity is 50.
        double initV = 50;

        // Try angles in the range [10,80]
        double bestAngle = 10;
        double bestX = 0;

        for (double angle = 10; angle <= 80; angle += 1) {
            sim.run(angle, initV);
            double x = sim.getX();
            if (x > bestX) {
                bestAngle = angle;
                bestX = x;
            }
        }

        System.out.println("Best angle: " + bestAngle);
    }

}
