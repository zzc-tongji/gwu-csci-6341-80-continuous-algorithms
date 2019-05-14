public class EstimationExample {

    public static void main(String[] argv) {
        double numTrials = 100000;
        double total = 0;
        for (int n = 0; n < numTrials; n++) {
            // Perform the experiment and observe the value.
            double x = Experiment.getValue();
            // Record the total so far.
            total = total + x;
        }
        // Compute average at the end.
        double avg = total / numTrials;
        System.out.println("Mean: " + avg);
    }

}
