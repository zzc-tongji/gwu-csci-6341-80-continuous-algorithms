import java.util.ArrayList;

class ZhichengProblem {

    ZhichengState start;
    ZhichengState end;
    ArrayList<ZhichengObstacle> obstacleList;

    ZhichengProblem(ZhichengState start, ZhichengState end, ArrayList<ZhichengObstacle> obstacleList) {
        this.start = start;
        this.end = end;
        this.obstacleList = obstacleList;
    }

    ArrayList<ZhichengState> getNeighbors(ZhichengState state) {
        ArrayList<ZhichengState> neighborList = new ArrayList<>();
        ZhichengState neighbor;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != j && i != -j) {
                    // 4 neighbors: up, down, left, right
                    neighbor = new ZhichengState(state.xGrid + i, state.yGrid + j);
                    if (isValid(neighbor)) {
                        neighbor.parent = state;
                        neighbor.costFromStart = state.costFromStart + 1;
                        neighbor.estimatedCostToGoal = Math.abs(neighbor.xGrid - end.xGrid) + Math.abs(neighbor.yGrid - end.yGrid);
                        neighborList.add(neighbor);
                    }
                }
            }
        }
        return neighborList;
    }

    boolean satisfiesGoal(ZhichengState state) {
        return end.equals(state);
    }

    boolean isValid(ZhichengState neighbor) {
        if (neighbor.xGrid < 0) {
            return false;
        }
        if (neighbor.yGrid < 0) {
            return false;
        }
        for (ZhichengObstacle obstacle : obstacleList) {
            if (gridIn(neighbor.xGrid, neighbor.yGrid, obstacle.xGridRange, obstacle.yGridRange)) {
                return false;
            }
        }
        return true;
    }

    boolean gridIn(int x, int y, ArrayList<Integer> xRange, ArrayList<Integer> yRange) {
        return x >= xRange.get(0) && x <= xRange.get(1) && y >= yRange.get(0) && y <= yRange.get(1);
    }

}
