// BusStop.java
//
// Author: Rahul Simha
// Feb, 2008
//
// Simulate interarrivals at a bus stop.


public class BusStop {

    static boolean isExp = true;

    double lambda = 1;

    double time = 0;
    double interarrival;

    public BusStop(boolean isExp) {
        this.isExp = isExp;
    }

    public void nextBus() {
        if (isExp) {
            interarrival = RandTool.exponential(lambda);
        } else {
            interarrival = RandTool.uniform(0.0, 2.0);
        }
        time += interarrival;
    }


    public double getArrivalTime() {
        return time;
    }

    public double getInterarrivalTime() {
        return interarrival;
    }

}
