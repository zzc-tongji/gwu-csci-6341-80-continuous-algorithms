public class TwoSegmentIncline2 {

    public static void main(String[] argv) {
        // Make an instance of the simulator.
        InclineSegmentSimulator sim = new InclineSegmentSimulator();

        // Run the straight incline as one segment.
        double t = sim.run(0, 10, 10, 0, 0, 0);
        System.out.println("Straight solution: " + t);

        double x2 = 5;
        for (double y2 = 1; y2 <= 9; y2 += 1) {
            // Now, a potential two segment solution. First segment:
            double t1 = sim.run(0, 10, x2, y2, 0, 0);

            // FILL IN YOUR CODE HERE
            double t2 = sim.run(x2, y2, 10, 0, sim.vx, sim.vy);

            // Comparison:
            System.out.println("Twisted solution for y2=" + y2 + ": " + (t1 + t2));
        }

    }


    static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

}
