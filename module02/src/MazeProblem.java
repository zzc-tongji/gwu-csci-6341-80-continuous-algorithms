// MazeProblem.java
//
// Author: Rahul Simha
// Jan, 2008.
//
// This class implements the problem spec for a planning problem.
// A maze is a 2D grid with some cells blacked out, like in a crossword.
// There are two special states: start, end. The goal is to find a 
// valid path from start to end using only N,W,E,W moves within
// valid cells.


import java.util.*;
import java.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;


public class MazeProblem extends JPanel implements PlanningProblem {

    // Density of black cells.
    static double density = 0.3;

    // GUI variables.
    JTextField nField = new JTextField(3);
    JTextField sxField = new JTextField(3);
    JTextField syField = new JTextField(3);
    JTextField exField = new JTextField(3);
    JTextField eyField = new JTextField(3);
    JLabel status;

    // Square maze.
    int N = 10;

    // Default starting point.
    int currentX = 1, currentY = 2;

    // Default end point.
    int endX = 3, endY = 4;


    // Keep track of current state.
    MazeState currentState = null;

    // Data structure for maze.
    int[][] grid;

    // Plan info.
    LinkedList<State> plan;
    Iterator<State> planIterator;


    public MazeProblem(JLabel status) {
        this.status = status;
    }


    //////////////////////////////////////////////////////////////////////
    // Screen work

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension D = this.getSize();

        // Background.
        g.setColor(Color.white);
        g.fillRect(0, 0, D.width, D.height);

        if (grid == null) {
            return;
        }

        g.setColor(Color.black);
        int cellSize = (D.height - N) / N;
        for (int i = 0; i < N; i++) {
            int x = (i + 1) * cellSize;
            for (int j = 0; j < N; j++) {
                int y = (j + 1) * cellSize;
                if (grid[i][j] == 0) {
                    g.drawRect(x, D.height - y, cellSize, cellSize);
                } else {
                    g.fillRect(x, D.height - y, cellSize, cellSize);
                }
            }
        }

        // End.
        g.setColor(Color.green);
        drawDot(g, endX, endY);

        // Current.
        g.setColor(Color.blue);
        drawDot(g, currentX, currentY);

    }

    void drawDot(Graphics g, int x, int y) {
        Dimension D = this.getSize();
        int cellSize = (D.height - N) / N;
        int drawX = (x + 1) * cellSize;
        int drawY = (y + 1) * cellSize;
        g.fillOval(drawX + 2, D.height - drawY + 2, cellSize - 4, cellSize - 4);
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
        panel.add(new JLabel("N:"));
        nField.setText("" + N);
        panel.add(nField);
        panel.add(new JLabel("  startX:"));
        panel.add(sxField);
        sxField.setText("" + currentX);
        panel.add(new JLabel("startY:"));
        panel.add(syField);
        syField.setText("" + currentY);
        panel.add(new JLabel("endX:"));
        panel.add(exField);
        exField.setText("" + endX);
        panel.add(new JLabel("endY:"));
        panel.add(eyField);
        eyField.setText("" + endY);

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

        panel.add(new JLabel("     "));
        JButton genB = new JButton("Generate");
        genB.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        generate();
                    }
                }
        );
        panel.add(genB);
        return panel;
    }


    //////////////////////////////////////////////////////////////////////
    // GUI events


    // The change button.

    void change() {
        try {
            int n = Integer.parseInt(nField.getText());
            int sx = Integer.parseInt(sxField.getText());
            int sy = Integer.parseInt(syField.getText());
            int ex = Integer.parseInt(exField.getText());
            int ey = Integer.parseInt(eyField.getText());
            // Note: set the values afterwards, in case an exception is thrown.
            N = n;
            currentX = sx;
            currentY = sy;
            endX = ex;
            endY = ey;
            this.repaint();
        } catch (NumberFormatException e) {
            System.out.println(e);
        }
    }


    // The generate button.

    void generate() {
        grid = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {

                int gridPoint = 0;

                if (UniformRandom.uniform() < density) {
                    gridPoint = 1;
                }

                // grid[i][j]==1 => blacked out.
                grid[i][j] = gridPoint;

                // Leave start and end blank. NOTE: row=y, col=x.
                if ((i == currentY) && (j == currentX)) {
                    grid[i][j] = 0;
                }
                if ((i == endY) && (j == endX)) {
                    grid[i][j] = 0;
                }

            }
        }

        this.repaint();
    }


    //////////////////////////////////////////////////////////////////////
    // Problem interface implementations.


    public State getStartState() {
        return new MazeState(null, N, currentX, currentY);
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


    public ArrayList<State> getNeighbors(State state) {
        MazeState m = (MazeState) state;
        ArrayList<State> neighbors = new ArrayList<State>();

        // North
        if (isValid(m.x, m.y + 1)) {
            MazeState m2 = new MazeState(m, m.N, m.x, m.y + 1);
            m2.costFromStart = m.costFromStart + 1;
            m2.estimatedCostToGoal = goalCost(m2.x, m2.y);
            neighbors.add(m2);
        }

        // South.
        if (isValid(m.x, m.y - 1)) {
            MazeState m2 = new MazeState(m, m.N, m.x, m.y - 1);
            m2.costFromStart = m.costFromStart + 1;
            m2.estimatedCostToGoal = goalCost(m2.x, m2.y);
            neighbors.add(m2);
        }

        // East.
        if (isValid(m.x + 1, m.y)) {
            MazeState m2 = new MazeState(m, m.N, m.x + 1, m.y);
            m2.costFromStart = m.costFromStart + 1;
            m2.estimatedCostToGoal = goalCost(m2.x, m2.y);
            neighbors.add(m2);
        }

        // West.
        if (isValid(m.x - 1, m.y)) {
            MazeState m2 = new MazeState(m, m.N, m.x - 1, m.y);
            m2.costFromStart = m.costFromStart + 1;
            m2.estimatedCostToGoal = goalCost(m2.x, m2.y);
            neighbors.add(m2);
        }

        return neighbors;
    }


    public boolean satisfiesGoal(State state) {
        MazeState m = (MazeState) state;
        if ((m.x == endX) && (m.y == endY)) {
            return true;
        }
        return false;
    }


    public void drawState(State state) {
        MazeState m = (MazeState) state;
        Graphics g = this.getGraphics();
        g.setColor(Color.lightGray);
        drawDot(g, m.x, m.y);
    }


    // Everytime the "next" button is clicked, draw the next
    // state in the most recent plan.

    public void next() {
        if ((planIterator == null) || (!planIterator.hasNext())) {
            status.setText("No more states left in plan");
            return;
        }

        MazeState state = (MazeState) planIterator.next();

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

        this.currentX = state.x;
        this.currentY = state.y;
        this.repaint();
    }


    //////////////////////////////////////////////////////////////////////
    // Utility methods


    boolean isValid(MazeState m) {
        if (!isValid(m.x, m.y)) {
            return false;
        }
        return true;
    }


    boolean isValid(int x, int y) {
        if ((x < 0) || (y < 0) || (x >= N) || (y >= N)) {
            return false;
        }
        if (grid[x][y] == 1) {
            return false;
        }
        return true;
    }


    boolean areNeighbors(MazeState m1, MazeState m2) {
        // North.
        if ((m2.x == m1.x) && (m2.y == m1.y + 1)) {
            return true;
        }

        // South
        if ((m2.x == m1.x) && (m2.y == m1.y - 1)) {
            return true;
        }

        // East
        if ((m2.x == m1.x + 1) && (m2.y == m1.y)) {
            return true;
        }

        // West
        if ((m2.x == m1.x - 1) && (m2.y == m1.y)) {
            return true;
        }

        return false;
    }


    double goalCost(int x, int y) {
        double d = Math.sqrt((endX - x) * (endX - x) + (endY - y) * (endY - y));
        return d;
    }

}
