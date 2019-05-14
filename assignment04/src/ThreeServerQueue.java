import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class ThreeServerQueue {

    public static void main(String[] args) {
        ThreeServerQueue queue = new ThreeServerQueue(2, 0.75, 1);
        if (queue.simulate(1000)) {
            System.out.println(queue.toString());
        }
    }

    // ============================================================

    // parameter
    private int serverNumber;    // K
    private double arrivalRate;  // λ
    private double serviceRate;  // μ
    private int maxClientNumber;

    // runtime
    private LinkedList<Client> clientInList;
    private PriorityQueue<Client> clientOutList;
    private PriorityQueue<Server> serverList;
    private LinkedList<Server> serverInList;
    private LinkedList<Server> serverOutList;
    private double clock;
    private boolean isDone;

    // statistics
    private double arrivalTime, arrivalTimePerClient, arrivalRatePrime;
    private double waitTime, waitTimePerClient;
    private double serviceTime, serviceTimePerClient, serviceRatePrime;
    private double systemTime, systemTimePerClient;
    private double freeTime, freeTimePerServer;
    private double freeCounter, freeCounterPerServer;

    private ThreeServerQueue(int serverNumber, double arrivalRate, double serviceRate) {
        this.serverNumber = serverNumber;
        this.arrivalRate = arrivalRate;
        this.serviceRate = serviceRate;
        maxClientNumber = 0;
        clientInList = new LinkedList<>();
        clientOutList = new PriorityQueue<>();
        serverList = new PriorityQueue<>();
        serverInList = new LinkedList<>();
        for (int i = 0; i < this.serverNumber; i++) {
            serverInList.add(new Server(i));
        }
        serverOutList = new LinkedList<>();
        clock = 0;
        arrivalTime = arrivalTimePerClient = arrivalRatePrime = 0;
        waitTime = waitTimePerClient = 0;
        serviceTime = serviceTimePerClient = serviceRatePrime = 0;
        freeTime = freeTimePerServer = 0;
        freeCounter = freeCounterPerServer = 0;
        isDone = false;
    }

    public String toString() {
        String result = "Parameter:";
        result += "\n  server number (K): " + serverNumber;
        result += "\n  arrival rate  (λ): " + arrivalRate;
        result += "\n  service rate  (μ): " + serviceRate;
        result += "\n  max client number: " + maxClientNumber;
        result += "\nResults:";
        result += "\n  total time:     " + clock;
        result += "\n  arrival time:   " + arrivalTime;
        result += "\n    - per client: " + arrivalTimePerClient;
        result += "\n    - rate        " + arrivalRatePrime;
        result += "\n  wait time:      " + waitTime;
        result += "\n    - per client: " + waitTimePerClient;
        result += "\n  service time:   " + serviceTime;
        result += "\n    - per client: " + serviceTimePerClient;
        result += "\n    - rate:       " + serviceRatePrime;
        result += "\n  system time:    " + systemTime;
        result += "\n    - per client: " + systemTimePerClient;
        result += "\n  free time:      " + freeTime;
        result += "\n    - per server: " + freeTimePerServer;
        result += "\n  free counter:   " + freeCounter;
        result += "\n    - per server: " + freeCounterPerServer;
        return result;
    }

    private void initialize(int maxClientNumber) {
        // set max client number
        this.maxClientNumber = maxClientNumber;
        // client ==> clientInList
        double timeInterval;
        clock = 0;
        for (int i = 0; i < maxClientNumber; i++) {
            timeInterval = RandTool.exponential(arrivalRate);
            clock += timeInterval;
            clientInList.add(new Client(i, clock));
            // STATISTICS
            //
            arrivalTime += timeInterval;
        }
        // reset clock
        clock = 0;
    }

    private void statisticize() {
        for (Client c : clientOutList) {
            // STATISTICS
            waitTime += (c.serviceBeginTime - c.joinTime);
            serviceTime += (c.serviceEndTime - c.serviceBeginTime);
            systemTime += (c.serviceEndTime - c.joinTime);
        }
        // STATISTICS
        arrivalTimePerClient = arrivalTime / clientOutList.size();
        arrivalRatePrime = 1 / arrivalTimePerClient;
        waitTimePerClient = waitTime / clientOutList.size();
        serviceTimePerClient = serviceTime / clientOutList.size();
        serviceRatePrime = 1 / serviceTimePerClient;
        systemTimePerClient = systemTime / clientOutList.size();
        for (Server s : serverOutList) {
            s.calculateFreeTime();
            freeTime += s.freeTime;
            clock = Math.max(clock, s.clock);
        }
        freeTimePerServer = freeTime / serverOutList.size();
        freeCounterPerServer = freeCounter / serverOutList.size();
    }

    private boolean simulate(int maxClientNumber) {
        if (isDone) {
            System.out.println("Exception: this.isDone == true");
            return false;
        }
        if (maxClientNumber < 1) {
            System.out.println("Exception: maxClientNumber < 1");
            return false;
        }
        // initialize
        initialize(maxClientNumber);
        // simulate
        //
        // data flow:
        //   - client: clientInList ==> serverList[i].client ==> clientOutList
        //   - server: serverInList ==> serverList ==> serverOutList
        //
        Server s;
        Client c1, c2;
        while (true) {
            if (!serverInList.isEmpty()) {
                //
                // At the very beginning, get server from `serverInList`
                //
                // No client is assigned to server (`server.client == null`);
                //
                s = serverInList.poll();
            } else {
                //
                // Normally get server from `serverInList`.
                //
                // There must be a client which is assigned to server (`server.client != null`);
                //
                s = serverList.poll();
            }
            // Try to get client which has been served by the server.
            c1 = s.client;
            if (c1 != null) {
                // Add it to `clientOutList`.
                clientOutList.add(c1);
            }
            //  Try to assign a new client to the server
            c2 = clientInList.poll();
            if (c2 != null) {
                //
                // `clientInList` is not empty.
                //
                // SERVER CLOCK: BEGIN
                if (s.clock < c2.joinTime) {
                    // Wait until new client joins.
                    s.clock = c2.joinTime;
                    // STATISTICS
                    freeCounter += 1;
                }
                c2.serviceBeginTime = s.clock;
                s.clock += RandTool.exponential(serviceRate);
                // SERVER CLOCK: END
                //
                c2.serviceEndTime = s.clock;
                c2.serverId = s.id;
                s.client = c2;
                s.beginTimeList.add(c2.serviceBeginTime);
                s.endTimeList.add(c2.serviceEndTime);
                s.serviceCounter += 1;
                //
                // note:
                //   - `serverList` is defined as `PriorityQueue<Server>`.
                //   - `server.compareTo()` is determined by `server.clock`.
                //   - As a result, `server` with smaller `server.clock` will be closer to queue head,
                //     which means it has a higher priority to serve new clients.
                //
                serverList.add(s);
            } else {
                //
                // `clientInList` is empty, which means no serve is needed.
                //
                // At this time, add `server` to `serverOutList`.
                //
                s.client = c2;
                serverOutList.add(s);
            }
            if (serverOutList.size() == serverNumber) {
                //
                // When all servers are in `serverOutList`, the loop comes to an end.
                //
                // At this time:
                //   - `serverInList` is empty.
                //   - `serverList` is empty.
                //   - `clientInList` is empty.
                //   - All clients are in `clientOutList` and they are sorted A-Z by "service end time".
                //     (`clientOutList` is defined as `PriorityQueue<Client>` and `client.compareTo()` is determined by `client.serviceEndTime`)
                //
                break;
            }
        }
        // statisticize
        statisticize();
        isDone = true;
        return true;
    }

}

class Client implements Comparable<Client> {

    int id;
    double joinTime;
    double serviceBeginTime;
    double serviceEndTime;
    int serverId;

    Client(int id, double joinTime) {
        this.id = id;
        this.joinTime = joinTime;
        this.serviceBeginTime = 0;
        this.serviceEndTime = 0;
    }

    public int compareTo(Client client) {
        return Double.compare(this.serviceEndTime, client.serviceEndTime);
    }

}

class Server implements Comparable<Server> {

    int id;
    Client client;
    double clock;
    ArrayList<Double> beginTimeList;
    ArrayList<Double> endTimeList;
    double serviceCounter;
    double freeTime;

    Server(int id) {
        this.id = id;
        this.client = null;
        clock = 0;
        beginTimeList = new ArrayList<>();
        endTimeList = new ArrayList<>();
        freeTime = 0;
        serviceCounter = 0;
    }

    public int compareTo(Server server) {
        return Double.compare(clock, server.clock);
    }

    public void calculateFreeTime() {
        double temp = 0;
        for (int i = 0; i < beginTimeList.size(); i++) {
            temp += (endTimeList.get(i) - beginTimeList.get(i));
        }
        freeTime = clock - temp;
    }

}
