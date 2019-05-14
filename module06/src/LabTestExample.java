public class LabTestExample {

    public static void main(String[] argv) {
        double numTrials = 1000000;
        double positive = 0;
        double infected = 0;
        double well = 0;
        LabTest lab = new LabTest(0.05, 0.99, 0.03);
        for (int n = 0; n < numTrials; n++) {
            lab.nextPatient();
            if (lab.testedPositive()) {
                // INSERT YOUR CODE HERE
                positive++;
                if (lab.isSick()) {
                    infected++;
                } else {
                    well++;
                }
            }
        }
        // AND HERE
        double infectedGivenPositive = infected / positive;
        double wellGivenPositive = well / positive;
        System.out.println("Pr[infected given positive]=" + infectedGivenPositive + "  theory=" + 0.635);
        System.out.println("Pr[well given positive]=" + wellGivenPositive + "  theory=" + 0.365);
    }

}
