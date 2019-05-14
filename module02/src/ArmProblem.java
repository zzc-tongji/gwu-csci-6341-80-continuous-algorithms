// ArmProblem.java
//
// Author: Rahul Simha
// Jan, 2008.
//
// This class implements the problem spec for a planning problem.
// The arm has fixed-size links. The goal is to make the end of the
// arm reach a target destination. This problem is a little more
// complicated because it needs to be discretized: comparisons
// need a tolerance, for example.

import java.util.*;
import java.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;


public class ArmProblem extends JPanel implements PlanningProblem {

    // Delta = the change for a new (neighboring) state.
    static double delta = 5;  // 5 pixels;

    // For equality comparisons.
    static double epsilon = 0.01;

    // Radius of each joint.
    static int radius = 10;

    // Number of joints, including tip.
    int numNodes = 3;

    // Distance between joints.
    double linkSize = 150;

    // GUI variables.
    JLabel status;
    JTextField obsXField = new JTextField(4);
    JTextField obsYField = new JTextField(4);
    JTextField targetXField = new JTextField(4);
    JTextField targetYField = new JTextField(4);

    // Target location.
    double targetX = 250, targetY = 30;

    // Obstacle: a single rectangle.
    double obstacleX = 200, obstacleY = 200;
    double obsWidth = 100;
    double obsHeight = 100;


    // Plan info.
    ArmState currentState;
    LinkedList<State> plan;
    Iterator<State> planIterator;


    public ArmProblem(JLabel status) {
        this.status = status;

        // Make the nodes and place them in their initial positions.
        // Note: the  positions satisfy the link size.
        reset();
    }


    void reset() {
        currentState = new ArmState(numNodes, linkSize);

        // Fix the initial location.
        double heightOffset = linkSize * Math.sin(2 * Math.PI * 15.0 / 360.0);
        double xOffset = linkSize * Math.cos(2 * Math.PI * 15.0 / 360.0);
        for (int i = 0; i < numNodes; i++) {
            double x = 0, y = 0;
            if (i % 2 == 0) {
                x = 0;
            } else {
                x = xOffset;
            }
            y = i * heightOffset;
            currentState.setNode(i, x, y);
        }

    }


    //////////////////////////////////////////////////////////////////////
    // Screen work


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension D = this.getSize();

        // Background.
        g.setColor(Color.white);
        g.fillRect(0, 0, D.width, D.height);

        // Draw nodes (joints).
        int prevX = -1, prevY = -1;
        for (int i = 0; i < numNodes; i++) {
            int x = (int) currentState.getX(i);
            int y = D.height - (int) currentState.getY(i);
            g.setColor(Color.blue);
            g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
            g.setColor(Color.gray);
            if (prevX >= 0) {
                g.drawLine(prevX, prevY, x, y);
            }
            prevX = x;
            prevY = y;
        }

        // Draw obstacle.
        g.setColor(Color.red);
        g.fillRect((int) obstacleX, D.height - (int) obstacleY, (int) obsWidth, (int) obsHeight);

        // Target.
        g.setColor(Color.green);
        int x = (int) targetX;
        int y = D.height - (int) targetY;
        g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
    }


    //////////////////////////////////////////////////////////////////////
    // GUI construction


    public JPanel getFullPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(this, BorderLayout.CENTER);
        panel.add(makeBottomPanel(), BorderLayout.SOUTH);
        return panel;
    }


    JPanel makeBottomPanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("<html><b>Obstacle</b></html>: "));
        panel.add(new JLabel(" X:"));
        panel.add(obsXField);
        obsXField.setText("" + obstacleX);
        panel.add(new JLabel(" Y:"));
        panel.add(obsYField);
        obsYField.setText("" + obstacleY);


        panel.add(new JLabel("     "));
        panel.add(new JLabel("<html><b>Target</b></html>: "));
        panel.add(new JLabel(" X:"));
        panel.add(targetXField);
        targetXField.setText("" + targetX);
        panel.add(new JLabel(" Y:"));
        panel.add(targetYField);
        targetYField.setText("" + targetY);

        panel.add(new JLabel("     "));
        JButton changeB = new JButton("Change");
        changeB.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        change();
                    }
                }
        );
        panel.add(changeB);
        return panel;
    }


    //////////////////////////////////////////////////////////////////////
    // GUI events


    void change() {
        try {
            double obsX = Double.parseDouble(obsXField.getText());
            double obsY = Double.parseDouble(obsYField.getText());
            double tX = Double.parseDouble(targetXField.getText());
            double tY = Double.parseDouble(targetYField.getText());
            // Copy into originals after attempting to read.
            obstacleX = obsX;
            obstacleY = obsY;
            targetX = tX;
            targetY = tY;
            reset();
            this.repaint();
        } catch (NumberFormatException e) {
            System.out.println(e);
        }
    }


    //////////////////////////////////////////////////////////////////////
    // Problem interface implementations.


    public void drawState(State state) {
        // Draw a light gray circle.
        ArmState a = (ArmState) state;
        Dimension D = this.getSize();
        Graphics g = this.getGraphics();
        g.setColor(Color.lightGray);
        int drawX = (int) a.getX(numNodes - 1);
        int drawY = D.height - (int) a.getY(numNodes - 1);
        int r = 3;
        g.fillOval(drawX - r, drawY - r, 2 * r, 2 * r);
    }


    public ArrayList<State> getNeighbors(State state) {
        ArmState a = (ArmState) state;
        ArrayList<State> neighbors = new ArrayList<State>();

        // Look at the 8 neighboring points at distance (+- delta, +-delta).
        for (int i = numNodes - 1; i > 1; i--) {
            for (double delX = -delta; delX <= delta; delX += delta) {
                for (double delY = -delta; delY <= delta; delY += delta) {
                    ArmState b = makeState(a, i, delX, delY);
                    if (isValid(b)) {
                        b.costFromStart = a.costFromStart + delta;
                        b.estimatedCostToGoal = b.tipDistance(targetX, targetY);
                        neighbors.add(b);
                    }
                }
            }
        }

        return neighbors;
    }


    public State getStartState() {
        return currentState;
    }


    public boolean satisfiesGoal(State state) {
        ArmState a = (ArmState) state;
        double x = a.getX(numNodes - 1);
        double y = a.getY(numNodes - 1);
        // Use a distance of at least delta.
        if ((Math.abs(targetX - x) > delta + epsilon) || (Math.abs(targetY - y) > delta + epsilon)) {
            return false;
        }
        return true;
    }


    public void setPlan(LinkedList<State> plan) {
        this.plan = plan;
        currentState = null;
        if (plan == null) {
            status.setText("No solution found by planner");
            return;
        }
        planIterator = plan.iterator();
    }


    public void next() {
        if ((planIterator == null) || (!planIterator.hasNext())) {
            status.setText("No more states left in plan");
            return;
        }

        ArmState state = (ArmState) planIterator.next();
        if (!isValid(state)) {
            status.setText("ERROR: not a valid state");
        }

        if (currentState != null) {
            // Check if neighbors.
            if (!areNeighbors(currentState, state)) {
                status.setText("ERROR: not a neighboring state");
            }
        }
        currentState = state;

        this.repaint();
    }


    //////////////////////////////////////////////////////////////////////
    // Utility methods


    boolean isValid(ArmState a) {
        if (a == null) {
            return false;
        }

        // See if within bounds.
        for (int i = 0; i < numNodes; i++) {
            double x = a.getX(i);
            double y = a.getY(i);
            if ((x < 0) || (y < 0)) {
                return false;
            }

        }

        // See if the new state hits the obstacle.
        Dimension D = this.getSize();
        Rectangle2D.Double R = new Rectangle2D.Double(obstacleX, D.height - obstacleY, obsWidth, obsHeight);
        for (int i = 0; i < numNodes - 1; i++) {
            double x1 = a.getX(i);
            double y1 = a.getY(i);
            double x2 = a.getX(i + 1);
            double y2 = a.getY(i + 1);
            Line2D.Double L = new Line2D.Double(x1, D.height - y1, x2, D.height - y2);
            if (R.intersectsLine(L)) {
                return false;
            }
        }

        //** Note: we have not checked whether a link crosses the
        // rest of the arm.

        return true;
    }


    boolean areNeighbors(ArmState a1, ArmState a2) {
        if ((a1 == null) || (a2 == null)) {
            return false;
        }

        // See if distance of last node is within delta+epsilon.
        double x1 = a1.getX(numNodes - 1);
        double y1 = a1.getY(numNodes - 1);
        double x2 = a2.getX(numNodes - 1);
        double y2 = a2.getY(numNodes - 1);

        double d = distance(x1, y1, x2, y2);
        if (d <= delta + epsilon) {
            return true;
        }

        return false;
    }


    double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }


    ArmState copy(ArmState a) {
        // Make a new instance that's a copy of a.
        ArmState b = new ArmState(numNodes, linkSize);
        for (int n = 0; n < numNodes; n++) {
            ArmStateNode aNode = a.nodes.get(n);
            ArmStateNode bNode = b.nodes.get(n);
            bNode.x = aNode.x;
            bNode.y = aNode.y;
        }
        return b;
    }


    public ArmState makeState(ArmState a, int n, double deltaX, double deltaY) {
        if (n <= 1) {
            return null;      // Can't move anchor or next node.
        }
        ArmState b = copy(a);
        b.parent = a;

        // Move node n and all later ones up by same amount.
        for (int k = n; k < numNodes; k++) {
            ArmStateNode bNode = b.nodes.get(k);
            bNode.x = bNode.x + deltaX;
            bNode.y = bNode.y + deltaY;
        }

        // Now use fixed distances for nodes below, except for nodes 0, 1.
        for (int k = n - 1; k >= 2; k--) {
            ArmStateNode bNode = b.nodes.get(k);
            ArmStateNode bPrev = b.nodes.get(k + 1);
            double d = distance(bPrev.x, bPrev.y, bNode.x, bNode.y);
            double xDiff = bPrev.x - bNode.x;
            double yDiff = bPrev.y - bNode.y;
            double moveX = xDiff * (d - linkSize) / d;
            double moveY = yDiff * (d - linkSize) / d;
            bNode.x = bNode.x + moveX;
            bNode.y = bNode.y + moveY;
        }

        // Now handle the case of ArmStateNode 1, which is at one of
        // the intersection points of the circle from 0, and
        // the circle from ArmStateNode 2.
        ArmStateNode zeroNode = b.nodes.get(0);
        ArmStateNode oneNode = b.nodes.get(1);
        ArmStateNode twoNode = b.nodes.get(2);

        Point2D.Double[] points = intersectCircles(zeroNode.x, zeroNode.y, linkSize, twoNode.x, twoNode.y, linkSize);
        if (points == null) {
            return null;
        }

        // Otherwise, find the closer one.
        Point2D.Double closerPoint = points[0];
        if (distance(oneNode, points[1]) < distance(oneNode, points[0])) {
            closerPoint = points[1];
        }
        // Move oneNode to closer point.
        ArmStateNode bOneNode = b.nodes.get(1);
        bOneNode.x = closerPoint.x;
        bOneNode.y = closerPoint.y;

        return b;
    }


    // Find intersection points of two circles.
    // See http://local.wasp.uwa.edu.au/~pbourke/geometry/2circle/
    // Let P0=(x0,y0) be the center of one, and P1=(x1,y1) be the other.
    // Define P2=(x2,y2) as the intersection between the radial 
    // connector and the perp bisector.

    Point2D.Double[] intersectCircles(double x0, double y0, double r0, double x1, double y1, double r1) {
        double d = distance(x0, y0, x1, y1);
        if (d > r0 + r1) {
            // No intersection points.
            //System.out.println ("No intersection: apart");
            return null;
        }
        if (d < Math.abs(r0 - r1)) {
            // One circle is contained within another.
            //System.out.println ("No intersection: containment");
            return null;
        }

        // Compute a = distance from (x0,y0) to perp bisector.
        double a = (d * d + r0 * r0 - r1 * r1) / (2 * d);
        // Similarly b = distance from (x1,y1) to perp bisector.
        double b = (d * d - r0 * r0 + r1 * r1) / (2 * d);
        // h = height from radial line to intersection point.
        double h = Math.sqrt(r0 * r0 - a * a);
        // Now, the coordinates of P2, using P2=P0+(a/d)*(P1-P0) (vectors).
        double x2 = x0 + (a / d) * (x1 - x0);
        double y2 = y0 + (a / d) * (y1 - y0);
        Point2D.Double p3_1 = new Point2D.Double();
        p3_1.x = x2 - (h / d) * (y1 - y0);
        p3_1.y = y2 + (h / d) * (x1 - x0);
        Point2D.Double p3_2 = new Point2D.Double();
        p3_2.x = x2 + (h / d) * (y1 - y0);
        p3_2.y = y2 - (h / d) * (x1 - x0);
        Point2D.Double[] points = new Point2D.Double[2];
        points[0] = p3_1;
        points[1] = p3_2;
        double d01 = distance(x0, y0, p3_1.x, p3_1.y);
        double d02 = distance(x0, y0, p3_2.x, p3_2.y);
        double d11 = distance(x1, y1, p3_1.x, p3_1.y);
        double d12 = distance(x1, y1, p3_2.x, p3_2.y);
        //System.out.println ("Circle intersection: " + points[0] + "  " + points[1] + "    a=" + a + " b=" + b + " h=" + h + " d01=" + d01 + " d11=" + d11 + " d02=" + d02 + " d12=" + d12);
        return points;
    }


    double distance(ArmStateNode n, Point2D.Double p) {
        return distance(n.x, n.y, p.x, p.y);
    }


    double distance(Point2D.Double p, Point2D.Double q) {
        return distance(q.x, q.y, p.x, p.y);
    }

}
