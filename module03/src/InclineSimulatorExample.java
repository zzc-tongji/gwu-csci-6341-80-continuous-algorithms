public class InclineSimulatorExample {

    public static void main(String[] argv) {
        // Make a new instance of the class.
        InclineSimulator sim = new InclineSimulator();
        // Set mass and angle.
        sim.mass = 1;
        sim.angle = 30;
        // We'll measure distance travelled at t=1, t=2, ... and put
        // these values into a Function object.
        Function dVst = new Function("d vs. t");
        Function vVsT = new Function("v vs. t");
        double deltaT = 0.001;
        for (double t = 0; t < 10; t++) {
            double d = sim.run(t);
            double v = (sim.run(t + deltaT) - d) / deltaT;
            dVst.add(t, d);
            vVsT.add(t, v);
            System.out.println("t=" + t + "  d=" + d + "  v=" + v);

        }
        // Display result.
        dVst.show();
        vVsT.show();
    }

}

