import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;
import java.text.*;

public class AccelCar extends JPanel {

    // Simulation variables: acceleration, velocity, distance.
    double a = 0;
    double v = 0;
    double x = 20;

    // Start and end x values.
    int start = 20;
    int end = 300;

    // Time-related variables.
    int time;
    int sleepTime = 100;
    int timeStep = sleepTime;
    int timeLimit = 60000; // 60 seconds

    // score = time-taken + 2*v*v + penalty
    double score;
    double penalty;

    // If an external controller is given.
    Function controller = null;

    // Animation and GUI stuff.
    boolean stopped = true;
    String terminateMsg = "";
    DecimalFormat df = new DecimalFormat("##.##");
    JSlider slider;


    public AccelCar(Function controller) {
        this.controller = controller;
    }


    void start() {
        Thread t = new Thread() {
            public void run() {
                simulateWithAnimation();
            }
        };
        t.start();
    }


    void simulateWithAnimation() {
        init();
        stopped = false;
        runSimulation();
        terminateMsg = computeScore();
        System.out.println(terminateMsg);
        repaint();
    }


    // Without animation.

    double simulate() {
        init();
        stopped = true;
        runSimulation();
        computeScore();
        System.out.println("Final score: " + df.format(score));
        return score;
    }


    void init() {
        // Initialize variables.
        a = 0;
        v = 0;
        x = start;
        time = 0;
        score = 0;
        penalty = 0;
        terminateMsg = "SCORE:                  ";
    }


    void runSimulation() {

        while (!stopped) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
            }

            nextStep();

            if (time > timeLimit) {
                stopped = true;
                System.out.println("FAILED: exceeded time limit");
                penalty = 2 * timeLimit;
                return;
            }

            if (x < start) {
                stopped = true;
                System.out.println("FAILED: Reversed past start");
                penalty = 2 * timeLimit;
                return;
            } else if (x > end) {
                stopped = true;
                penalty = 0;
                break;
            }

            // Redraw.
            repaint();
        }

    }


    void nextStep() {
        // Update clock.
        time += timeStep;

        // Move.
        if (controller != null) {
            a = controller.get(x);
        } else {
            // Read from slider.
            a = (double) slider.getValue();
        }

        double delT = (double) timeStep / 1000.0;  // seconds.

        v = v + delT * a;
        x = x + delT * v;

    }


    String computeScore() {
        score = penalty + time + 2.0 * v * v;
        return "SCORE: " + df.format(score);
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Dimension D = this.getSize();

        // Background.
        g.setColor(Color.white);
        g.fillRect(0, 0, D.width, D.height);

        // Drawline.
        g.setColor(Color.lightGray);
        g.drawLine(start, 100, end, 100);

        g.setColor(Color.red);
        g.fillOval((int) x - 10, 100 - 10, 20, 20);

        // Status:
        g.setColor(Color.black);
        g.setFont(new Font("Serif", Font.BOLD, 20));
        int seconds = time / 1000;
        int millis = time % 1000;
        String clockStr = "" + seconds + ":" + millis;
        g.drawString(clockStr, 300, 20);
        g.setColor(Color.red);
        g.drawString(terminateMsg, 10, 250);
    }


    void reset() {
        if (controller == null) {
            slider.setValue(0);
        }
        this.start();
    }


    ////////////////////////////////////////////////////////////////////////
    // GUI code


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
        panel.add(new JLabel("      "));
        panel.add(new JLabel("Accelerator: "));

        slider = new JSlider(-10, 10, 0);
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        panel.add(slider);

        if (controller != null) {
            slider.setEnabled(false);
        }

        panel.add(new JLabel("        "));
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
        frame.setTitle("Acceleration model");
        Container cPane = frame.getContentPane();
        cPane.add(makeBottomPanel(), BorderLayout.SOUTH);
        cPane.add(this, BorderLayout.CENTER);
        frame.setVisible(true);
    }


    ////////////////////////////////////////////////////////////////////////
    // static methods


    public static double tryControl(Function controller) {
        AccelCar accel = new AccelCar(controller);
        double score = accel.simulate();
        return score;
    }

    public static void animateControl(Function controller) {
        AccelCar a = new AccelCar(controller);
        a.makeFrame();
    }

    public static void main(String[] argv) {
        AccelCar a = new AccelCar(null);
        a.makeFrame();
    }

}
