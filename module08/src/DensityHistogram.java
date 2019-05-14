// DensityHistogram.java
//
// Author: Rahul Simha
// Mar, 2008
//
// A simple density histogram and display, modified from PropHistogram.java

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.text.*;

public class DensityHistogram extends JPanel {

    double[] counts;         // The number of data values that fall in i-th bin.
    double[] heights;        // The height, for a relative histogram.
    double delta;            // Width of each bin.
    int numIntervals;        // Number of bins.
    double left, right;      // Left and right ends of interval. All other values are discarded.
    int numSamples;          // # data points added.

    // To prettify.
    DecimalFormat df = new DecimalFormat();

    // GUI stuff.
    int inset = 60;            // Inset of axes and bounding box.


    public DensityHistogram(double left, double right, int numIntervals) {
        this.left = left;
        this.right = right;
        this.numIntervals = numIntervals;
        delta = (right - left) / numIntervals;
        counts = new double[numIntervals];
        heights = new double[numIntervals];
        numSamples = 0;
        df.setMaximumFractionDigits(4);
    }


    public void add(double x) {
        numSamples++;
        int bin = (int) ((x - left) / delta);
        if ((bin >= 0) && (bin < numIntervals)) {
            counts[bin]++;
        } else {
            // Discard.
        }
    }


    void computeHeights() {
        for (int i = 0; i < numIntervals; i++) {
            heights[i] = ((counts[i] / numSamples) / delta);
        }
    }


    public String toString() {
        computeHeights();
        String str = "Density histogram: \n";
        for (int i = 0; i < numIntervals; i++) {
            double a = left + i * delta;
            double b = a + delta;
            str += "[" + df.format(a) + "," + df.format(b) + "]: " + heights[i] + "\n";
        }
        return str;
    }


    public void display() {
        computeHeights();
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


        // Axes, bounding box.
        g.drawLine(inset, D.height - inset, D.width - inset, D.height - inset);
        g.drawLine(inset, inset, inset, D.height - inset);
        g.drawLine(D.width - inset, inset, D.width - inset, D.height - inset);
        g.drawLine(inset, inset, D.width - inset, inset);

        // X-ticks and labels.
        int modVal = numIntervals / 5;
        for (int i = 1; i <= numIntervals; i++) {
            double xTickd = i * delta;
            int xTick = (int) (xTickd / (right - left) * (D.width - 2 * inset));
            g.drawLine(inset + xTick, D.height - inset - 5, inset + xTick, D.height - inset + 5);
            double x = left + i * delta;
            if (numIntervals <= 10) {
                g.drawString(df.format(x), xTick + inset - 5, D.height - inset + 20);
            } else if (i % modVal == 0) {
                // If there are too many values, write out only 5.
                g.drawString(df.format(x), xTick + inset - 5, D.height - inset + 20);
            }

        }

        // Y-ticks
        // First, find max.
        double maxY = Double.MIN_VALUE;
        for (int i = 0; i < numIntervals; i++) {
            if (heights[i] > maxY) {
                maxY = heights[i];
            }
        }
        // We'll make only 10 ticks.
        int numYTicks = 10;
        double yDelta = maxY / numYTicks;
        for (int i = 0; i < numYTicks; i++) {
            int yTick = (i + 1) * (int) ((D.height - 2 * inset) / (double) numYTicks);
            g.drawLine(inset - 5, D.height - yTick - inset, inset + 5, D.height - yTick - inset);
            double yValue = (i + 1) * yDelta;
            g.drawString(df.format(yValue), 1, D.height - yTick - inset);
        }

        // Finally, draw the histogram.
        g.setColor(Color.blue);
        for (int i = 0; i < numIntervals; i++) {
            int topLeftY = (int) ((heights[i] / maxY) * (D.height - 2.0 * inset));
            double x1 = i * delta;
            int topLeftX = (int) (x1 / (right - left) * (D.width - 2 * inset));
            double x2 = (i + 1) * delta;
            int endX = (int) (x2 / (right - left) * (D.width - 2 * inset));
            int barWidth = endX - topLeftX - 10;
            g.fillRect(inset + topLeftX + 5, D.height - topLeftY - inset, barWidth, topLeftY);
        }

    }

}
