public class InclineSimulatorExample3 {

    public static void main(String[] argv) {
        // Make a new instance of the class.
        InclineSimulator sim = new InclineSimulator();
        InclineSimulatorXY simXY = new InclineSimulatorXY();

        // Set mass and angle.
        sim.mass = 1;
        sim.angle = 30;
        simXY.mass = 1;
        simXY.angle = 30;

        // Measure x(t) = distance moved along x-axis.
        Function dist = new Function("dist");
        Function distXY = new Function("distXY");
        Function axVsT = new Function("ax vs. t");
        Function ayVsT = new Function("ay vs. t");

        for (double t = 1; t <= 10; t += 1) {
            double d = sim.run(t);
            double dXY = simXY.run(t);
            dist.add(t, d);
            distXY.add(t, dXY);
            axVsT.add(t, simXY.ax);
            ayVsT.add(t, simXY.ay);
        }

        // Display result.
        dist.show();
        distXY.show();
        axVsT.show();
        ayVsT.show();
    }

}
