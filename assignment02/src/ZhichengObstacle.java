import java.util.ArrayList;

public class ZhichengObstacle {
    ArrayList<Double> xRange;
    ArrayList<Double> yRange;
    ArrayList<Integer> xGridRange;
    ArrayList<Integer> yGridRange;

    ZhichengObstacle(double xBegin, double xEnd, double yBegin, double yEnd, double gridSize) {
        double magic = 0.001;
        xRange = new ArrayList<>();
        xRange.add(xBegin);
        xRange.add(xEnd);
        yRange = new ArrayList<>();
        yRange.add(yBegin);
        yRange.add(yEnd);
        xGridRange = new ArrayList<>();
        xGridRange.add((int) Math.floor((xBegin) / gridSize));
        xGridRange.add((int) Math.floor((xEnd - magic) / gridSize));
        yGridRange = new ArrayList<>();
        yGridRange.add((int) Math.floor((yBegin) / gridSize));
        yGridRange.add((int) Math.floor((yEnd - magic) / gridSize));
    }
}
