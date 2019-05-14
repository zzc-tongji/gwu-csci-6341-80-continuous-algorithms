import java.util.*;

public class UniformVarianceExample {

    public static void main(String[] argv) {
        double n = 100;

        // First estimate the mean.
        double total = 0;
        ArrayList<Double> data = new ArrayList<Double>();
        for (int i = 0; i < n; i++) {
            double x = RandTool.uniform();
            total += x;
            data.add(x);                       // Store for later use in variance estimate.
        }
        double mu_p = total / n;
        System.out.println("Mean estimate: " + mu_p);

        // Now the variance.
        double varTotal = 0;
        for (double x : data) {
            varTotal += (x - mu_p) * (x - mu_p);
        }
        double sigma_p = Math.sqrt((1.0 / (n - 1) * varTotal));
        System.out.println("Std-dev estimate: " + sigma_p);

        // One can now use mu_p and sigma_p in Normal-distribution formulas.
    }

}
