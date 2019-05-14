public class Exercise47 {

    public static void main(String[] argv) {
        for (double i = 0; i < Math.PI; i += 0.1) {
            System.out.println("x = " + String.format("%.1f", i) + ", Math.sin(x) = " + String.format("%.7f", Math.sin(i)) + ", sin(x) = " + String.format("%.7f", sin(i)) + ", sinQuick(x) = " + String.format("%.7f", sinQuick(i, 4)));
        }
    }

    static double sinQuick(double x, int accuracy) {
        double res = 0;
        double sign = 1;
        double numerator = x;
        double temp;
        double denominator = 1;
        for (int i = 0; i < accuracy; i++) {
            res = res + sign * numerator / denominator;
            sign = -sign;
            numerator = numerator * x * x;
            temp = (i + 1) << 1;
            denominator = denominator * temp * (temp + 1);
        }
        return res;
    }

    static double sin(double x) {
        double s = x - (x * x * x) / (1 * 2 * 3) + (x * x * x * x * x) / (1 * 2 * 3 * 4 * 5) - (x * x * x * x * x * x * x) / (1 * 2 * 3 * 4 * 5 * 6 * 7);
        return s;
    }
}
