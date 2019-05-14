import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;
import java.text.*;

public class Incline extends JPanel {

    String statusStr = "";

    double m = 1;                // Mass.
    double alpha = 30;           // Angle of incline to the horizontal.
    double a = 0;                // Acceleration along incline.
    double v = 0;                // Velocity along incline.
    double d = 0;                // Distance from top of incline.
    double g = 9.8;              // meters /second^2

    int time;                    // Track the current time.
    int sleepTime = 100;         // For animation.
    int timeStep = sleepTime;    // Advance the clock by this much.

    boolean showShadows = true;  // Show x and y components?

    double top = 0;              // y-value of top of incline (to be calculated).
    double inclineLength = 0;    // Total length (to be calculated).
    int tickSpacing = 30;        // Spacing of tick marks along incline.
    int tickSize = 4;            // Size of each tick mark.

    boolean stopped = true;      // Animation status.

    String terminateMsg = "";
    DecimalFormat df = new DecimalFormat("##.##");

    // Textfields:
    JTextField angleField, massField;


    void start() {
        // Make a thread and run a simulation (whenever "Go" is clicked).
        Thread t = new Thread() {
            public void run() {
                simulate();
            }
        };
        t.start();
    }


    void simulate() {
        runSimulation(100000);
    }


    double runSimulation(double stopTime) {
        // Initialize variables.
        a = 0;
        v = 0;
        d = 0;

        stopped = false;
        time = 0;

        while (!stopped) {

            // First pause the thread.
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
            }

            // Now we're past the sleeptime. Update clock.
            time += timeStep;

            // Interval of time in seconds.
            double delT = (double) timeStep / 1000.0;

            // Acceleration along incline: g*sin(alpha).
            double angleRadians = 2 * Math.PI * alpha / 360.0;
            a = g * Math.sin(angleRadians);

            // Increase velocity accordingly.
            v = v + delT * a;

            // Increase distance according to velocity.
            d = d + delT * v;

            if ((time >= stopTime) || (d >= inclineLength)) {
                stopped = true;
                break;
            }

            // Redraw screen.
            repaint();
        }

        // Done.
        return d;
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Dimension D = this.getSize();

        // Background.
        g.setColor(Color.white);
        g.fillRect(0, 0, D.width, D.height);

        // Draw X and Y ticks.
        g.setColor(Color.lightGray);
        for (int i = 0; i <= D.width; i += tickSpacing) {
            g.drawLine(i, D.height, i, D.height - tickSize);
        }
        for (int i = 0; i <= D.height; i += tickSpacing) {
            g.drawLine(0, D.height - i, tickSize, D.height - i);
        }

        // Draw incline.
        double angleRadians = 2 * Math.PI * alpha / 360.0;
        top = D.width * Math.tan(angleRadians);
        g.drawLine(0, D.height - (int) top, D.width, D.height);

        // Draw markers on incline.
        inclineLength = D.width / Math.cos(angleRadians);
        for (double mark = 0; mark <= inclineLength; mark += tickSpacing) {
            // Distance along incline from bottom.
            double dist = inclineLength - mark;
            int xVal = D.width - (int) (dist * Math.cos(angleRadians));
            int yVal = (int) (dist * Math.sin(angleRadians));
            g.fillOval(xVal - 2, D.height - yVal - 2, 4, 4);
        }

        // Draw the bead [sic].
        g.setColor(Color.red);
        int x = (int) (D.width - (inclineLength - d) * Math.cos(angleRadians));
        int y = (int) ((inclineLength - d) * Math.sin(angleRadians));
        g.fillOval(x - 10, D.height - y - 10, 20, 20);

        // Draw shadows.
        if (showShadows) {
            g.setColor(Color.lightGray);
            g.fillOval(x - 5, D.height - 5, 10, 10);
            g.fillOval(-5, D.height - y - 5, 10, 10);
        }


        // Status:
        g.setColor(Color.black);
        g.setFont(new Font("Serif", Font.BOLD, 20));
        int seconds = time / 1000;
        int millis = time % 1000;
        String clockStr = "time: " + seconds + ":" + millis;
        g.drawString(clockStr, 300, 20);
        g.setColor(Color.red);
        g.drawString("dist: " + df.format(d), 300, 40);
    }


    void go() {
        try {
            // Read angle and mass from textfields.
            alpha = Double.parseDouble(angleField.getText());
            m = Double.parseDouble(massField.getText());
            stopped = false;
            // Fire off the simulation thread.
            start();
        } catch (Exception e) {
        }
    }


    public void makeFrame() {
        JFrame frame = new JFrame();
        frame.setSize(500, 400);
        frame.setResizable(true);
        Container cPane = frame.getContentPane();
        cPane.add(this, BorderLayout.CENTER);
        cPane.add(makeBottomPanel(), BorderLayout.SOUTH);
        frame.setVisible(true);
    }


    JPanel makeBottomPanel() {
        JPanel panel = new JPanel();

        // Angle field.
        panel.add(new JLabel("Angle: "));
        angleField = new JTextField(4);
        angleField.setText("30");
        panel.add(angleField);
        panel.add(new JLabel("   "));

        // Mass field.
        panel.add(new JLabel("Mass: "));
        massField = new JTextField(4);
        massField.setText("1");
        panel.add(massField);
        panel.add(new JLabel("       "));

        // Go button.
        JButton button = new JButton("Go");
        button.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        go();
                    }
                }
        );
        panel.add(button);

        // Quit button.
        panel.add(new JLabel("       "));
        button = new JButton("Quit");
        button.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        System.exit(0);
                    }
                }
        );
        panel.add(button);

        return panel;
    }


    public static void main(String[] argv) {
        Incline I = new Incline();
        I.makeFrame();
    }

}
