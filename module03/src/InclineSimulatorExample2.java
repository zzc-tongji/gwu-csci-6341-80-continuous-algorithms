public class InclineSimulatorExample2 {

    public static void main(String[] argv) {
        // Make a new instance of the class.
        InclineSimulator sim = new InclineSimulator();

        // Set mass and angle.
        sim.mass = 1;
        sim.angle = 30;

        // Measure x(t) = distance moved along x-axis.
        Function dxist = new Function("dx vs. t");
        Function vxVsT = new Function("vx vs. t");

        for (double t = 1; t <= 10; t += 1) {
            sim.run(t);
            dxist.add(t, sim.getX());
            vxVsT.add(t, sim.getV() * Math.cos(Math.toRadians(sim.angle)));
        }

        // Display result.
        dxist.show();
        vxVsT.show();
    }

}
