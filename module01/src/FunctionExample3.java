public class FunctionExample3 {

    public static void main(String[] argv) {
        Function f = new Function("f(x) = 1 / (x - 2)");
        for (double x = 0; x <= 5; x += 0.001) {
            if (x > 1.9 && x < 2.1) {
                continue;
            }
            f.add(x, 1 / (x - 2));
        }
        f.show();
    }

}
