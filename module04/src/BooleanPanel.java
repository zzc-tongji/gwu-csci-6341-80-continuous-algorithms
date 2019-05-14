import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;
import java.text.*;


// A small class used to represent each node in the Boolean network.

class Node {
    int id;                           // id for drawing.
    int x, y;                          // x,y location on GUI.
    boolean stateOn = false;            // Current state.
    boolean nextStateOn = false;        // Temp variable for next state.
    int input1 = -1;                    // Index of first input node.
    int input2 = -1;                    // Index of other one.
    int function = BooleanPanel.NONE;   // Which boolean function?
}


public class BooleanPanel extends JPanel implements MouseListener {

    public static final int TEST = 1;
    public static final int CELL_CYCLE = 2;

    // Boolean functions used in cell-cycle model of S.Huang & D.E.Ingber.
    // See: Huang and Ingber, Expt. Cell Research, Vol. 261, pp.91-103,2000.
    public static final int NONE = 0;
    public static final int AND = 1;
    public static final int OR = 2;
    public static final int NAND = 3;
    public static final int NOTIF = 4;
    public static final int IMPLIES = 5;

    JLabel statusLabel;           // A bar on top for messages.
    JPanel drawPanel;             // Used for drawing network.

    // Store node instances.
    Vector nodes;

    // 9 nodes in model, plus two more to represent for two signals.
    int numNodes = -1;

    // Radius of circle to draw.
    int radius = 20;

    int model = CELL_CYCLE;


    //------------------------------------------------------------------
    // Constructor

    public BooleanPanel(int model) {
        this.model = model;
        if (model == TEST) {
            numNodes = 4;
        } else {
            numNodes = 11;
        }

        this.setLayout(new BorderLayout());

        // Status bar:
        Border border = BorderFactory.createLineBorder(Color.black);
        statusLabel = new JLabel(" ");
        statusLabel.setBorder(border);
        this.add(statusLabel, BorderLayout.NORTH);

        drawPanel = new JPanel();
        drawPanel.addMouseListener(this);
        this.add(drawPanel, BorderLayout.CENTER);
        this.add(makeBottomPanel(), BorderLayout.SOUTH);
    }


    //------------------------------------------------------------------
    // Screen updates

    public void status(String msg) {
        statusLabel.setForeground(Color.black);
        statusLabel.setText(msg);
    }

    public void error(String str) {
        statusLabel.setForeground(Color.red);
        statusLabel.setText("  " + str);
    }


    //------------------------------------------------------------------
    // GUI construction

    JPanel makeBottomPanel() {
        JPanel panel = new JPanel();

        JButton startB = new JButton("Start");
        startB.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        start();
                    }
                }
        );
        panel.add(startB);

        JButton nextB = new JButton("Next");
        nextB.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        nextState();
                    }
                }
        );
        panel.add(nextB);

        return panel;
    }


    //------------------------------------------------------------------
    // Simulation/iteration.

    void start() {
        Dimension D = drawPanel.getSize();

        // Draw nodes in a circular arrangement.
        int cx = D.width / 2;
        int cy = D.height / 2;
        int r = 3 * D.width / 8;
        if (D.height > D.width) {
            r = 3 * D.height / 8;
        }

        // Angle subtended by each node from center.
        double theta = 2.0 * Math.PI / (double) numNodes;

        // Create the nodes.
        nodes = new Vector();
        for (int k = 0; k < numNodes; k++) {
            Node node = new Node();
            node.id = k;
            node.x = cx + (int) (r * Math.cos(k * theta));
            node.y = cy + (int) (r * Math.sin(k * theta));
            nodes.add(node);
        }

        if (model == TEST) {
            testModel();
        } else {
            cellCycleModel();
        }

        redraw();
    }


    void cellCycleModel() {
        // Node functions.
        Node node = (Node) nodes.get(0);
        node.function = NOTIF;

        node = (Node) nodes.get(1);
        node.function = NOTIF;

        node = (Node) nodes.get(2);
        node.function = IMPLIES;

        node = (Node) nodes.get(3);
        node.function = AND;

        node = (Node) nodes.get(4);
        node.function = IMPLIES;

        node = (Node) nodes.get(5);
        node.function = NOTIF;

        node = (Node) nodes.get(6);
        node.function = NAND;

        node = (Node) nodes.get(7);
        node.function = NOTIF;

        node = (Node) nodes.get(8);
        node.function = NOTIF;

        node = (Node) nodes.get(9);
        node.function = NONE;

        node = (Node) nodes.get(10);
        node.function = NONE;

        // Network structure:
        node = (Node) nodes.get(0);
        node.input2 = 7;
        node.input1 = 9;

        node = (Node) nodes.get(1);
        node.input1 = 0;
        node.input2 = 2;

        node = (Node) nodes.get(2);
        node.input1 = 8;
        node.input2 = 3;

        node = (Node) nodes.get(3);
        node.input1 = 9;
        node.input2 = 10;

        node = (Node) nodes.get(4);
        node.input1 = 5;
        node.input2 = 2;

        node = (Node) nodes.get(5);
        node.input1 = 4;
        node.input2 = 6;

        node = (Node) nodes.get(6);
        node.input1 = 1;
        node.input2 = 4;

        node = (Node) nodes.get(7);
        node.input1 = 5;
        node.input2 = 6;

        node = (Node) nodes.get(8);
        node.input1 = 7;
        node.input2 = 8;

        node = (Node) nodes.get(9);
        node.input1 = -1;
        node.input2 = -1;

        node = (Node) nodes.get(10);
        node.input1 = -1;
        node.input2 = -1;
    }


    void testModel() {
        // Node functions.
        Node node = (Node) nodes.get(0);
        node.function = OR;

        node = (Node) nodes.get(1);
        node.function = OR;

        node = (Node) nodes.get(2);
        node.function = AND;

        node = (Node) nodes.get(3);
        node.function = NOTIF;

        // Network structure:
        node = (Node) nodes.get(0);
        node.input2 = 2;
        node.input1 = 3;

        node = (Node) nodes.get(1);
        node.input1 = 0;
        node.input2 = 1;

        node = (Node) nodes.get(2);
        node.input1 = 0;
        node.input2 = 1;

        node = (Node) nodes.get(3);
        node.input1 = 3;
        node.input2 = 2;
    }


    void redraw() {
        Dimension D = drawPanel.getSize();
        Graphics g = drawPanel.getGraphics();
        g.setFont(new Font("Serif", Font.BOLD, 16));

        for (int k = 0; k < nodes.size(); k++) {

            // Extract each node.
            Node node = (Node) nodes.get(k);

            // Draw arrows first.
            g.setColor(Color.black);
            if (node.input1 >= 0) {
                Node prev1 = (Node) nodes.get(node.input1);
                drawArrow(prev1, node);
            }
            if (node.input2 >= 0) {
                Node prev2 = (Node) nodes.get(node.input2);
                drawArrow(prev2, node);
            }

            // Red for off, blue for on.
            g.setColor(Color.red);
            if (node.stateOn) {
                g.setColor(Color.blue);
            }

            // Draw node.
            int topLeftX = node.x - radius;
            int topLeftY = D.height - node.y - radius;
            g.fillOval(topLeftX, topLeftY, 2 * radius, 2 * radius);

            // Node number.
            g.setColor(Color.black);
            g.drawString("" + k, topLeftX + radius / 2, topLeftY + 4 * radius / 3);

        }

    }


    // Compute the next state.

    void nextState() {

        // First, compute next state and write that into nextStateOn.

        for (int i = 0; i < nodes.size(); i++) {

            // Extract node instance.
            Node node = (Node) nodes.get(i);

            // Default: keep state.
            node.nextStateOn = node.stateOn;

            // If both inputs are well-defined:
            if ((node.input1 >= 0) && (node.input2 >= 0)) {
                // Compute next state.
                Node inputNode1 = (Node) nodes.get(node.input1);
                Node inputNode2 = (Node) nodes.get(node.input2);
                // Various boolean functions.
                if (node.function == AND) {
                    node.nextStateOn = inputNode1.stateOn && inputNode2.stateOn;
                } else if (node.function == OR) {
                    node.nextStateOn = inputNode1.stateOn || inputNode2.stateOn;
                } else if (node.function == NAND) {
                    node.nextStateOn = !(inputNode1.stateOn && inputNode2.stateOn);
                } else if (node.function == NOTIF) {
                    node.nextStateOn = (inputNode1.stateOn) && (!inputNode2.stateOn);
                } else if (node.function == IMPLIES) {
                    node.nextStateOn = !(!inputNode1.stateOn && inputNode2.stateOn);
                }
            } // end-if

        } // end-for

        // Copy over next state into current state. This changes
        // the network state synchronously (in one fell swoop).
        for (int i = 0; i < nodes.size(); i++) {
            Node node = (Node) nodes.get(i);
            node.stateOn = node.nextStateOn;
        }

        redraw();
    }


    //------------------------------------------------------------------
    // Mouse-listening - to flip state.


    public void mouseClicked(MouseEvent e) {
        // Find out if any node got clicked.
        Dimension D = drawPanel.getSize();
        for (int k = 0; k < nodes.size(); k++) {
            Node node = (Node) nodes.get(k);
            int d = (int) distance(node.x, node.y, e.getX(), D.height - e.getY());
            if (d < radius) {
                // Click occured => change state.
                node.stateOn = !node.stateOn;
                break;
            }
        }

        redraw();
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    double distance(int x1, int y1, int x2, int y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }


    //------------------------------------------------------------------
    // Drawing arrows.

    void drawArrow(Node node1, Node node2) {
        Dimension D = drawPanel.getSize();
        Graphics2D g2 = (Graphics2D) drawPanel.getGraphics();
        if (node1.id == node2.id) {
            // Self-loop.
            selfLoop(g2, node1.x + radius, D.height - node1.y, radius);
            return;
        }
        // First compute the end points.
        double theta = Math.atan2(node2.y - node1.y, node2.x - node1.x);
        double thetaDeg = theta * 360.0 / (2 * Math.PI);
        int x1 = (int) (node1.x + radius * Math.cos(theta));
        int y1 = (int) (node1.y + radius * Math.sin(theta));
        int x2 = (int) (node2.x - radius * Math.cos(theta));
        int y2 = (int) (node2.y - radius * Math.sin(theta));
        y1 = D.height - y1;
        y2 = D.height - y2;
        drawArrow(g2, x1, y1, x2, y2, 1f);
    }

    // From: http://forum.java.sun.com/thread.jspa?threadID=378460&tstart=135
    public static void drawArrow(Graphics2D g2d, int xCenter, int yCenter, int x, int y, float stroke) {
        double aDir = Math.atan2(xCenter - x, yCenter - y);
        g2d.drawLine(x, y, xCenter, yCenter);
        // make the arrow head solid even if dash pattern has been specified
        g2d.setStroke(new BasicStroke(1f));
        Polygon tmpPoly = new Polygon();
        int i1 = 12 + (int) (stroke * 2);
        // make the arrow head the same size regardless of the length length
        int i2 = 6 + (int) stroke;
        tmpPoly.addPoint(x, y);
        tmpPoly.addPoint(x + xCor(i1, aDir + .5), y + yCor(i1, aDir + .5));
        tmpPoly.addPoint(x + xCor(i2, aDir), y + yCor(i2, aDir));
        tmpPoly.addPoint(x + xCor(i1, aDir - .5), y + yCor(i1, aDir - .5));
        tmpPoly.addPoint(x, y); // arrow tip
        g2d.drawPolygon(tmpPoly);

        // Remove this line to leave arrow head unpainted:
        g2d.fillPolygon(tmpPoly);
    }

    private static int yCor(int len, double dir) {
        return (int) (len * Math.cos(dir));
    }

    private static int xCor(int len, double dir) {
        return (int) (len * Math.sin(dir));
    }

    void selfLoop(Graphics2D g2, int x, int y, int radius) {
        // First draw a circle, then an arrowhead.
        g2.setColor(Color.black);
        int topLeftX = x;
        int topLeftY = y - radius;
        g2.drawOval(topLeftX, topLeftY, 2 * radius, 2 * radius);
        // Arrowhead at x,y.
        drawArrow(g2, x + 3, y - 3, x, y, 1f);
    }


}
