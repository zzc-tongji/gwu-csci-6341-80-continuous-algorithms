// ArmSimulator.java
//
// Author: Rahul Simha
// December 2013
//
// Modified Jan 2017: specialized example for two-link case.
// 
// NOTE ABOUT COORDINATES: All the control code will use standard
// Cartesian coordinates with the origin in the lower-left corner.
// The GUI code converts to Java's coordinates where necessary.

import java.util.*;
import java.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;

class ArmSimulatorNode {
    int ID;
    double x, y;
    double nextX, nextY;
    double angleFromPrev;
    Matrix33 transMatrix = new Matrix33();  // Transformation from previous
}

public class ArmSimulator extends JPanel {

    static final boolean debug = false;
    static ArmSimulator simulator;        // For static methods.

    int numLinks = 2;
    Vector<ArmSimulatorNode> nodes;
    Vector<Rectangle2D.Double> obstacles;

    ArmSimulatorNode target;

    ArmController armController = null;

    // Change of angle determined by controller.
    double maxDelTheta = 2 * Math.PI * 10 / 360.0;
    double[] delTheta = new double[numLinks];

    // Radius of circle to draw.
    double radius = 0.5;
    int numNodes = numLinks + 1;
    double linkSize = 20;
    int currentNode = -1;
    int numIllegalMoves = 0;
    boolean isTorque = false;

    double minX = 0, maxX = 200, minY = 0, maxY = 100;

    Dimension D;                 // Size of drawing area.
    int numIntervalsX = 10;      // # tick marks.
    int numIntervalsY = 10;      // # tick marks.
    int inset = 60;                // Inset of axes and bounding box.


    // The time step. 0.1 is a large value. We might reduce it
    // later and also reduce the sleeptime.
    double delT = 0.1;
    double time;
    int sleepTime = 200;

    // Animation stuff.
    Thread currentThread;
    boolean isPaused = false;

    // GUI stuff.
    String[] models = {"Angle", "Torque"};
    JComboBox modelBox;
    JToggleButton obstacleButton = new JToggleButton("Obstacles", false);
    boolean useObstacles = false;
    JToggleButton tracingButton = new JToggleButton("Tracing", false);
    boolean useTracing = false;
    // For tracing the last node (the head of the arm)
    LinkedList<Line2D.Double> lineSegments = new LinkedList<Line2D.Double>();

    String msg = "";
    DecimalFormat df = new DecimalFormat("##.##");
    JTextField controllerField;
    JTextField numLinksField;

    ////////////////////////////////////////////////////////////////////////
    // Constructors

    public ArmSimulator() {
        initializeArmAndObjects();
    }

    void initializeArmAndObjects() {
        // Make the nodes and place them in their initial positions.
        // Note: the  positions satisfy the link size.
        nodes = new Vector<ArmSimulatorNode>();
        ArmSimulatorNode node = new ArmSimulatorNode();
        node.ID = 0;
        node.x = node.y = 0;
        node.angleFromPrev = 0;
        nodes.add(node);
        for (int i = 1; i < numNodes; i++) {
            node = new ArmSimulatorNode();
            node.ID = i;
            if (i == 1) {
                // First one is 15 deg from x-axis
                node.angleFromPrev = toRadians(15);
                ;
            } else if (i % 2 == 0) {
                // Odd links (even nodes) are at 150 deg
                node.angleFromPrev = toRadians(150);
            } else {
                // In the clockwise direction, you need to add 180.
                node.angleFromPrev = toRadians(180 + 30);
            }
            nodes.add(node);
        }

        // Compute forward transformation with matrices.
        forwardKinematics();

        // Target.
        target = new ArmSimulatorNode();
        target.x = 30;
        target.y = 10;
    }

    void setObstacles() {
        if (obstacleButton.isSelected()) {
            useObstacles = true;
            // The obstacles.
            obstacles = new Vector<Rectangle2D.Double>();
            obstacles.add(new Rectangle2D.Double(25, 32, 15, 5));
            obstacles.add(new Rectangle2D.Double(35, 20, 12, 12));
        } else {
            useObstacles = false;
        }
        msg = "With obstacles";
        this.repaint();
    }

    void setTracing() {
        useTracing = tracingButton.isSelected();
        msg = "Set tracing on";
        this.repaint();
    }

    ////////////////////////////////////////////////////////////////////////
    // Drawing

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        RenderingHints rh = g2.getRenderingHints();
        rh.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHints(rh);

        // Clear.
        D = this.getSize();
        g.setColor(Color.white);
        g.fillRect(0, 0, D.width, D.height);

        // Axes, bounding box.
        g.setColor(Color.gray);
        g.drawLine(inset, D.height - inset, D.width - inset, D.height - inset);
        g.drawLine(inset, inset, inset, D.height - inset);
        g.drawLine(D.width - inset, inset, D.width - inset, D.height - inset);
        g.drawLine(inset, inset, D.width - inset, inset);


        // X-ticks and labels.
        double xDelta = (maxX - minX) / numIntervalsX;
        for (int i = 1; i <= numIntervalsX; i++) {
            double xTickd = i * xDelta;
            int xTick = realToJavaX(xTickd);
            g.drawLine(xTick, D.height - inset - 5, xTick, D.height - inset + 5);
            double x = minX + i * xDelta;
            g.drawString(df.format(x), xTick - 5, D.height - inset + 20);
        }

        // Y-ticks
        double yDelta = (maxY - minY) / numIntervalsY;
        for (int i = 0; i < numIntervalsY; i++) {
            int yTick = (i + 1) * (int) ((D.height - 2 * inset) / (double) numIntervalsY);
            g.drawLine(inset - 5, D.height - yTick - inset, inset + 5, D.height - yTick - inset);
            double y = minY + (i + 1) * yDelta;
            g.drawString(df.format(y), 1, D.height - yTick - inset);
        }

        if (useTracing) {
            if (lineSegments != null) {
                g2.setColor(Color.yellow);
                for (Line2D.Double L : lineSegments) {
                    drawLine(g2, L);
                }
            }
        }

        // Arm
        ArmSimulatorNode prevNode = nodes.get(0);
        g.setColor(Color.blue);
        drawCircle(g, prevNode.x, prevNode.y, 2 * radius);
        for (int i = 1; i < numNodes; i++) {
            ArmSimulatorNode node = nodes.get(i);
            g.setColor(Color.blue);
            drawCircle(g, node.x, node.y, 2 * radius);
            g.setColor(Color.gray);
            drawLine(g, prevNode.x, prevNode.y, node.x, node.y);
            prevNode = node;
        }

        // Draw obstacles.
        if (useObstacles) {
            g.setColor(Color.red);
            for (Rectangle2D.Double R : obstacles) {
                Rectangle2D.Double Rjava = new Rectangle2D.Double(R.x, R.y, R.width, R.height);
                drawRectangle(g, Rjava);
            }
        }

        // Target.
        g.setColor(Color.green);
        drawCircle(g, target.x, target.y, 2 * radius);

        // Message.
        g.setColor(Color.black);
        msg = "# Illegal moves: " + numIllegalMoves;
        g.drawString(msg, 20, 20);
    }


    int realToJavaX(double x) {
        int scaledX = (int) ((x - minX) / (maxX - minX) * (D.width - 2 * inset));
        return (inset + scaledX);
    }


    int realToJavaY(double y) {
        int scaledY = (int) ((y - minY) / (maxY - minY) * (D.height - 2.0 * inset));
        return (D.height - inset - scaledY);
    }


    double javaToRealX(int jX) {
        int scaledX = jX - inset;
        double x = minX + scaledX * (maxX - minX) / (D.width - 2.0 * inset);
        return x;
    }


    double javaToRealY(int jY) {
        int scaledY = D.height - inset - jY;
        double y = minY + scaledY * (maxY - minY) / (D.height - 2.0 * inset);
        return y;
    }


    void drawLine(Graphics g, Line2D.Double L) {
        if (L == null) {
            return;
        }
        drawLine(g, L.x1, L.y1, L.x2, L.y2);
    }


    void drawLine(Graphics g, double Lx1, double Ly1, double Lx2, double Ly2) {
        int x1 = realToJavaX(Lx1);
        int y1 = realToJavaY(Ly1);
        int x2 = realToJavaX(Lx2);
        int y2 = realToJavaY(Ly2);
        g.drawLine(x1, y1, x2, y2);
    }


    void drawRectangle(Graphics g, Rectangle2D.Double R) {
        if (R == null) {
            return;
        }
        int x1 = realToJavaX(R.x);
        int y1 = realToJavaY(R.y);
        double x = R.x + R.width;
        double y = R.y - R.height;
        int x2 = realToJavaX(x);
        int y2 = realToJavaY(y);
        g.fillRect(x1, y1, x2 - x1, y2 - y1);
    }


    void drawCircle(Graphics g, double cx, double cy, double radius) {
        int x = realToJavaX(cx);
        int y = realToJavaY(cy);
        int r = realToJavaY(0) - realToJavaY(radius);
        g.fillOval(x - r, y - r, 2 * r, 2 * r);
    }


    double toDegrees(double r) {
        // Convert radians to degrees.
        return 360 * r / (2 * Math.PI);
    }

    double toRadians(double degrees) {
        return 2 * Math.PI * degrees / 360.0;
    }


    void forwardKinematics() {
        for (int i = 1; i < numNodes; i++) {
            ArmSimulatorNode node = nodes.get(i);
            ArmSimulatorNode prevNode = nodes.get(i - 1);
            forwardKinematics(prevNode, node);
        }
    }

    void forwardKinematics(ArmSimulatorNode prev, ArmSimulatorNode node) {
        // We have the coords of prev, the angle to node. Need to
        // instantiate the matrix and multiply.
        Vector31 v = new Vector31();

        // Coordinates of the node's origin relative to itself.
        v.elements[0] = 0;
        v.elements[1] = 0;
        v.elements[2] = 1;

        // Transformation matrix for the new frame relative to previous.
        double theta = angleFix(node.angleFromPrev);
        node.transMatrix.elements[0][0] = Math.cos(theta);
        node.transMatrix.elements[0][1] = -Math.sin(theta);
        node.transMatrix.elements[0][2] = linkSize * Math.cos(theta);
        node.transMatrix.elements[1][0] = Math.sin(theta);
        node.transMatrix.elements[1][1] = Math.cos(theta);
        node.transMatrix.elements[1][2] = linkSize * Math.sin(theta);
        node.transMatrix.elements[2][0] = 0;
        node.transMatrix.elements[2][1] = 0;
        node.transMatrix.elements[2][2] = 1;

        // Post-multiply previous matrix to get to reference frame (first link).
        node.transMatrix = prev.transMatrix.postMultiply(node.transMatrix);

        // Now multiply by vector to get coordinates in ref frame.
        Vector31 u = node.transMatrix.multiply(v);
        if ((useTracing) && (node.ID == numNodes - 1)) {
            // Add tracing linesegments.
            Line2D.Double segment = new Line2D.Double(node.x, node.y, u.elements[0], u.elements[1]);
            lineSegments.add(segment);
        }

        // Now change location.
        node.x = u.elements[0];
        node.y = u.elements[1];

	/*
	if (node.ID==2) {
	    double th_prev = prev.angleFromPrev;
	    System.out.println (">> th=" + toDegrees(theta) + " th_prev=" + toDegrees(th_prev) + " x=" + node.x + " y=" + node.y);
	    System.out.println (node.transMatrix);
	    }*/
    }


    ////////////////////////////////////////////////////////////////////////
    // public static methods - for use by controllers

    public static double getX(int link) {
        return simulator.nodes.get(link + 1).x;
    }


    public static double getY(int link) {
        return simulator.nodes.get(link + 1).y;
    }


    public static double getAngle(int link) {
        return simulator.angleFix(simulator.nodes.get(link + 1).angleFromPrev);
    }

    public static double getAngleDegrees(int link) {
        return (360.0 * getAngle(link) / (2 * Math.PI));
    }

    public static double getLinkLength(int link) {
        return simulator.linkSize;
    }

    public static void drawLine(double x1, double y1, double x2, double y2) {
        simulator.lineSegments.add(new Line2D.Double(x1, y1, x2, y2));
    }


    public static double getAngle(double x1, double y1, double x2, double y2) {
        return simulator.angleFix(Math.atan2(y2 - y1, x2 - x1));
    }


    public static double getCurrentTime() {
        return simulator.getTime();
    }


    ////////////////////////////////////////////////////////////////////////
    // Animation and simulation

    void reset() {
        try {
            int nlinks = Integer.parseInt(numLinksField.getText().trim());
            numLinks = nlinks;
        } catch (Exception e) {
        }

        numNodes = numLinks + 1;

        initializeArmAndObjects();
        int index = modelBox.getSelectedIndex();
        if (index == 0) {
            // displacement
            isTorque = false;
        } else {
            // torque
            isTorque = true;
        }

        time = 0;
        lineSegments = new LinkedList<Line2D.Double>();
        isPaused = false;
        stopAnimationThread();

        // Load controller.
        String className = "";
        try {
            className = controllerField.getText().trim();
            if (className.length() > 0) {
                armController = (ArmController) (Class.forName(className)).newInstance();
                msg = " Controller loaded";
            }
            armController.init(numLinks, isTorque);
        } catch (Exception e) {
            System.out.println(e);
            msg = "ERROR: Could not load or instantiate controller: " + className;
        }

        this.repaint();
    }


    void stopAnimationThread() {
        if (currentThread != null) {
            currentThread.interrupt();
            currentThread = null;
        }
    }


    double getTime() {
        return time;
    }


    void go() {
        if (isPaused) {
            isPaused = false;
            return;
        }

        stopAnimationThread();    // To ensure only one thread.

        currentThread = new Thread() {
            public void run() {
                animate();
            }
        };
        currentThread.start();
    }


    void pause() {
        isPaused = true;
    }


    void animate() {
        while (true) {

            if (!isPaused) {
                boolean done = nextStep();
                if (done) {
                    System.out.println("DONE!");
                    break;
                }
            }

            time++;
            this.repaint();

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                break;
            }
        } //endwhile

        this.repaint();
    }


    boolean nextStep() {
        // Call controller to get change of displacement or torque's and apply.

        double[] controls = new double[numLinks];
        if (!isTorque) {
            // Displacement
            controls = armController.getDeltaAngles();
            applyDisplacement(controls);
        } else {
            controls = armController.getTorques();
            applyTorque(controls);
        }

        // Check whether allowed and whether to stop.
        // For each link of arm, test against axes and obstacles.
        // Also test angle so that it's less than 360 overall.
        boolean isValid = testValidity();

        if (isValid) {
            return false;
        }
        return true;
    }


    void applyDisplacement(double[] controls) {
        for (int i = 0; i < controls.length; i++) {
            delTheta[i] = controls[i];
        }

        // Change angleFromPrev.
        //printAngles ("Old angles: ");
        for (int i = 1; i < numNodes; i++) {
            ArmSimulatorNode node = nodes.get(i);
            node.angleFromPrev += delTheta[i - 1];
            node.angleFromPrev = angleFix(node.angleFromPrev);
        }
        //printAngles ("New angles: ");
    }

    void printAngles(String prompt) {
        System.out.print(prompt);
        for (int i = 1; i < numNodes; i++) {
            ArmSimulatorNode node = nodes.get(i);
            System.out.print("  " + toDegrees(node.angleFromPrev));
        }
        System.out.println();
    }

    void applyTorque(double[] torque) {
    }


    boolean testValidity() {
        boolean valid = true;

        // Check change-of-angle limit, and link collisions
        for (int i = 0; i < numLinks; i++) {
            if (delTheta[i] > maxDelTheta) {
                numIllegalMoves++;
                msg = "Max-Angle-Change Exceeded: # illegal moves=" + numIllegalMoves;
                System.out.println(msg);
                valid = false;
            }
        }

        for (int i = 0; i < numLinks; i++) {
            ArmSimulatorNode node = nodes.get(i + 1);
            if ((Math.abs(node.angleFromPrev - Math.PI) < 0.02) ||
                    (Math.abs(node.angleFromPrev + Math.PI) < 0.02)) {
                // Too close to 180 or 0. Note: 0.02 rad is approx 1 deg.
                numIllegalMoves++;
                msg = "Links too close: angleFromPrev=" + node.angleFromPrev + " # illegal moves=" + numIllegalMoves;
                System.out.println(msg);
                valid = false;
            }
        }

        // Compute (x,y) of each of the nodes.
        forwardKinematics();

        // Check each link.
        for (int i = 1; i < numNodes; i++) {
            ArmSimulatorNode curr = nodes.get(i);
            if (wrongQuadrant(curr)) {
                // handle wrong quadrant.
                msg = "Wrong quadrant: node=" + i + " x=" + curr.x + " y=" + curr.y + " # illegal moves=" + numIllegalMoves;
                System.out.println(msg);
                valid = false;
            }
            ArmSimulatorNode prev = nodes.get(i - 1);
            if (obstacleCollision(prev, curr)) {
                // handle obstacle collision
                msg = "Obstacle collision: # illegal moves=" + numIllegalMoves;
                System.out.println(msg);
                valid = false;
            }
        }

        return valid;
    }

    boolean wrongQuadrant(ArmSimulatorNode node) {
        if ((node.x < -0.2) || (node.y < -0.2)) {
            numIllegalMoves++;
            return true;
        }
        return false;
    }


    boolean obstacleCollision(ArmSimulatorNode prev, ArmSimulatorNode curr) {
        if (!useObstacles) {
            return false;
        }
        // We're using Java to check intersection and so, will need Java coords.
        Dimension D = this.getSize();
        Line2D.Double L = new Line2D.Double(prev.x, D.height - prev.y, curr.x, D.height - curr.y);
        int numIntersections = 0;
        for (Rectangle2D.Double R : obstacles) {
            // See if R intersects line.
            Rectangle2D.Double Rjava = new Rectangle2D.Double(R.x, D.height - R.y, R.width, R.height);

            if (Rjava.intersectsLine(L)) {
                numIntersections++;
            }
        }

        if (numIntersections > 0) {
            numIllegalMoves++;
            return true;
        }
        return false;
    }

    double angleFix(double a) {
        // Make each angle an angle between 0 and 2*PI.
        //** Note: this code can be optimized.
        if (a < 0) {
            while (a < 0) {
                a = a + 2 * Math.PI;
            }
        } else if (a > 2 * Math.PI) {
            while (a > 2 * Math.PI) {
                a = a - 2 * Math.PI;
            }
        }
        return a;
    }


    double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }


    ////////////////////////////////////////////////////////////////////////
    // GUI construction

    JPanel makeBottomPanel() {
        JPanel panel = new JPanel();

        panel.setLayout(new GridLayout(2, 1));
        panel.add(makeFirstLayer());
        panel.add(makeSecondLayer());
        return panel;
    }

    JPanel makeFirstLayer() {
        JPanel panel = new JPanel();

        panel.add(new JLabel("#Links: "));
        numLinksField = new JTextField(2);
        numLinksField.setText("2");
        panel.add(numLinksField);

        panel.add(new JLabel("  "));

        obstacleButton.addItemListener(
                new ItemListener() {
                    public void itemStateChanged(ItemEvent a) {
                        setObstacles();
                    }
                }
        );
        panel.add(obstacleButton);

        panel.add(new JLabel("   "));
        tracingButton.addItemListener(
                new ItemListener() {
                    public void itemStateChanged(ItemEvent a) {
                        setTracing();
                    }
                }
        );
        panel.add(tracingButton);

        panel.add(new JLabel("   "));
        modelBox = new JComboBox(models);
        panel.add(modelBox);

        return panel;
    }

    JPanel makeSecondLayer() {
        JPanel panel = new JPanel();

        panel.add(new JLabel("Controller: "));
        controllerField = new JTextField(10);
        controllerField.setText("TwoLinkController");
        panel.add(controllerField);

        panel.add(new JLabel("   "));
        JButton resetB = new JButton("Reset");
        resetB.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        reset();
                    }
                }
        );
        panel.add(resetB);

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
        JButton pauseB = new JButton("Pause");
        pauseB.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        pause();
                    }
                }
        );
        panel.add(pauseB);

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
        frame.setSize(900, 700);
        frame.setTitle("Arm Simulator");
        Container cPane = frame.getContentPane();
        cPane.add(makeBottomPanel(), BorderLayout.SOUTH);
        cPane.add(this, BorderLayout.CENTER);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    ////////////////////////////////////////////////////////////////////////
    // Main

    public static void main(String[] argv) {
        simulator = new ArmSimulator();
        simulator.makeFrame();
    }

}


// A simple 3x1 vector and 3x3 matrix

class Vector31 {
    double[] elements = new double[3];
}

class Matrix33 {
    double[][] elements = new double[3][3];

    public Matrix33() {
        // Make default the identity so that matrix multiply works
        // for the first one.
        elements[0][0] = 1;
        elements[1][1] = 1;
        elements[2][2] = 1;
    }

    Vector31 multiply(Vector31 v) {
        Vector31 u = new Vector31();
        for (int i = 0; i < 3; i++) {
            // u[i] is the i-th row of matrix times the input vector
            double sum = 0;
            for (int j = 0; j < 3; j++) {
                sum += elements[i][j] * v.elements[j];
            }
            u.elements[i] = sum;
        }
        return u;
    }

    Matrix33 postMultiply(Matrix33 M) {
        Matrix33 result = new Matrix33();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // i,j-th element of result is row i of this times column j of that
                result.elements[i][j] = 0;
                for (int k = 0; k < 3; k++) {
                    result.elements[i][j] += this.elements[i][k] * M.elements[k][j];
                }
            }
        }

        return result;
    }

    public String toString() {
        String s = "Matrix: \n";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                s += " " + elements[i][j];
            }
            s += "\n";
        }
        return s;
    }

}
