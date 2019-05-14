public class BusStopExample3 {

    public static void main(String[] argv) {
        double myArrivalTime = 2;
        BusStop busStop;
        double arrivalTime;
        double numBuses;
        double hit = 0;
        int trial = 100000;
        for (int i = 0; i < trial; i++) {
            busStop = new BusStop(true);
            arrivalTime = 0;
            numBuses = -1;
            while (arrivalTime < myArrivalTime) {
                numBuses++;
                busStop.nextBus();
                arrivalTime = busStop.getArrivalTime();
            }
            if (numBuses == 3) {
                hit++;
            }
        }
        System.out.println("The probability that exactly three buses arrive during the interval [0,2] is " + hit / trial);
    }

}
