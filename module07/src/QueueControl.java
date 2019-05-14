// QueueControl.java
//   - Also included at end of file: classes Customer and Event
//
// Author: Rahul Simha
// Mar, 2008
//
// Customers pick between two queues.


import java.util.*;
import java.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;


public class QueueControl extends JPanel {

    // Avg time between arrivals = 1.0, avg time at server=1/0.75.
    double arrivalRate = 1.0;
    double[] serviceRates = {0.75, 0.6};

    // queue[0] and queue[1] are the two queues.
    LinkedList<Customer>[] queues;

    // A data structure for simulation: list of forthcoming events.
    PriorityQueue<Event> eventList;

    double clock;

    // Statistics.
    int numArrivals = 0;                    // How many arrived?
    int numDepartures;                      // How many left?
    double totalWaitTime, avgWaitTime;      // For time spent in queue.
    double totalSystemTime, avgSystemTime;  // For time spent in system.

    // Animation and drawing.
    boolean doAnimation = true;
    Thread currentThread;
    boolean isPaused = false;
    int sleepTime = 50;
    int r = 20;    // 1/2 width of queue.
    int w = 600;   // Start of queue.
    DecimalFormat df = new DecimalFormat("##.####");


    public QueueControl(boolean doAnimation) {
        this.doAnimation = doAnimation;
    }


    void reset() {
        // The Event class is Comparable, and so can use a Priority Queue directly.
        eventList = new PriorityQueue<Event>();

        // The two queues. Note: JDK 1.5 does not like "new LinkedList<Customer>()"
        queues = new LinkedList[2];
        queues[0] = new LinkedList<Customer>();
        queues[1] = new LinkedList<Customer>();

        // Initialize stats variables.
        numArrivals = 0;
        numDepartures = 0;
        avgWaitTime = totalWaitTime = 0;
        avgSystemTime = totalSystemTime = 0;

        // Need to have at least one event in event list.
        clock = 0;
        scheduleArrival();
    }


    void nextStep() {
        // Extract first event.
        if (eventList.isEmpty()) {
            System.out.println("ERROR: nextStep(): EventList empty");
            return;
        }

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

        if (numDepartures % 1000 == 0) {
            System.out.println("After " + numDepartures + " departures: avgWait=" + avgWaitTime + "  avgSystemTime=" + avgSystemTime);
        }
    }


    void handleArrival(Event e) {
        // For an arrival, we need to put the customer in a queue, and
        // schedule a departure for that queue if there isn't one scheduled.
        // Lastly, we need to schedule the next arrival.

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
        // Pick a random queue:
        int k = UniformRandom.uniform(0, 1);

        // Shortest queue.
        //int k = 0;
        //if (queues[1].size() < queues[0].size()) {
        //    k = 1;
        //}

        return k;
    }


    void handleDeparture(Event e) {
        // For a departure from a queue, remove the customer from
        // that particular queue, then schedule the next departure
        // if that queue has waiting customers.

        numDepartures++;
        int k = e.whichQueue;
        Customer c = queues[k].removeFirst();
        totalSystemTime += clock - c.entryTime;
        if (queues[k].size() > 0) {
            // There's a waiting customer => schedule departure.
            Customer waitingCust = queues[k].get(0);
            // Note where we are collecting stats for waiting time.
            totalWaitTime += clock - waitingCust.entryTime;
            scheduleDeparture(k);
        }
    }


    void scheduleArrival() {
        // The next arrival occurs when we add an interrarrival to the the current time.
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
        return (1.0 / lambda) * (-Math.log(1.0 - UniformRandom.uniform()));
    }

    void stats() {
        if (numDepartures == 0) {
            return;
        }
        avgWaitTime = totalWaitTime / numDepartures;
        avgSystemTime = totalSystemTime / numDepartures;
    }


    ///////////////////////////////////////////////////////////////////////
    // Animation

    void go() {
        // Fire off a thread so that Swing's thread isn't used.
        if (isPaused) {
            isPaused = false;
            return;
        }
        if (currentThread != null) {
            currentThread.interrupt();
            currentThread = null;
        }

        currentThread = new Thread() {
            public void run() {
                simulate();
            }

        };
        currentThread.start();
    }


    void pause() {
        isPaused = true;
    }


    void simulate() {
        while (true) {

            if (!isPaused) {
                nextStep();
            }

            this.repaint();

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                break;
            }


        }

        this.repaint();
    }

    ///////////////////////////////////////////////////////////////////////
    // GUI and drawing


    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!doAnimation) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g;

        // Clear.
        Dimension D = this.getSize();
        g.setColor(Color.white);
        g.fillRect(0, 0, D.width, D.height);

        g2.setStroke(new BasicStroke(2f));

        // Draw service areas.
        int yTop1 = D.height / 2 - 8 * r;
        int yTop2 = D.height / 2 + 6 * r;
        g.setColor(Color.black);
        g.drawOval(w, yTop1, 2 * r, 2 * r);
        g.drawOval(w, yTop2, 2 * r, 2 * r);

        // Draw waiting areas - top.
        g2.drawLine(w - 2 * r, yTop1, w - 12 * r, yTop1);
        g2.drawLine(w - 2 * r, yTop1 + 2 * r, w - 12 * r, yTop1 + 2 * r);
        g2.drawLine(w - 2 * r, yTop1, w - 2 * r, yTop1 + 2 * r);

        // Draw waiting areas - bottom.
        g2.drawLine(w - 2 * r, yTop2, w - 12 * r, yTop2);
        g2.drawLine(w - 2 * r, yTop2 + 2 * r, w - 12 * r, yTop2 + 2 * r);
        g2.drawLine(w - 2 * r, yTop2, w - 2 * r, yTop2 + 2 * r);

        // Label.
        g2.drawString("Queue 0", w - 12 * r, yTop1 + 4 * r);
        g2.drawString("Queue 1", w - 12 * r, yTop2 + 4 * r);

        if ((queues == null) || (queues[0] == null)) {
            return;
        }
        g.setColor(Color.blue);
        for (int k = 0; k < 2; k++) {
            int yTop = yTop1;
            if (k == 1) {
                yTop = yTop2;
            }
            if (queues[k].size() == 0) {
                continue;
            }
            // Draw customer in service.
            g.fillOval(w + 2, yTop + 2, 2 * r - 4, 2 * r - 4);
            for (int i = 1; i < queues[k].size(); i++) {
                int x = w - 2 * r - i * 2 * r;
                g.fillOval(x + 2, yTop + 2, 2 * r - 4, 2 * r - 4);
            }
        }

        // Current averages.
        String str = "numDepartures=" + numDepartures + "  Avg wait = " + df.format(avgWaitTime) + "   avg time in system = " + df.format(avgSystemTime);
        g.setColor(Color.darkGray);
        g.drawString(str, 10, 30);
    }


    JPanel makeBottomPanel() {
        JPanel panel = new JPanel();

        JButton resetB = new JButton("Reset");
        resetB.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        reset();
                    }
                }
        );
        panel.add(resetB);

        panel.add(new JLabel("          "));
        JButton nextB = new JButton("Next");
        nextB.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        nextStep();
                    }
                }
        );
        panel.add(nextB);


        panel.add(new JLabel("          "));
        JButton goB = new JButton("Go");
        goB.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        go();
                    }
                }
        );
        panel.add(goB);

        panel.add(new JLabel("  "));
        JButton pauseB = new JButton("Pause");
        pauseB.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        pause();
                    }
                }
        );
        panel.add(pauseB);

        panel.add(new JLabel("           "));
        JButton quitB = new JButton("Quit");
        quitB.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        System.exit(0);
                    }
                }
        );
        panel.add(quitB);

        return panel;
    }


    void makeFrame() {
        JFrame frame = new JFrame();
        frame.setSize(1000, 600);
        frame.setTitle("Queue Control");
        Container cPane = frame.getContentPane();
        cPane.add(makeBottomPanel(), BorderLayout.SOUTH);
        cPane.add(this, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    ///////////////////////////////////////////////////////////////////////
    // main

    public static void main(String[] argv) {
        if ((argv == null) || (argv.length != 1)) {
            System.out.println("Usage: java QueueControl animate=true\n        OR\n       java QueueControl animate=false");
            System.exit(0);
        }
        if (argv[0].endsWith("true")) {
            QueueControl q = new QueueControl(true);
            q.makeFrame();
        } else {
            QueueControl q = new QueueControl(false);
            q.reset();
            while (true) {
                q.nextStep();
            }
        }
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
