public class ProjectileSimulatorExample2 {

    public static void main(String[] argv) {
        // Make a new simulator object.
        ProjectileSimulator proSim = new ProjectileSimulator();

        // We want to plot d vs. t along x-axis.
        Function dxist = new Function("dx vs. t");
        Function vxist = new Function("vx vs. t");

        for (double t = 0.1; t <= 2.3; t += 0.1) {
            // mass=1, angle=37, initVel=20, s=0.01
            proSim.run(1, 37, 20, t, 0.01);
            // After the simulation is run, get the final x-value.
            dxist.add(t, proSim.getX());
            vxist.add(t, proSim.vx);
        }

        // Display.
        dxist.show();
        vxist.show();
    }

}
