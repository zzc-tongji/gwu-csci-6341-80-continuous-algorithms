import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;
import java.text.*;

class ArmNode {
    int ID;
    double x, y;
    double nextX, nextY;
}


public class Arm extends JPanel implements MouseInputListener {

    // Store node instances.
    Vector<ArmNode> nodes;
    Vector<Rectangle2D.Double> obstacles;

    ArmNode target;

    // Radius of circle to draw.
    int radius = 10;
    int numNodes = 6;
    double linkSize = 200;
    int currentNode = -1;

    String msg = "";
    int numIllegalMoves = 0;

    public Arm() {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        // Make the nodes and place them in their initial positions.
        // Note: the  positions satisfy the link size.
        nodes = new Vector<ArmNode>();
        double heightOffset = linkSize * Math.sin(2 * Math.PI * 15.0 / 360.0);
        double xOffset = linkSize * Math.cos(2 * Math.PI * 15.0 / 360.0);
        for (int i = 0; i < numNodes; i++) {
            ArmNode node = new ArmNode();
            node.ID = i;
            if (i % 2 == 0) {
                node.x = 0;
            } else {
                node.x = xOffset;
            }
            node.y = i * heightOffset;
            nodes.add(node);
        }

        // The obstacles.
        obstacles = new Vector<Rectangle2D.Double>();
        obstacles.add(new Rectangle2D.Double(260, 300, 140, 50));
        obstacles.add(new Rectangle2D.Double(350, 240, 125, 120));

        // Target.
        target = new ArmNode();
        target.x = 450;
        target.y = 275;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension D = this.getSize();
        g.setColor(Color.white);
        g.fillRect(0, 0, D.width, D.height);

        // Draw nodes.
        int prevX = -1, prevY = -1;
        for (ArmNode node : nodes) {
            int x = (int) node.x;
            int y = D.height - (int) node.y;
            if (node.ID == currentNode) {
                g.setColor(Color.cyan);
            } else {
                g.setColor(Color.blue);
            }
            g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
            g.setColor(Color.gray);
            if (prevX >= 0) {
                g.drawLine(prevX, prevY, x, y);
            }
            prevX = x;
            prevY = y;
        }

        // Draw obstacles.
        g.setColor(Color.red);
        Graphics2D g2 = (Graphics2D) g;
        for (Rectangle2D.Double R : obstacles) {
            Rectangle2D.Double Rjava = new Rectangle2D.Double(R.x, D.height - R.y, R.width, R.height);
            g2.fill(Rjava);
        }

        // Target.
        g.setColor(Color.green);
        int x = (int) target.x;
        int y = D.height - (int) target.y;
        g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);

        // Message.
        g.setColor(Color.black);
        msg = "# Illegal moves: " + numIllegalMoves;
        g.drawString(msg, 20, 20);
    }


    void move(ArmNode node, double nextX, double nextY) {
        // Move this node and all later ones up by same amount.
        double delX = nextX - node.x;
        double delY = nextY - node.y;
        for (int j = node.ID; j < numNodes; j++) {
            // Translate by same amount.
            ArmNode n = nodes.get(j);
            n.nextX = n.x + delX;
            n.nextY = n.y + delY;
        }

        for (int j = node.ID - 1; j >= 0; j--) {
            // Find closest point to circle centered at previous node's center.
            ArmNode n = nodes.get(j);
            ArmNode prev = nodes.get(j + 1);
            double d = distance(prev.nextX, prev.nextY, n.x, n.y);
            double xDiff = prev.nextX - n.x;
            double yDiff = prev.nextY - n.y;
            double moveX = xDiff * (d - linkSize) / d;
            double moveY = yDiff * (d - linkSize) / d;
            n.nextX = n.x + moveX;
            n.nextY = n.y + moveY;
            //System.out.println ("j=" + j + " d=" + d + " xD=" + xDiff + " yD=" + yDiff + " mX=" + moveX + " mY=" + moveY);
        }

        // Check validity here: intersection w/ obstacles.
        Dimension D = this.getSize();
        for (int i = 0; i < nodes.size() - 1; i++) {
            ArmNode n = nodes.get(i);
            ArmNode m = nodes.get(i + 1);
            Line2D.Double L = new Line2D.Double(n.x, D.height - n.y, m.x, D.height - m.y);
            for (Rectangle2D.Double R : obstacles) {
                // See if R intersects line.
                Rectangle2D.Double Rjava = new Rectangle2D.Double(R.x, D.height - R.y, R.width, R.height);

                if (Rjava.intersectsLine(L)) {
                    numIllegalMoves++;
                }
            }
        }


        // Now update.
        for (ArmNode n : nodes) {
            n.x = n.nextX;
            n.y = n.nextY;
        }


    }


    //------------------------------------------------------------------
    // Mouse-listening - to flip state.


    public void mouseDragged(MouseEvent e) {
        Dimension D = this.getSize();
        if (currentNode < 0) {
            return;
        }
        ArmNode node = (ArmNode) nodes.get(currentNode);
        int x = (int) node.x;
        int y = D.height - (int) node.y;
        int d = (int) distance(x, y, e.getX(), e.getY());
        if (d > radius) {
            // Mouse drag occurred too far.
            return;
        }
        move(node, e.getX(), D.height - e.getY());
        this.repaint();
    }

    public void mouseClicked(MouseEvent e) {
        // Find out if any node got clicked.
        Dimension D = this.getSize();
        currentNode = -1;
        for (int k = 0; k < nodes.size(); k++) {
            ArmNode node = (ArmNode) nodes.get(k);
            int x = (int) node.x;
            int y = D.height - (int) node.y;
            int d = (int) distance(x, y, e.getX(), e.getY());
            if (d < radius) {
                // Click occured => change state.
                currentNode = k;
                break;
            }
        }
        this.repaint();
    }

    public void mouseMoved(MouseEvent e) {
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

    double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public static void main(String[] argv) {
        Arm a = new Arm();
        JFrame frame = new JFrame();
        frame.setSize(500, 500);
        Container cPane = frame.getContentPane();
        cPane.add(a);
        frame.setVisible(true);
    }

}
