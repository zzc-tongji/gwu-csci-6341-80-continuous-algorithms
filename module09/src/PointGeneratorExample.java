import java.awt.geom.*;


public class PointGeneratorExample {

    public static void main(String[] argv) {
        // Generate some points and display.
        int numPoints = 1000;
        PointDisplay points = new PointDisplay();
        for (int n = 0; n < numPoints; n++) {
            Point2D.Double p = PointGenerator.randomPoint();
            points.add(p);
        }
        points.display();
    }

}
