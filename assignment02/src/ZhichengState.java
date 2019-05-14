class ZhichengState {

    double costFromStart;
    double estimatedCostToGoal;
    ZhichengState parent;
    int xGrid;
    int yGrid;

    ZhichengState(int xGrid, int yGrid) {
        costFromStart = 0;
        estimatedCostToGoal = 0;
        this.parent = null;
        this.xGrid = xGrid;
        this.yGrid = yGrid;
    }

    ZhichengState getParent() {
        return parent;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ZhichengState)) {
            return false;
        }
        ZhichengState m = (ZhichengState) obj;
        return (m.xGrid == xGrid) && (m.yGrid == yGrid);
    }

    public String toString() {
        return "ZhichengState: [xGrid=" + xGrid + ", yGrid=" + yGrid + ", cost=" + costFromStart + " est=" + estimatedCostToGoal + "]";
    }
}
