public class MovingObjectBasic {

    public static void main(String[] argv) {
        Function V = new Function("Velocity");
        Function D = new Function("Distance");

        double a = 4.9, t = 0, d = 0, v = 0, delT = 0.1;

        while (t <= 3) {
            // INSERT YOUR CODE HERE for updating t, v, and d.
            // Can you write this without looking at the other code or the module?
            t += delT;
            v = v + a * delT;
            V.add(t, v);
            d = d + v * delT;
            D.add(t, d);
            System.out.printf("t=%4.2f a=%4.2f v=%4.2f d=%4.2f\n", t, a, v, d);
        }

        Function.show(V, D);
    }

}
