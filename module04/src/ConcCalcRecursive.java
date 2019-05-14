import java.text.DecimalFormat;

public class ConcCalcRecursive {

    static double A = 3.0;
    static double B = 2.0;
    static double C = 1.5;
    static double K_ab = 1.0;
    static double K_c = 0.5;
    static double s = 0.01;
    static DecimalFormat df = new DecimalFormat("###.###");

    public static String calc(double t) {
        if (t <= 0) {
            return "At time t=" + df.format(t) + ":   A(t)=" + df.format(A) + "   B(t)=" + df.format(B) + "   C(t)=" + df.format(C) + "\n";
        }
        String str = calc(t - s);
        A = A + s * (K_c * C - K_ab * A * B);
        B = B + s * (K_c * C - K_ab * A * B);
        C = C + s * (K_ab * A * B - K_c * C);
        return str + "At time t=" + df.format(t) + ":   A(t)=" + df.format(A) + "   B(t)=" + df.format(B) + "   C(t)=" + df.format(C) + "\n";
    }

    public static void main(String[] argv) {
        System.out.print(calc(1));
    }

}
