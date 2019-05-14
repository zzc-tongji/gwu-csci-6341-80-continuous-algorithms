// Winch with proportional-derivative control

import java.util.*;
import java.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;

public class Winch3 extends JPanel {

    // Set this to true to make vertical case with gravity.
    static boolean isVertical = false;

    double g = 9.8;              // Set g=0 for horizonal case.

    // Time variables
    double t = 0;                // Current time.
    double delT = 0.1;           // Time increment.

    // Winch variables
    double winchY = 450;         // (x,y) location of wheel center.
    double winchX = 300;
    double mw = 1;               // Mass.
    double R = 50;               // Radius
    double angularVel = 0;       // angular velocity.

    // Load variables (initial y = 0). The location is determined from the wheel.
    double m = 10;               // Mass.          
    double y = 3 * R;              // Height of top edge.
    double yVel = 0;             // Track load velocity.
    double yMax = 250;           // Target.
    double prevY = 0;            // Record previous y value to estimate y'(t)


    // Control variables and parameters.
    double V = 10000;              // Voltage.
    double kP = 500;               // Constant for proportional term.
    double kD = 100;               // Constant for differential term.
    double kI = 10;               // Constant for integral term.
    double S = 0;                // For integration.

    // GUI variables.
    int sleepTime = 100;
    String terminateMsg = "";
    Thread currentThread;

    boolean isStopped = false;
    Function funcY = new Function("y vs time");

    // When the "go" button is pressed, make a thread and fire off a simulation run.

    void go() {
        if (isStopped) {
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


    // Perform one simulation run.

    void simulate() {
        reset();
        while (y < winchY - R) {   // Stop if it collides with the winch.
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                break;
            }

            nextStep();
            repaint();
        }
        terminateMsg = "Final velocity: " + yVel;
    }


    // Initialize variables at t=0

    void reset() {
        t = 0;
        // Initial y = height of load = 2R (by design).
        y = 3 * R;
        prevY = y;

        angularVel = 0;

        if (!isVertical) {
            g = 0;           // No gravity.
        } else {
            g = 9.8;
        }
    }


    void nextStep() {
        // Current time.
        t = t + delT;

        // PID.
        // V = kP * (yMax - y) - kD * (yVel) + kI * S;

        // Proportional-derivative control:
        V = kP * (yMax - y) - kD * (yVel);

        // Assume that torque is proportional to voltage with const=1
        double torque = V;

        // Compute current mu(t) = angular acceleration.
        double angularAcc = (torque - m * g * R) / (m * R * R + mw * R * R);

        // Update angular velocity.
        angularVel = angularVel + angularAcc * delT;

        // We need the change in angle to compute change in Y because y(t)=R*theta(t)
        double delAngle = angularVel * delT;
        double delY = delAngle * R;
        y = y + delY;

        // Estimate y'(t) - for use in control.
        yVel = (y - prevY) / delT;
        prevY = y;

        // Accumulate errors - for use in control.
        S = S + (yMax - y);

        funcY.add(t, y);
    }


    void stop() {
        isStopped = true;
        funcY.show();
    }

    ////////////////////////////////////////////////////////////////////////
    // GUI stuff


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension D = this.getSize();

        // Background.
        g.setColor(Color.white);
        g.fillRect(0, 0, D.width, D.height);

        Graphics2D g2 = (Graphics2D) g;
        AffineTransform originalTrans = g2.getTransform();
        // Rotate for horizontal case.
        if (!isVertical) {
            g2.setTransform(AffineTransform.getRotateInstance(Math.PI / 2, D.width / 2, D.height / 2));
        }

        // Winch.
        int drawX = (int) (winchX - R);
        int drawY = (int) (winchY + R);
        g.setColor(Color.blue);
        g.fillOval(drawX, D.height - drawY, (int) R * 2, (int) R * 2);

        // Load.
        drawX = (int) winchX;
        drawY = (int) y;
        g.setColor(Color.red);
        g.fillRect(drawX, D.height - drawY, (int) R * 2, (int) R * 2);

        // Cable.
        int drawX1 = (int) (winchX + R);
        int drawY1 = (int) winchY;
        int drawY2 = (int) y;
        g.setColor(Color.black);
        g.drawLine(drawX1, D.height - drawY1, drawX1, D.height - drawY2);

        // Goal line.
        g.setColor(Color.lightGray);
        g.drawLine(0, D.height - (int) yMax, D.width, D.height - (int) yMax);

        g2.setTransform(originalTrans);
        // Message.
        g.setColor(Color.black);
        g.drawString(terminateMsg, 500, 500);
    }


    JPanel makeBottomPanel() {
        JPanel panel = new JPanel();

        panel.add(new JLabel("  "));
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
        JButton stopB = new JButton("Stop");
        stopB.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        stop();
                    }
                }
        );
        panel.add(stopB);

        panel.add(new JLabel("  "));
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
        frame.setSize(800, 600);
        frame.setTitle("Winch");
        Container cPane = frame.getContentPane();
        cPane.add(makeBottomPanel(), BorderLayout.SOUTH);
        cPane.add(this, BorderLayout.CENTER);
        frame.setVisible(true);
    }


    public static void main(String[] argv) {
        new Winch3().makeFrame();
    }

}
