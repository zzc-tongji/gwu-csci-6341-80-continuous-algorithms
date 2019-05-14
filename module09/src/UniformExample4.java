public class UniformExample4 {

    public static void main(String[] argv) {
        int numTrials = 100000;
        double total = 0;
        for (int n = 0; n < numTrials; n++) {
            // INSERT YOUR CODE HERE
            System.out.println(UniformRandom.uniform());
        }
        System.out.println("Avg value of g(X): " + total / numTrials);
    }

}
