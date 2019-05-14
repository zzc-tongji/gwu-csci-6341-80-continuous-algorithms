public class ProjectileSimulatorExample3 {

    public static void main(String[] argv) {
        // Make a new simulator object.
        ProjectileSimulator proSim = new ProjectileSimulator();

        // We want to plot d vs. t along y-axis.
        Function dyist = new Function("dy vs. t");
        Function vyist = new Function("vy vs. t");

        for (double t = 0.1; t <= 2.3; t += 0.1) {
            // mass=1, angle=37, initVel=20, s=0.01
            proSim.run(1, 37, 20, t, 0.01);
            // After the simulation is run, get the final y-value.
            dyist.add(t, proSim.getY());
            vyist.add(t, proSim.vy);
        }

        // Display.
        dyist.show();
        vyist.show();
    }

}
