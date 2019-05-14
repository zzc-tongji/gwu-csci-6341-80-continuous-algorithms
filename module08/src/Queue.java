// Queue.java
//
// Author: Rahul Simha
// Mar, 2008
//
// A single-server queue.


import java.util.*;

public class Queue {

    // Avg time between arrivals = 1.0, avg time at server=1/0.75.
    double arrivalRate = 0.75; // λ
    double serviceRate = 1; // μ

    // A data structure to store customers.
    LinkedList<QueueCustomer> queue;

    // A data structure for simulation: list of forthcoming events.
    PriorityQueue<QueueEvent> eventList;

    // The system clock, which we'll advance from event to event.
    double clock;

    // Statistics.
    int numArrivals = 0;                    // How many arrived?
    int numDepartures;                      // How many left?
    double totalWaitTime, avgWaitTime;      // For time spent in queue.
    double totalSystemTime, avgSystemTime;  // For time spent in system.
    double totalInterarrivalTime, avgInterarrivalTime;
    double totalServiceTime, avgServiceTime;
    double interarrivalRatePrime;
    double serviceRatePrime;
    double CustomNumber, avgCustomNumber;
    double free, probabilityFree;

    // Histogram
    PropHistogram M = new PropHistogram(0, 20, 20);
    PropHistogram D = new PropHistogram(0, 20, 20);


    void init() {
        queue = new LinkedList<QueueCustomer>();
        eventList = new PriorityQueue<QueueEvent>();
        clock = 0.0;
        numArrivals = numDepartures = 0;
        totalWaitTime = totalSystemTime = 0.0;
        scheduleArrival();
    }


    void simulate(int maxQueueCustomers) {
        init();

        while (numArrivals < maxQueueCustomers) {
            QueueEvent e = eventList.poll();
            clock = e.eventTime;
            if (e.type == QueueEvent.ARRIVAL) {
                handleArrival(e);
            } else {
                handleDeparture(e);
            }
        }

        stats();
    }


    void handleArrival(QueueEvent e) {
        numArrivals++;
        queue.add(new QueueCustomer(clock));
        CustomNumber += queue.size();
        M.add(queue.size());
        if (queue.size() == 1) {
            // This is the only customer => schedule a departure.
            free += 1;
            scheduleDeparture();
        }
        scheduleArrival();
    }


    void handleDeparture(QueueEvent e) {
        numDepartures++;
        QueueCustomer c = queue.removeFirst();

        // This is the time from start to finish for this customer:
        double timeInSystem = clock - c.arrivalTime;
        D.add(timeInSystem);

        // Maintain total (for average, to be computed later).
        totalSystemTime += timeInSystem;

        if (queue.size() > 0) {
            // There's a waiting customer => schedule departure.
            QueueCustomer waitingCust = queue.get(0);
            // This is the time spent only in waiting:
            double waitTime = clock - waitingCust.arrivalTime;
            // Note where we are collecting stats for waiting time.
            totalWaitTime += waitTime;
            scheduleDeparture();
        }
    }


    void scheduleArrival() {
        // The next arrival occurs when we add an interrarrival to the the current time.
        double nextArrivalTime = clock + randomInterarrivalTime();
        eventList.add(new QueueEvent(nextArrivalTime, QueueEvent.ARRIVAL));
    }


    void scheduleDeparture() {
        double nextDepartureTime = clock + randomServiceTime();
        eventList.add(new QueueEvent(nextDepartureTime, QueueEvent.DEPARTURE));
    }


    double randomInterarrivalTime() {
        double temp = exponential(arrivalRate);
        totalInterarrivalTime += temp;
        return temp;
    }


    double randomServiceTime() {
        double temp = exponential(serviceRate);
        totalServiceTime += temp;
        return temp;
    }


    double exponential(double gamma) {
        return (1.0 / gamma) * (-Math.log(1.0 - RandTool.uniform()));
    }

    void stats() {
        if (numDepartures == 0) {
            return;
        }
        avgWaitTime = totalWaitTime / numDepartures;
        avgSystemTime = totalSystemTime / numDepartures;
        avgInterarrivalTime = totalInterarrivalTime / numArrivals;
        interarrivalRatePrime = 1 / avgInterarrivalTime;
        avgServiceTime = totalServiceTime / numDepartures;
        serviceRatePrime = 1 / avgServiceTime;
        avgCustomNumber = CustomNumber / numArrivals;
        probabilityFree = free / numArrivals;
    }


    public String toString() {
        String results = "Simulation results:";
        results += "\n  numArrivals:           " + numArrivals;
        results += "\n  numDepartures:         " + numDepartures;
        results += "\n  avg Wait:              " + avgWaitTime;
        results += "\n  avg System Time (d)    " + avgSystemTime;
        results += "\n  avg Interarrival Time: " + avgInterarrivalTime;
        results += "\n  interarrival Rate (λ): " + interarrivalRatePrime;
        results += "\n  avg Service Time:      " + avgServiceTime;
        results += "\n  service Rate (μ):      " + serviceRatePrime;
        results += "\n  Custom number:         " + CustomNumber;
        results += "\n  avg Custom Number (m): " + avgCustomNumber;
        results += "\n  probability of Free:   " + probabilityFree;
        return results;
    }


    ///////////////////////////////////////////////////////////////////////
    // main

    public static void main(String[] argv) {
        Queue queue = new Queue();
        queue.simulate(1000);
        System.out.println(queue);
        queue.M.display();
        queue.D.display();
    }

}


// Class `QueueCustomer` (one instance per customer) stores whatever we
// need for each customer. Since we collect statistics on waiting
// time at the time of departure, we need to record when a
// customer arrives.

class QueueCustomer {
    double arrivalTime;

    public QueueCustomer(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}


// Class `QueueEvent` has everything we need for an event: the type of
// event, and when it occurs. To use Java's PriorityQueue, we need
// have this class implement the Comparable interface where
// one event is "less" if it occurs sooner.

class QueueEvent implements Comparable {
    public static int ARRIVAL = 1;
    public static int DEPARTURE = 2;
    int type = -1;                     // Arrival or departure.
    double eventTime;                  // When it occurs.

    public QueueEvent(double eventTime, int type) {
        this.eventTime = eventTime;
        this.type = type;
    }


    public int compareTo(Object obj) {
        QueueEvent e = (QueueEvent) obj;
        if (eventTime < e.eventTime) {
            return -1;
        } else if (eventTime > e.eventTime) {
            return 1;
        } else {
            return 0;
        }
    }

    public boolean equals(Object obj) {
        return (compareTo(obj) == 0);
    }

}
