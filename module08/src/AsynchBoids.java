import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;


public class AsynchBoids extends JPanel {

    int numSteps = 100;                 // Number of time-steps in simulation.

    int numBoids = 10;                  // Boid data.
    Point[] boids = null;
    int numNeighbors = 3;               // Each boid observes some of its neighbors.

    int leader = 0;                     // The current leader.
    double leaderTheta = Math.PI / 4.0;   // Current direction for leader.
    double leaderChangeProb = 0.05;     // The probability that we'll change the leader.

    double randomMoveProb = 0.1;        // Sometimes, a boid moves in a random direction.
    int numMoveAttempts = 5;            // Move attempts before giving up.

    int maxStepSize = 20;               // Distance moved each step.
    int minDistSquare = 200;
    int radius = 10;                    // Drawing parameters.


    // For asychronous simulation, our usual next-event variables:
    PriorityQueue<AsynchBoidsEvent> eventList;
    double clock = 0;


    void init() {
        clock = 0;
        eventList = new PriorityQueue<AsynchBoidsEvent>();
        // As usual, we need to have at least one event in the queue at the start.
        for (int i = 0; i < numBoids; i++) {
            scheduleNextMove(i);
        }

        // Pick random locations.
        boids = new Point[numBoids];
        for (int i = 0; i < numBoids; i++) {
            int x = RandTool.uniform(1, 800);
            int y = RandTool.uniform(1, 600);
            boids[i] = new Point(x, y);
        }
        repaint();
    }


    void simulate() {
        init();

        for (int n = 0; n < numSteps; n++) {

            // Next-event simulation:
            AsynchBoidsEvent event = eventList.poll();
            nextStep(event.i);
            scheduleNextMove(event.i);

            // Sleep for animation effect.
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                break;
            }

            repaint();
        }
    }


    void scheduleNextMove(int i) {
        double moveTime = clock + RandTool.exponential(1.0);
        eventList.add(new AsynchBoidsEvent(moveTime, i));
    }


    void nextStep(int i) {
        // Is it time to change the leader?
        if (RandTool.uniform() < leaderChangeProb) {
            leader = RandTool.uniform(0, numBoids - 1);
            leaderTheta = RandTool.uniform(0.0, Math.PI);
        }

        // Except for the leader, move the others towards neighbors.
        if (i == leader) {
            // Apply different rules for leader.
            boids[i] = moveLeader();
        } else {
            boids[i] = moveOther(i);
        }

        // Note: No need for copying over new locations.
    }


    Point moveLeader() {
        int leaderX = (int) (boids[leader].x + maxStepSize * Math.cos(leaderTheta));
        int leaderY = (int) (boids[leader].y + maxStepSize * Math.sin(leaderTheta));
        Dimension D = this.getSize();
        if ((leaderX < 5) || (leaderX > D.width - 5)
                || (leaderY < 5) || (leaderY > D.height - 5)) {
            // Change direction: either opposite, or random.
            if (RandTool.uniform() < 0.5) {
                leaderTheta = RandTool.uniform(0.0, Math.PI);
            } else {
                // Opposite.
                leaderTheta = 2 * Math.PI - leaderTheta;
            }
            leaderX = boids[leader].x;
            leaderY = boids[leader].y;
        }
        return new Point(leaderX, leaderY);
    }


    Point moveOther(int i) {
        // Identify nearest neighbors.
        int[] neighbors = findNearestNeighbors(i);

        // Compute centroid.
        Point centroid = computeCentroid(neighbors);

        if (RandTool.uniform() < randomMoveProb) {
            // Random move: pick a random direction.
            return randomMove(i);
        } else {
            // Apply rules.
            return applyRules(i, neighbors, centroid);
        }

    }


    Point computeCentroid(int[] neighbors) {
        int sumX = 0, sumY = 0;
        for (int k = 0; k < numNeighbors; k++) {
            sumX += boids[neighbors[k]].x;
            sumY += boids[neighbors[k]].y;
        }
        int centX = (int) ((double) sumX / (double) numNeighbors);
        int centY = (int) ((double) sumY / (double) numNeighbors);
        return new Point(centX, centY);
    }


    Point randomMove(int i) {
        double theta = RandTool.uniform(0.0, 2.0 * Math.PI);
        int x = (int) (boids[i].x + maxStepSize * Math.cos(theta));
        int y = (int) (boids[i].y + maxStepSize * Math.sin(theta));
        return new Point(x, y);
    }


    Point applyRules(int i, int[] neighbors, Point centroid) {
        double centDist = Math.sqrt(distance(boids[i].x, boids[i].y, centroid.x, centroid.y));
        double maxAlpha = 1.0;
        if (centDist < (double) maxStepSize) {
            maxAlpha = 1.0;
        } else {
            maxAlpha = (double) maxStepSize / centDist;
        }

        boolean succ = false;
        double alpha = maxAlpha;
        int x = 0, y = 0;

        for (int m = 0; m < numMoveAttempts; m++) {
            // Compute new x,y.
            int tempX = (int) (boids[i].x + alpha * (centroid.x - boids[i].x));
            int tempY = (int) (boids[i].y + alpha * (centroid.y - boids[i].y));
            int setDist = setDistance(tempX, tempY, neighbors);
            if (setDist > minDistSquare) {
                // Valid.
                x = tempX;
                y = tempY;
                succ = true;
                break;
            }
            // Otherwise, try a smaller move.
            alpha = alpha / 2.0;
        }

        // If not successful, try moving away from centroid.
        alpha = maxAlpha;
        if (!succ) {
            x = (int) (boids[i].x - alpha * (centroid.x - boids[i].x));
            y = (int) (boids[i].y - alpha * (centroid.y - boids[i].y));
        }

        return new Point(x, y);
    }


    int[] findNearestNeighbors(int i) {
        int[] neighbors = new int[numNeighbors];
        int current = i;
        for (int k = 0; k < numNeighbors; k++) {
            current = nextBoid(current);
            neighbors[k] = current;
        }
        return neighbors;
    }


    int nextBoid(int i) {
        if (i < numBoids - 1) {
            return i + 1;
        } else {
            return 0;
        }
    }


    int distance(int x1, int y1, int x2, int y2) {
        return ((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }


    int setDistance(int x, int y, int[] neighbors) {
        // Distance to closest neighbor.
        Point p = boids[neighbors[0]];
        int min = distance(x, y, p.x, p.y);
        for (int k = 1; k < numNeighbors; k++) {
            p = boids[neighbors[k]];
            int d = distance(x, y, p.x, p.y);
            if (d < min) {
                min = d;
            }
        }
        return min;
    }


    ///////////////////////////////////////////////////////////////////////
    // GUI and drawing


    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (boids == null) {
            return;
        }

        // Clear.
        Dimension D = this.getSize();
        g.setColor(Color.white);
        g.fillRect(0, 0, D.width, D.height);

        g.setColor(Color.white);
        g.fillRect(0, 0, D.width, D.height);
        for (int i = 0; i < numBoids; i++) {
            int topLeftX = boids[i].x - radius;
            int topLeftY = D.height - (boids[i].y - radius);
            g.setColor(Color.black);
            g.fillOval(topLeftX, topLeftY, 2 * radius, 2 * radius);
            g.setColor(Color.darkGray);
            Font f = new Font("Serif", Font.BOLD, 10);
            g.setFont(f);
            g.drawString("" + i, boids[i].x - 2, D.height - boids[i].y + 2);
        }
    }

    void makeFrame() {
        JFrame frame = new JFrame();
        frame.setSize(800, 600);
        frame.setTitle("Queue Control");
        Container cPane = frame.getContentPane();
        cPane.add(this, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    ///////////////////////////////////////////////////////////////////////
    // main

    public static void main(String[] argv) {
        AsynchBoids boids = new AsynchBoids();
        boids.makeFrame();
        boids.simulate();
    }

}


class AsynchBoidsEvent implements Comparable {

    double eventTime;                  // When it occurs.
    int i;

    public AsynchBoidsEvent(double eventTime, int i) {
        this.eventTime = eventTime;
        this.i = i;
    }


    public int compareTo(Object obj) {
        AsynchBoidsEvent e = (AsynchBoidsEvent) obj;
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
