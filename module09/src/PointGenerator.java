import java.awt.geom.*;


public class PointGenerator {

    public static Point2D.Double randomPoint() {
        double x = RandTool.uniform(1.0, 5.0);
        double y = RandTool.gaussian(5, 2);
        return new Point2D.Double(x, y);
    }

    public static Point2D.Double randomPoint2() {
        double x = RandTool.uniform(1.0, 5.0);
        double y = RandTool.gaussian(5, x);
        return new Point2D.Double(x, y);
    }

}
