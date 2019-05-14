public class ProjectileExercise {

    public static void main(String[] argv) {
        // Make an instance of our new simulator.
        ProjectileSimulator2 sim = new ProjectileSimulator2();

        // Launch velocity is 50.
        double initV = 50;

        // Try angles in the range [10,80]
        for (double angle = 10; angle <= 80; angle += 1) {
            sim.run(angle, initV);
            double x = sim.getX();
            System.out.println("angle=" + angle + " x=" + x);
        }

    }

}
