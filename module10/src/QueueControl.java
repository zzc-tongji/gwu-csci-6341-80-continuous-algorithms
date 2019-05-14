// QueueControl.java
//
// Author: Rahul Simha
// Mar, 2008
//
// Customers pick between two queues. Simplified version without GUI.


import java.util.*;
import java.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;


public class QueueControl {

    // Avg time between arrivals = 1.0, avg time at server=1/0.75.
    double arrivalRate = 0.9;
    double[] serviceRates = {1, 1.5};

    // queue[0] and queue[1] are the two queues.
    LinkedList<Customer>[] queues;

    // A data structure for simulation: list of forthcoming events.
    PriorityQueue<Event> eventList;

    double clock;

    // Statistics.
    int numArrivals = 0;                    // How many arrived?
    int numDepartures;                      // How many left?
    double totalSystemTime, avgSystemTime;  // For time spent in system.

    double currentX = 0.5;


    void reset() {
        eventList = new PriorityQueue<Event>();

        queues = new LinkedList[2];
        queues[0] = new LinkedList<Customer>();
        queues[1] = new LinkedList<Customer>();

        // Initialize stats variables.
        numArrivals = 0;
        numDepartures = 0;
        avgSystemTime = totalSystemTime = 0;

        // Need to have at least one event in event list.
        clock = 0;
        scheduleArrival();

    }

    void simulate(int maxDepartures) {
        reset();
        while (numDepartures < maxDepartures) {
            nextStep();
        }
    }


    void nextStep() {
        // Extract the next event and set the time to that event.
        Event e = eventList.poll();
        clock = e.eventTime;

        // Handle each type separately.
        if (e.type == Event.ARRIVAL) {
            handleArrival(e);
        } else if (e.type == Event.DEPARTURE) {
            handleDeparture(e);
        }

        // Do stats after event is processed.
        stats();
    }


    void handleArrival(Event e) {
        numArrivals++;
        int k = chooseQueue();
        queues[k].add(new Customer(clock));
        if (queues[k].size() == 1) {
            // This is the only customer => schedule a departure.
            scheduleDeparture(k);
        }
        scheduleArrival();
    }


    int chooseQueue() {
        if (RandTool.uniform() < currentX) {
            return 0;
        } else {
            return 1;
        }
    }


    void handleDeparture(Event e) {
        numDepartures++;
        int k = e.whichQueue;
        Customer c = queues[k].removeFirst();
        totalSystemTime += clock - c.entryTime;
        if (queues[k].size() > 0) {
            scheduleDeparture(k);
        }
    }


    void scheduleArrival() {
        double nextArrivalTime = clock + randomInterarrivalTime();
        eventList.add(new Event(nextArrivalTime, Event.ARRIVAL, 0));
    }


    void scheduleDeparture(int i) {
        double nextDepartureTime = clock + randomServiceTime(i);
        eventList.add(new Event(nextDepartureTime, Event.DEPARTURE, i));
    }


    double randomInterarrivalTime() {
        return exponential(arrivalRate);
    }


    double randomServiceTime(int i) {
        return exponential(serviceRates[i]);
    }


    double exponential(double lambda) {
        return (1.0 / lambda) * (-Math.log(1.0 - RandTool.uniform()));
    }

    void stats() {
        if (numDepartures == 0) {
            return;
        }
        avgSystemTime = totalSystemTime / numDepartures;
    }

    public static void main(String[] argv) {
        QueueControl queue = new QueueControl();
        queue.currentX = 0.5;
        queue.simulate(10000);
        System.out.println("Avg sys time: " + queue.avgSystemTime);
    }

}


// Class Customer (one instance per customer) stores whatever we
// need for each customer. Since we collect statistics on waiting
// time at the time of departure, we need to record when a
// customer arrives.

class Customer {
    double entryTime;

    public Customer(double entryTime) {
        this.entryTime = entryTime;
    }
}


// Class Event has everything we need for an event: the type of
// event, and when it occurs. To use Java's PriorityQueue, we need
// have this class implement the Comparable interface where
// one event is "less" if it occurs sooner.

class Event implements Comparable {
    public static int ARRIVAL = 1;
    public static int DEPARTURE = 2;
    int type = -1;                     // Arrival or departure.
    double eventTime;                  // When it occurs.
    int whichQueue = -1;               // Which queue, for a departure.

    public Event(double eventTime, int type, int whichQueue) {
        this.eventTime = eventTime;
        this.type = type;
        this.whichQueue = whichQueue;
    }

    public int compareTo(Object obj) {
        Event e = (Event) obj;
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
