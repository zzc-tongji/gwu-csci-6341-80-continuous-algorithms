import java.util.*;
import java.awt.geom.*;
import java.awt.*;

public class ZhichengDubinCarController implements CarController {

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
        if (time >= 0 && time < 0.3) {
            mu1 = 0;
            mu2 = 10;
        } else if (time >= 0.3 && time < 3.3) {
            mu1 = 10;
            mu2 = 10;
        } else if (time >= 3.3 && time < 3.6) {
            mu1 = 10;
            mu2 = 0;
        } else if (time >= 3.6 && time < 6.6) {
            mu1 = 10;
            mu2 = 10;
        } else if (time >= 6.6 && time < 6.9) {
            mu1 = 10;
            mu2 = 0;
        } else if (time >= 6.9 && time < 9.9) {
            mu1 = 10;
            mu2 = 10;
        } else if (time >= 9.9 && time < 10.2) {
            mu1 = 0;
            mu2 = 10;
        } else if (time >= 10.2 && time < 11.3) {
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
