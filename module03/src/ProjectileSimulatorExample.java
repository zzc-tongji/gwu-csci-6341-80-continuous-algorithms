public class ProjectileSimulatorExample {

    public static void main(String[] argv) {
        // Make a new instance of the class.
        ProjectileSimulator sim = new ProjectileSimulator();
        Function dVst = new Function("d vs. t");
        Function vVsT = new Function("v vs. t");
        Function aVsT = new Function("a vs. t");
        double mass = 1;
        double angle = 70;
        double initialVelocity = 10;
        double timeInterval = 0.001;
        for (double t = 1; t <= 10; t++) {
            double d0 = sim.run(mass, angle, initialVelocity, t, timeInterval);
            double d1 = sim.run(mass, angle, initialVelocity, t + timeInterval, timeInterval);
            double d2 = sim.run(mass, angle, initialVelocity, t + timeInterval + timeInterval, timeInterval);
            double v0 = (d1 - d0) / timeInterval;
            double v1 = (d2 - d1) / timeInterval;
            double a0 = (v1 - v0) / timeInterval;
            dVst.add(t, d0);
            vVsT.add(t, v0);
            aVsT.add(t, a0);
            System.out.println("t=" + t + "  d=" + d0 + "  v=" + v0 + "  a=" + a0);

        }
        // Display result.
        dVst.show();
        vVsT.show();
        aVsT.show();
    }

}
