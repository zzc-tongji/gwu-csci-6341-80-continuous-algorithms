public class Exercise42 {

    public static void main(String[] argv) {
        double[][] data = new double[5][2];
        data[0][0] = 0.5;
        data[0][1] = 4.97;
        data[1][0] = 1.5;
        data[1][1] = 4.77;
        data[2][0] = 2.5;
        data[2][1] = 4.33;
        data[3][0] = 3.5;
        data[3][1] = 3.57;
        data[4][0] = 4.5;
        data[4][1] = 2.18;
        for (int i = 0; i < data.length; i++) {
            System.out.println("x = " + data[i][0] + ", f(x) = " + data[i][1] + ", x^2 + (f(x))^2 = " + data[i][0] * data[i][0] + data[i][1] * data[i][1]);
        }
    }
}
