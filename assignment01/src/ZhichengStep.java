public class ZhichengStep {

    private double left;
    private double right;
    private int time; // unit: ms
    private int globalTime; // unit: ms

    public ZhichengStep(double left, double right, int time, int globalTime) {
        this.left = left;
        this.right = right;
        this.time = time;
        this.globalTime = globalTime;
    }

    @Override
    public String toString() {
        return "{ left: " + this.left + ", right: " + this.right + ", time: " + this.time + ", globalTime: " + this.globalTime + " }\n";
    }
}
