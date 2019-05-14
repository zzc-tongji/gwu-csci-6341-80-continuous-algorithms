// PointDisplay.java
//
// Author: Rahul Simha
// Mar, 2008
//
// A simple display of 2D points.

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.text.*;

public class PointDisplay extends JPanel {

    ArrayList<Point2D.Double> points;   // Each point is an instance of Point2D.Double
    double minX, maxX, minY, maxY;      // Bounds.
    int numIntervals = 10;              // # tick marks.

    // To prettify.
    DecimalFormat df = new DecimalFormat();

    // GUI stuff.
    int inset = 60;            // Inset of axes and bounding box.


    public PointDisplay() {
        points = new ArrayList<Point2D.Double>();
        df.setMaximumFractionDigits(4);
    }


    public void add(double x, double y) {
        add(new Point2D.Double(x, y));
    }

    public void add(Point2D.Double p) {
        points.add(p);
    }


    public Point2D.Double get(int i) {
        if ((points == null) || (i >= points.size())) {
            return null;
        }
        return points.get(i);
    }


    void computeBounds() {
        minX = minY = Double.MAX_VALUE;
        maxX = maxY = Double.MIN_VALUE;
        for (Point2D.Double p : points) {
            if (p.x < minX) {
                minX = p.x;
            }
            if (p.x > maxX) {
                maxX = p.x;
            }
            if (p.y < minY) {
                minY = p.y;
            }
            if (p.y > maxY) {
                maxY = p.y;
            }
        }
    }


    public void display() {
        computeBounds();
        // Make a frame and add this instance (as JPanel).
        JFrame frame = new JFrame();
        frame.setSize(600, 480);
        frame.getContentPane().add(this);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.setVisible(true);
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Background.
        Dimension D = this.getSize();
        g.setColor(Color.white);
        g.fillRect(0, 0, D.width, D.height);
        Graphics2D g2 = (Graphics2D) g;
        g.setColor(Color.black);

        // Finally, draw the points.
        if ((points == null) || (points.size() <= 1)) {
            g.drawString("At least two points are needed for display", 50, 100);
            return;
        }

        // Axes, bounding box.
        g.drawLine(inset, D.height - inset, D.width - inset, D.height - inset);
        g.drawLine(inset, inset, inset, D.height - inset);
        g.drawLine(D.width - inset, inset, D.width - inset, D.height - inset);
        g.drawLine(inset, inset, D.width - inset, inset);


        double xDelta = (maxX - minX) / numIntervals;

        // X-ticks and labels.
        for (int i = 1; i <= numIntervals; i++) {
            double xTickd = i * xDelta;
            int xTick = (int) (xTickd / (maxX - minX) * (D.width - 2 * inset));
            g.drawLine(inset + xTick, D.height - inset - 5, inset + xTick, D.height - inset + 5);
            double x = minX + i * xDelta;
            g.drawString(df.format(x), xTick + inset - 5, D.height - inset + 20);
        }

        // Y-ticks
        double yDelta = (maxY - minY) / numIntervals;
        for (int i = 0; i < numIntervals; i++) {
            int yTick = (i + 1) * (int) ((D.height - 2 * inset) / (double) numIntervals);
            g.drawLine(inset - 5, D.height - yTick - inset, inset + 5, D.height - yTick - inset);
            double y = minY + (i + 1) * yDelta;
            g.drawString(df.format(y), 1, D.height - yTick - inset);
        }

        // The points.
        g.setColor(Color.blue);
        for (Point2D.Double p : points) {
            int x = (int) ((p.x - minX) / (maxX - minX) * (D.width - 2 * inset));
            int y = (int) ((p.y - minY) / (maxY - minY) * (D.height - 2.0 * inset));
            g.fillOval(inset + x - 3, D.height - y - inset - 3, 6, 6);
        }
    }


    public static void main(String[] argv) {
        // Test.
        PointDisplay pd = new PointDisplay();
        pd.add(1, 2);
        pd.add(2, 3);
        pd.add(1.5, 2.5);
        pd.display();
    }

}
