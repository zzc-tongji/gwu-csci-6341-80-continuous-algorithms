// GCD.java
//
// Rahul Simha
// Spring 2008
// Ref: Dasgupta algorithms book

public class GCD {

    public static void main(String[] argv) {
        System.out.println("gcd(30,24)=" + gcd(30, 24)); // 6.
        System.out.println("gcd(7276500,3185325)=" + gcd(7276500, 3185325)); // 7425.
    }


    static int gcd(int m, int n) {
        // Bad input check:
        if (n > m) {
            return gcd(n, m);
        }

        // Algorithm:
        if (n == 0) {
            return m;
        } else return gcd(n, m % n);
    }

}


// The second example is from the Neapolitan/Naimipour book.
