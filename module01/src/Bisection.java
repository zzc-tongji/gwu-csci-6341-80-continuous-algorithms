// Bisection.java
//
// Rahul Simha
// Spring 2008

import java.util.ArrayList;
import java.util.List;

public class Bisection {

    public static void main(String[] argv) {
        System.out.println("bisection():        sqrt(3)=" + bisection(0, 2));
        System.out.println("bisectionPrecise(): sqrt(3)=" + bisectionPrecise(0, 2));
        System.out.println("bisectionPrecise(): sqrt(3)=" + bisectionRecursive(0, 2));
        System.out.println("Google Calculator:  sqrt(3)=1.73205080757");
    }

    static double f(double x) {
        return x * x - 3;
    }

    static double bisection(double a, double b) {
        // Compute mid-point and function value at mid-point.
        double m = (a + b) / 2.0;
        // As long as f(m) <> 0, repeat.
        while (Math.abs(f(m)) > 0.01) {
            if (f(a) * f(m) > 0) {
                a = m;      // Same side as fa => move "a" closer to m.
            } else {
                b = m;      // Other side => move "b" closer to m.
            }
            m = (a + b) / 2.0;
        }
        return m;
    }

    static double bisectionPrecise(double a, double b) {
        // Compute mid-point and function value at mid-point.
        double m = (a + b) / 2.0;
        // As long as f(m) <> 0, repeat.
        while (Math.abs(f(m)) > 0.0001) {
            if (f(a) * f(m) > 0) {
                a = m;      // Same side as fa => move "a" closer to m.
            } else {
                b = m;      // Other side => move "b" closer to m.
            }
            m = (a + b) / 2.0;
        }
        return m;
    }

    static double bisectionRecursive(double a, double b) {
        // Compute mid-point and function value at mid-point.
        double m = (a + b) / 2.0;
        // As long as f(m) == 0, return directly.
        if (Math.abs(f(m)) < 0.01) {
            return m;
        }
        // Recurs.
        if (f(a) * f(m) > 0) {
            return bisectionRecursive(m, b);
        } else {
            return bisectionRecursive(a, m);
        }
    }

}
