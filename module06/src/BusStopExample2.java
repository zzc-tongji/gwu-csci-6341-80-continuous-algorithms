public class BusStopExample2 {

    public static void main(String[] argv) {
        double myArrivalTime = 10;
        BusStop busStop = new BusStop(true);
        double arrivalTime = 0;
        double numBuses = -1;
        while (arrivalTime < myArrivalTime) {
            numBuses++;
            busStop.nextBus();
            arrivalTime = busStop.getArrivalTime();
        }
        System.out.println("Number of buses: " + numBuses);
    }

}
