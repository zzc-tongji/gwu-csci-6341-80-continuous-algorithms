public class ProjectileExercise3 {

    public static void main(String[] argv) {
        ProjectileSimulator2 sim = new ProjectileSimulator2();
        double angle;
        double velocity;

        angle = 45;
        velocity = calculate(angle);
        sim.run(angle, velocity);
        System.out.println("velocity=" + velocity + ", angle=" + angle + "deg" + ", distance=" + sim.getX());

        angle = 60;
        velocity = calculate(angle);
        sim.run(angle, velocity);
        System.out.println("velocity=" + velocity + ", angle=" + angle + "deg" + ", distance=" + sim.getX());
    }

    public static double calculate(double angle) {
        return Math.sqrt(1960 / Math.sin(Math.toRadians(2 * angle)));
    }
}
