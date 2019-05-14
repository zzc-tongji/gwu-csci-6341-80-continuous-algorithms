public class DataAnalysis {

    public static void main(String[] argv) {
        // Make a Function object.
        Function F = new Function("mystery");

        // Put the data in.
        F.add(1, 1);
        F.add(2, 13);
        F.add(3, 33);
        F.add(4, 61);
        F.add(5, 97);

        // Display it.
        F.show();
    }

}
