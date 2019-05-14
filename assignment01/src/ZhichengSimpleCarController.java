import java.util.*;
import java.awt.geom.*;
import java.awt.*;

public class ZhichengSimpleCarController implements CarController {

    double vel;
    double phi;
    ArrayList<Rectangle2D.Double> obstacles;
    SensorPack sensors;

    public void init(double initX, double initY, double initTheta, double endX, double endY, double endTheta, ArrayList<Rectangle2D.Double> obstacles, SensorPack sensors) {
        this.obstacles = obstacles;
        this.sensors = sensors;
    }

    public double getControl(int i) {
        if (i == 1) {
            return vel;
        } else if (i == 2) {
            return phi;
        }
        return 0;
    }

    public void move() {
        double time = sensors.getTime();
        if (time >= 0 && time < 0.6) {
            vel = 10;
            phi = 1;
        } else if (time >= 0.6 && time < 8.6) {
            vel = 10;
            phi = 0;
        } else if (time >= 8.6 && time < 9.2) {
            vel = 10;
            phi = -1;
        } else if (time >= 9.2 && time < 27.2) {
            vel = 10;
            phi = 0;
        } else if (time >= 27.2 && time < 27.8) {
            vel = 10;
            phi = -1;
        } else if (time >= 27.8 && time < 35.8) {
            vel = 10;
            phi = 0;
        } else if (time >= 35.8 && time < 36.4) {
            vel = 10;
            phi = 1;
        } else if (time >= 36.4 && time < 52) {
            vel = 10;
            phi = 0;
        } else {
            vel = 0;
            phi = 0;
        }
    }

    public void draw(Graphics2D g2, Dimension D) {
    }

}
