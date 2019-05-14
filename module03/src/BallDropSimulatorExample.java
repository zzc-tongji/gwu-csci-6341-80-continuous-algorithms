public class BallDropSimulatorExample {

    public static void main(String[] argv) {
        BallDropSimulator sim = new BallDropSimulator();
        Function dVst = new Function("d vs. t");
        Function vVsT = new Function("v vs. t");
        Function aVsT = new Function("a vs. t");
        double deltaT = 0.01;
        double height = 1000;
        for (double t = 0; t < 10; t++) {
            double d = sim.run(t, height);
            double dPrime = sim.run(t + deltaT, height);
            double dPrimePrime = sim.run(t + deltaT + deltaT, height);
            double v = (dPrime - d) / deltaT;
            double vPrime = (dPrimePrime - dPrime) / deltaT;
            double a = (vPrime - v) / deltaT;
            dVst.add(t, d);
            vVsT.add(t, v);
            aVsT.add(t, a);
            System.out.println("t=" + t + "  d=" + d + "  v=" + v + " a=" + a);
        }
        dVst.show();
        vVsT.show();
        aVsT.show();
    }

}
