public class TwoSegmentIncline {

    public static void main(String[] argv) {
        // Make an instance of the simulator.
        InclineSegmentSimulator sim = new InclineSegmentSimulator();

        // Run the straight incline as one segment.
        double t = sim.run(0, 10, 10, 0, 0, 0);
        System.out.println("Straight solution: " + t);

        // Now, a potential two segment solution. First segment:
        double t1 = sim.run(0, 10, 5, 5, 0, 0);

        // INSERT YOUR CODE HERE.

        double t2 = sim.run(5, 5, 10, 0, sim.vx, sim.vy);

        // Comparison:
        System.out.println("Twisted solution: " + (t1 + t2));
    }

}
