import java.util.*;
import java.awt.geom.*;
import java.awt.*;

public class ZhichengOptimalDubinCarController implements CarController {

    double mu1 = 0;
    double mu2 = 1;
    ArrayList<Rectangle2D.Double> obstacles;
    SensorPack sensors;

    public void init(double initX, double initY, double initTheta, double endX, double endY, double endTheta, ArrayList<Rectangle2D.Double> obstacles, SensorPack sensors) {
        this.obstacles = obstacles;
        this.sensors = sensors;
    }

    public double getControl(int i) {
        if (i == 1) {
            return mu1;
        } else if (i == 2) {
            return mu2;
        }
        return 0;
    }

    public void move() {
        double time = sensors.getTime();
        if (time >= 0 && time < 0.1) {
            mu1 = -10;
            mu2 = 10;
        } else if (time >= 0.1 && time < 0.2) {
            mu1 = -8;
            mu2 = 8;
        } else if (time >= 0.2 && time < 7.5) {
            mu1 = 10;
            mu2 = 9;
        } else if (time >= 7.5 && time < 7.6) {
            mu1 = -10;
            mu2 = 10;
        } else if (time >= 7.6 && time < 7.7) {
            mu1 = -8.50000000000028;
            mu2 = 8.50000000000028;
        } else if (time >= 7.7 && time < 10.6) {
            mu1 = 10;
            mu2 = 10;
        } else {
            mu1 = 0;
            mu2 = 0;
        }
    }

    public void draw(Graphics2D g2, Dimension D) {
    }

}
