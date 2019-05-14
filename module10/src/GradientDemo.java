// GradientDemo.java
//
// Author: Rahul Simha
// Mar, 2008
//
// An example demonstrating the gradient-descent method.

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.text.*;


public class GradientDemo extends JPanel {

    double alpha = 0.01;          // Algorithm parameter: the stepsize.

    double a = 1, b = 9;              // Range [a,b]
    double currentX = a;          // x.
    double derivAtCurrentX;       // f'(x)

    // GUI stuff.
    double currentY;              // Used in drawing gradient. 
    double deltaX = 0.1;          // Spacing for sampling points.
    int inset = 60;                 // Inset of axes and bounding box.
    double gradOffsetX = 1.0;


    //////////////////////////////////////////////////////////////////////
    // Gradient method.

    double computef(double x) {
        return 5 + (x - 4.71) * (x - 4.71);
    }


    double computefDeriv(double x) {
        return 2 * (x - 4.71);
    }


    void init() {
        currentX = 1.8;
    }


    void nextStep() {
        derivAtCurrentX = computefDeriv(currentX);
        System.out.println("x = " + currentX + ", xf'(x) = " + (currentX - alpha * derivAtCurrentX));
        currentX = currentX - alpha * derivAtCurrentX;
        repaint();
    }


    //////////////////////////////////////////////////////////////////////
    // GUI code.

    public void display() {
        // Make a frame and add this instance (as JPanel).
        JFrame frame = new JFrame();
        frame.setSize(600, 480);
        Container cPane = frame.getContentPane();
        cPane.add(makeBottomLayer(), BorderLayout.SOUTH);
        cPane.add(this, BorderLayout.CENTER);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.setVisible(true);
    }


    JPanel makeBottomLayer() {
        JPanel panel = new JPanel();
        JButton resetB = new JButton("Reset");
        resetB.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        init();
                    }
                }
        );
        panel.add(resetB);
        panel.add(new JLabel("      "));
        JButton nextB = new JButton("Next");
        nextB.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        nextStep();
                    }
                }
        );
        panel.add(nextB);
        panel.add(new JLabel("      "));
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

        double w = D.width - 2 * inset;
        double h = D.height - 2 * inset;
        double xInterval = w / 10;
        double yInterval = h / 30;

        // X-ticks: 0 to 10 and labels.
        for (int i = 1; i <= 10; i++) {
            int xTick = (int) (i * xInterval);
            g.drawLine(inset + xTick, D.height - inset - 5, inset + xTick, D.height - inset + 5);
            g.drawString("" + i, xTick + inset - 5, D.height - inset + 20);
        }

        // Y-ticks: 0 to 25 and labels.
        for (int i = 0; i <= 30; i += 3) {
            int yTick = (int) (i * yInterval);
            g.drawLine(inset - 5, D.height - yTick - inset, inset + 5, D.height - yTick - inset);
            g.drawString("" + i, 1, D.height - yTick - inset);
        }


        // Draw the function.
        g2.setStroke(new BasicStroke(2f));
        g.setColor(Color.blue);
        boolean first = true;
        int prevX = 0, prevY = 0;
        for (double x = a; x <= b; x += deltaX) {
            double y = computef(x);
            int plotX = inset + (int) (w * (x - 0) / 10.0);
            int plotY = D.height - inset - (int) (h * (y - 0) / 30.0);
            if (first) {
                first = false;
            } else {
                g.drawLine(prevX, prevY, plotX, plotY);
            }
            prevX = plotX;
            prevY = plotY;
        }

        // Draw currentX and deriv.
        g.setColor(Color.red);
        int plotX = inset + (int) (w * (currentX - 0) / 10.0);
        g.drawLine(plotX, D.height - inset - 5, plotX, D.height - inset + 5);
        // Now gradient.
        if (derivAtCurrentX == 0) {
            return;
        }
        double x1 = currentX - gradOffsetX;
        double x2 = currentX + gradOffsetX;
        int plotX1 = (int) (inset + (int) (w * (x1 - 0) / 10.0));
        int plotX2 = (int) (inset + (int) (w * (x2 - 0) / 10.0));
        derivAtCurrentX = computefDeriv(currentX);
        currentY = computef(currentX);
        double y1 = currentY + (-gradOffsetX) * derivAtCurrentX;
        double y2 = currentY + (gradOffsetX) * derivAtCurrentX;
        int plotY1 = D.height - inset - (int) (h * (y1 - 0) / 30.0);
        int plotY2 = D.height - inset - (int) (h * (y2 - 0) / 30.0);
        g.setColor(Color.red);
        g2.setStroke(new BasicStroke(1f));
        g.drawLine(plotX1, plotY1, plotX2, plotY2);
    }


    public static void main(String[] argv) {
        GradientDemo gradientDemo = new GradientDemo();
        gradientDemo.display();
    }


}
