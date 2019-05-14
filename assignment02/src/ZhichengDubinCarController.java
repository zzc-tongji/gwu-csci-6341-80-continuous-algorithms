import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedList;

public class ZhichengDubinCarController implements CarController {

    public static void main(String[] argv) {
        // Scene 3
        double initX = 50;
        double initY = 50;
        double endX = 500;
        double endY = 50;
        ArrayList<Rectangle2D.Double> obs = new ArrayList<>();
        obs.add(new Rectangle2D.Double(150, 100, 100, 100));
        // calculate
        ZhichengDubinCarController controller = new ZhichengDubinCarController();
        controller.init(initX, initY, 0, endX, endY, 0, obs, null);
        // output
        System.out.print("Path: ");
        System.out.print("(" + initX + ", " + initY + ") -> ");
        for (ZhichengState state : controller.solution) {
            System.out.print("(" + controller.getGridCenterByCoordinate(state.xGrid) + ", " + controller.getGridCenterByCoordinate(state.yGrid) + ") -> ");
        }
        System.out.println("(" + endX + ", " + endY + ")");
    }

    double mu1 = 0;
    double mu2 = 0;
    ArrayList<Rectangle2D.Double> obstacles;
    SensorPack sensors;

    double gridSize = 100;
    double errorRadius = 5;
    double initX;
    double initY;
    double initTheta;
    double endX;
    double endY;
    double endTheta;
    DubinCarSimulator simulator;
    ArrayList<ZhichengObstacle> obstacleList;
    ZhichengProblem problem;
    ZhichengCBPlannerAStar planner;
    LinkedList<ZhichengState> solution;
    ArrayList<ZhichengStep> instruction;

    public void init(double initX, double initY, double initTheta, double endX, double endY, double endTheta, ArrayList<Rectangle2D.Double> obstacles, SensorPack sensors) {
        this.obstacles = obstacles;
        this.sensors = sensors;

        // get necessary value
        this.initX = initX;
        this.initY = initY;
        this.initTheta = initTheta;
        this.endX = endX;
        this.endY = endY;
        this.endTheta = endTheta;
        // init simulator
        simulator = new DubinCarSimulator(false);
        simulator.init(initX, initY, initTheta, obstacles);
        // init obstacle list
        obstacleList = new ArrayList<>();
        for (Rectangle2D.Double obstacle : obstacles) {
            obstacleList.add(new ZhichengObstacle(obstacle.x, obstacle.x + obstacle.width, obstacle.y - obstacle.height, obstacle.y, gridSize));
        }
        // init problem
        problem = new ZhichengProblem(
                new ZhichengState(coordinateToGrid(initX), coordinateToGrid(initY)),
                new ZhichengState(coordinateToGrid(endX), coordinateToGrid(endY)),
                obstacleList
        );
        // init planner and solve problem
        planner = new ZhichengCBPlannerAStar();
        solution = this.planner.makePlan(this.problem);
        // convert solution to instruction
        //
        // TODO: there are some bugs may cause infinite loop.
        //
        // instruction = new ArrayList<>();
        // solutionToInstruction();
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
        // TODO: use `instruction` to take control.
    }

    public void draw(Graphics2D g2, Dimension D) {
    }

    void solutionToInstruction() {
        // step 1: move from the origin to the center of the first grid
        originToGridCenter();
        // step 2: move between grids
        for (ZhichengState state : solution) {
            if (state.parent != null) {
                moveBetweenGrid(state.parent, state);
            }
        }
        // step 3: move from the center of the last grid to the destination
        GridCenterToDestination();
    }

    void originToGridCenter() {
        double centerX = getGridCenterByCoordinate(initX);
        double centerY = getGridCenterByCoordinate(initY);
        double theta = Math.atan2(centerY - initY, centerX - initX);
        if (theta < 0) {
            theta += 2 * Math.PI;
        }
        turnTo(theta);
        while (!coordinateEqual(simulator.getX(), simulator.getY(), centerX, centerY)) {
            // forward
            simulator.nextStep(10, 10, 0.1);
            instruction.add(new ZhichengStep(10, 10, 0.1, simulator.getTime()));
        }
    }

    void moveBetweenGrid(ZhichengState from, ZhichengState to) {
        double theta;
        int direction;
        if (from.xGrid != to.xGrid) { // from.yGrid == to.yGrid
            if (from.xGrid < to.xGrid) {
                // right
                theta = 0;
                direction = 2;
            } else { // from.xGrid > to.xGrid
                // left
                theta = Math.PI;
                direction = 1;
            }

        } else { // from.xGrid == to.xGrid, from.yGrid != to.yGrid
            if (from.yGrid < to.yGrid) {
                // up
                theta = Math.PI / 2;
                direction = 3;
            } else { // from.yGrid > to.yGrid
                // down
                theta = 3 * Math.PI / 2;
                direction = 4;
            }
        }
        turnTo(theta);
        moveForwardTo(gridToCoordinate(to.xGrid), gridToCoordinate(to.yGrid), direction);
    }

    void GridCenterToDestination() {
        double theta = Math.atan2(endY - simulator.getY(), endX - simulator.getX());
        if (theta < 0) {
            theta += 2 * Math.PI;
        }
        turnTo(theta);
        while (!coordinateEqual(simulator.getX(), simulator.getY(), endX, endY)) {
            // forward
            simulator.nextStep(10, 10, 0.1);
            instruction.add(new ZhichengStep(10, 10, 0.1, simulator.getTime()));
        }
    }

    void turnTo(double theta) {
        if (angleCompare(simulator.getTheta(), theta) == -1) {
            while (angleCompare(simulator.getTheta(), theta) == -1) {
                // turn left
                simulator.nextStep(-1, 1, 0.1);
                instruction.add(new ZhichengStep(-1, 1, 0.1, simulator.getTime()));
            }
        }
        if (angleCompare(simulator.getTheta(), theta) == 1) {
            while (angleCompare(simulator.getTheta(), theta) == 1) {
                // turn right
                simulator.nextStep(1, -1, 0.1);
                instruction.add(new ZhichengStep(1, -1, 0.1, simulator.getTime()));
            }
        }

    }

    void moveForwardTo(double x, double y, int direction) {
        // direction: 1 - left, 2 - right, 3 - up, 4 - down
        while (!coordinateEqual(simulator.getX(), simulator.getY(), x, y)) {
            switch (direction) {
                case 1: // left
                    if (simulator.getY() < y - errorRadius / 2) {
                        // turn right
                        simulator.nextStep(10, 9, 0.1);
                        instruction.add(new ZhichengStep(10, 9, 0.1, simulator.getTime()));
                    } else if (simulator.getY() > y + errorRadius / 2) {
                        // turn left
                        simulator.nextStep(9, 10, 0.1);
                        instruction.add(new ZhichengStep(9, 10, 0.1, simulator.getTime()));
                    } else {
                        // forward
                        simulator.nextStep(10, 10, 0.1);
                        instruction.add(new ZhichengStep(10, 10, 0.1, simulator.getTime()));
                    }
                    break;
                case 2: // right
                    if (simulator.getY() < y - errorRadius / 2) {
                        // turn left
                        simulator.nextStep(9, 10, 0.1);
                        instruction.add(new ZhichengStep(9, 10, 0.1, simulator.getTime()));
                    } else if (simulator.getY() > y + errorRadius / 2) {
                        // turn right
                        simulator.nextStep(10, 9, 0.1);
                        instruction.add(new ZhichengStep(10, 9, 0.1, simulator.getTime()));
                    } else {
                        // forward
                        simulator.nextStep(10, 10, 0.1);
                        instruction.add(new ZhichengStep(10, 10, 0.1, simulator.getTime()));
                    }
                    break;
                case 3: // up
                    if (simulator.getX() < x - errorRadius / 2) {
                        // turn right
                        simulator.nextStep(10, 9, 0.1);
                        instruction.add(new ZhichengStep(10, 9, 0.1, simulator.getTime()));
                    } else if (simulator.getX() > x + errorRadius / 2) {
                        // turn left
                        simulator.nextStep(9, 10, 0.1);
                        instruction.add(new ZhichengStep(9, 10, 0.1, simulator.getTime()));
                    } else {
                        // forward
                        simulator.nextStep(10, 10, 0.1);
                        instruction.add(new ZhichengStep(10, 10, 0.1, simulator.getTime()));
                    }
                    break;
                default: // down
                    if (simulator.getX() < x - errorRadius / 2) {
                        // turn left
                        simulator.nextStep(9, 10, 0.1);
                        instruction.add(new ZhichengStep(9, 10, 0.1, simulator.getTime()));
                    } else if (simulator.getX() > x + errorRadius / 2) {
                        // turn right
                        simulator.nextStep(10, 9, 0.1);
                        instruction.add(new ZhichengStep(10, 9, 0.1, simulator.getTime()));
                    } else {
                        // forward
                        simulator.nextStep(10, 10, 0.1);
                        instruction.add(new ZhichengStep(10, 10, 0.1, simulator.getTime()));
                    }
                    break;
            }
        }
    }

    boolean coordinateEqual(double x1, double y1, double x2, double y2) {
        return Math.abs(x1 - x2) <= errorRadius && Math.abs(y1 - y2) <= errorRadius;
    }

    int angleCompare(double angle1, double angle2) {
        double magic = 0.05;
        if ((angle1 >= 2 * Math.PI - magic && angle1 < 2 * Math.PI) && (angle2 >= 0 && angle2 < magic)) {
            return -1;
        } else if ((angle1 >= 0 && angle1 < magic) && (angle2 >= 2 * Math.PI - magic && angle2 < 2 * Math.PI)) {
            return 1;
        } else if (angle1 < angle2) {
            return -1;
        } else if (angle1 > angle2) {
            return 1;
        }
        return 0;
    }

    double getGridCenterByCoordinate(int grid) {
        return gridSize * grid + Math.floor(gridSize / 2);
    }

    double getGridCenterByCoordinate(double coordinate) {
        return gridSize * Math.floor(coordinate / gridSize) + Math.floor(gridSize / 2);
    }

    double gridToCoordinate(int grid) {
        return gridSize * grid + Math.floor(gridSize / 2);
    }

    int coordinateToGrid(double coordinate) {
        return (int) Math.floor(coordinate / gridSize);
    }
}
