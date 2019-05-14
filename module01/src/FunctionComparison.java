public class FunctionComparison {

    public static void main(String[] argv) {
        // Make two objects, one for each function.
        Function F = new Function("f(x) = 3x + 5");
        Function G = new Function("g(x) = 3x + 10");

        // Generate 100 values in the range [0,10]
        for (double x = 0; x <= 10; x += 0.2) {
            F.add(x, 3 * x + 5);
            G.add(x, 4 * x + 10);
        }

        // Display both together.
        Function.show(F, G);
    }

}
