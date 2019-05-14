import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;
import java.text.*;

public class MultiIncline extends JPanel implements MouseInputListener {

    String statusStr = "";

    boolean designMode = false;       // If in design mode, corners can be moved.
    int radius = 10;                  // Radius of bead.
    int numSegments = 2;              // Number of segments in incline.
    double maxX = 400;                // x3
    double[] segx, segy;              // Store x and y values of intermediate points.

    // Variables tied to the current segment (where the bead is now).
    int currentSeg = -1;
    double cosineOfCurrentAngle;
    double sineOfCurrentAngle;

    // The usual physical variables.
    double m = 1;
    double ax, ay;
    double vx, vy;
    double x, y;
    double g = 9.8;
    int time;

    // Animation:
    int sleepTime = 100;
    int timeStep = sleepTime;
    boolean stopped = true;

    // GUI:
    String terminateMsg = "";
    DecimalFormat df = new DecimalFormat("##.##");
    JTextField segmentsField;
    int currentBeingDragged = -1;


    public MultiIncline() {
        initSegments();
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }


    void initSegments() {
        segx = new double[numSegments + 1];
        segy = new double[numSegments + 1];
        // Initialize the x and y parts.
        double interval = maxX / numSegments;

        // Leftmost point (the start).
        segx[0] = 0;
        segy[0] = maxX;

        // Intermediate points along straight line.
        for (int i = 1; i < numSegments; i++) {
            segx[i] = i * interval;
            segy[i] = maxX - i * interval;
        }

        // Rightmost.
        segx[numSegments] = maxX;
        segy[numSegments] = 0;
    }


    void start() {
        Thread t = new Thread() {
            public void run() {
                simulate();
            }
        };
        t.start();
    }


    void simulate() {
        // Initialize variables.
        double a = 0;
        ax = ay = 0;
        vx = vy = 0;
        x = 0;
        y = maxX;

        // We'll set currentSeg=0 inside loop.
        currentSeg = -1;

        stopped = false;
        time = 0;

        while (!stopped) {

            // Pause animation.
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
            }

            // Update clock.
            time += timeStep;

            double delT = (double) timeStep / 1000.0;  // seconds.

            if (x >= segx[currentSeg + 1]) {
                // Change to new segment.
                currentSeg++;
                double hyp = distance(segx[currentSeg], segy[currentSeg], segx[currentSeg + 1], segy[currentSeg + 1]);
                double yDiff = Math.abs(segy[currentSeg] - segy[currentSeg + 1]);
                double xDiff = Math.abs(segx[currentSeg] - segx[currentSeg + 1]);
                cosineOfCurrentAngle = xDiff / hyp;
                sineOfCurrentAngle = yDiff / hyp;
                a = g * sineOfCurrentAngle;

                // Now conserve energy
                double v = Math.sqrt(vx * vx + vy * vy);
                vx = v * cosineOfCurrentAngle;
                vy = -v * sineOfCurrentAngle;
            }

            // Horizontal and vertical accelerations.
            ax = a * cosineOfCurrentAngle;
            ay = -a * sineOfCurrentAngle;

            // Increase velocity accordingly.
            vx = vx + delT * ax;
            vy = vy + delT * ay;

            // Increase distance according to velocity.
            x = x + delT * vx;
            y = y + delT * vy;

            // If we're close enough to the end, stop.
            if (x >= (maxX - 0.01)) {
                break;
            }

            // Redraw.
            repaint();
        }

    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Dimension D = this.getSize();

        // Background.
        g.setColor(Color.white);
        g.fillRect(0, 0, D.width, D.height);

        // Draw segments.
        g.setColor(Color.lightGray);
        for (int i = 0; i < numSegments; i++) {
            // Draw i-th segment.
            int px = (int) segx[i];
            int py = (int) segy[i];
            int qx = (int) segx[i + 1];
            int qy = (int) segy[i + 1];
            g.drawLine(px, D.height - py, qx, D.height - qy);
            g.fillOval(px - 2, D.height - py - 2, 4, 4);
            g.fillOval(qx - 2, D.height - qy - 2, 4, 4);
        }

        // If design mode, draw circles.
        if (designMode) {
            g.setColor(Color.black);
            for (int i = 0; i <= numSegments; i++) {
                g.drawOval((int) segx[i] - radius, D.height - (int) segy[i] - radius, 2 * radius, 2 * radius);
            }
            return;
        }

        // Draw the bead [sic].
        g.setColor(Color.red);
        g.fillOval((int) x - 10, D.height - (int) y - 10, 20, 20);


        // Time taken so far:
        g.setColor(Color.black);
        g.setFont(new Font("Serif", Font.BOLD, 20));
        int seconds = time / 1000;
        int millis = time % 1000;
        String clockStr = "" + seconds + ":" + millis;
        g.drawString(clockStr, 300, 20);
    }


    //////////////////////////////////////////////////////////////////////
    //
    // GUI stuff below

    void go() {
        designMode = false;
        stopped = false;
        start();
    }

    void design() {
        try {
            int n = Integer.parseInt(segmentsField.getText());
            if ((n >= 2) && (n != numSegments)) {
                numSegments = n;
                initSegments();
            }
            designMode = true;
            this.repaint();
        } catch (Exception e) {
        }
    }


    // These methods are for the mouse-listening interface.
    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
        currentBeingDragged = -1;
    }


    public void mousePressed(MouseEvent e) {
        if (!designMode) {
            return;
        }
        currentBeingDragged = nodeClick(e.getPoint());
    }

    int nodeClick(Point clickPoint) {
        // See if click is within a circle.
        Dimension D = this.getSize();
        for (int i = 1; i < numSegments; i++) {
            double d = distance(clickPoint.x, D.height - clickPoint.y, segx[i], segy[i]);
            if (d < radius) {
                return i;
            }
        }
        return -1;
    }

    double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public void mouseReleased(MouseEvent e) {
        if (!designMode) {
            return;
        }

        currentBeingDragged = -1;
        this.repaint();
    }

    public void mouseDragged(MouseEvent e) {
        if ((!designMode) || (currentBeingDragged < 0)) {
            return;
        }

        Dimension D = this.getSize();
        int my = e.getY();
        // Check if valid.
        double tempY = D.height - my;
        if ((tempY < segy[currentBeingDragged - 1]) &&
                (tempY > segy[currentBeingDragged + 1])) {
            segy[currentBeingDragged] = tempY;
        }
        this.repaint();
    }


    public void makeFrame() {
        JFrame frame = new JFrame();
        frame.setSize(500, 500);
        frame.setResizable(true);
        Container cPane = frame.getContentPane();
        cPane.add(this, BorderLayout.CENTER);
        cPane.add(makeBottomPanel(), BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    JPanel makeBottomPanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("# Segments: "));
        segmentsField = new JTextField(4);
        segmentsField.setText("2");
        panel.add(segmentsField);

        panel.add(new JLabel("   "));
        JButton button = new JButton("Design");
        button.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        design();
                    }
                }
        );
        panel.add(button);

        panel.add(new JLabel("   "));
        button = new JButton("Go");
        button.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        go();
                    }
                }
        );
        panel.add(button);

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
        MultiIncline m = new MultiIncline();
        m.makeFrame();
    }


}
