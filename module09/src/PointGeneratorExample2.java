import java.awt.geom.*;


public class PointGeneratorExample2 {

    public static void main(String[] argv) {
        int numPoints = 100000;
        DensityHistogram xHist = new DensityHistogram(0, 5, 20);
        DensityHistogram yHist = new DensityHistogram(0, 10, 20);
        for (int n = 0; n < numPoints; n++) {
            Point2D.Double p = PointGenerator.randomPoint();
            // INSERT YOUR CODE HERE
            xHist.add(p.getX());
            yHist.add(p.getY());
        }
        xHist.display();
        yHist.display();
    }

}
