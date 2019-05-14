import java.awt.geom.*;


public class PointGeneratorExample4 {

    public static void main(String[] argv) {
        int numPoints = 100000;

        // Histogram of Y all by itself.
        DensityHistogram yHist = new DensityHistogram(0, 10, 20);
        for (int n = 0; n < numPoints; n++) {
            Point2D.Double p = PointGenerator.randomPoint();
            yHist.add(p.y);
        }
        yHist.display();


        // Histogram of Y given X in [3,4].
        DensityHistogram yHist2 = new DensityHistogram(0, 10, 20);
        for (int n = 0; n < numPoints; n++) {
            Point2D.Double p = PointGenerator.randomPoint();
            if ((3 <= p.x) && (p.x <= 4)) {
                yHist2.add(p.y);
            }
        }
        yHist2.display();
    }

}
