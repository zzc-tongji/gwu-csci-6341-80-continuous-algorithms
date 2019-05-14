public class CosMaximum {

    public static void main(String[] argv) {
        // Initial guess: max occurs at 0, and has value 0.
        double bestX = 0;
        double max = Math.sin(bestX);

        // Now try values in the range [0,2*pi]
        for (double x = 0; x <= 2 * Math.PI; x += 0.0001) {
            double f = Math.cos(x);
            if (f > max) {
                max = f;
                bestX = x;
            }
        }

        // Print result.
        System.out.println("Max value " + max + " occurred at x=" + bestX);
    }

}
