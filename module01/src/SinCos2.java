public class SinCos2 {

    public static void main(String[] argv) {
        // Create two Function objects.
        Function sinFunc = new Function("sin");
        Function cosFunc = new Function("cos");

        // Put values into them.
        for (double x = 0; x <= 8 * Math.PI; x += 0.1) {
            sinFunc.add(x, Math.sin(x));
            cosFunc.add(x, Math.cos(x));
        }

        // Display.
        Function.show(sinFunc, cosFunc);
    }

}
