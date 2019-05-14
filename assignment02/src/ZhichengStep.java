public class ZhichengStep {

    private double c1;
    private double c2;
    private double time;
    private double globalTime;

    ZhichengStep(double c1, double c2, double time, double globalTime) {
        this.c1 = c1;
        this.c2 = c2;
        this.time = time;
        this.globalTime = globalTime;
    }

    public String toString() {
        return "{ c1: " + this.c1 + ", c2: " + this.c2 + ", time: " + this.time + ", globalTime: " + this.globalTime + " }\n";
    }
}
