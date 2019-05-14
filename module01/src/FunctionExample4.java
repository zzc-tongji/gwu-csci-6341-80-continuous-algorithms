public class FunctionExample4 {

    public static void main(String[] argv) {
        Function f1 = new Function("f1(x) = 3x + 5");
        Function f2 = new Function("f2(x) = x^2 - 2");
        Function f3 = new Function("f3(x) = 5 / x^2");
        Function f4 = new Function("f4(x) = e^(-2x)");
        for (double x = 0; x <= 10; x += 0.01) {
            if (x < 0.5) {
                continue;
            }
            f1.add(x, 3 * x + 5);
            f2.add(x, x * x - 2);
            f3.add(x, 5 / (x * x));
            f4.add(x, Math.pow(Math.E, (-2 * x)));
        }
        Function.show(f1, f2, f3, f4);
    }

}
