import java.awt.geom.*;


public class PointGeneratorExample3 {

    public static void main(String[] argv) {
        double numPoints = 100000;
        double numSuccesses = 0;
        double x34 = 0;
        double x34y57 = 0;
        for (int n = 0; n < numPoints; n++) {
            Point2D.Double p = PointGenerator.randomPoint();
            if ((5 <= p.y) && (p.y <= 7)) {
                numSuccesses++;
                if ((3 <= p.x) && (p.x <= 4)) {
                    x34y57 += 1;
                }
            }
            if ((3 <= p.x) && (p.x <= 4)) {
                x34 += 1;
            }
        }
        System.out.println("Pr[Y in [5,7]]              = " + (numSuccesses / numPoints));
        System.out.println("Pr[Y in [5,7] | X in [3,4]] = " + (x34y57 / x34));
    }

}
