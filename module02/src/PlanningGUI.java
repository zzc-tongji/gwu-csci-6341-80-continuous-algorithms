// PlanningGUI.java
//
// Author: Rahul Simha
// Jan, 2008
//
// This is the starting point for the planning algorithm demo:
//    java PlanningGUI

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;


public class PlanningGUI extends JFrame {

    // Write text feedback at the top of the frame.
    JLabel status = new JLabel(" ");

    // Other GUI stuff.
    JTabbedPane tabbedPane;
    JTextField algField = new JTextField(20);
    JButton nextB = new JButton("Next-step");

    // Three problems currently supported.
    MazeProblem mazeProblem;
    PuzzleProblem puzzleProblem;
    ArmProblem armProblem;

    // Which planner, which problem etc. These are interfaces.
    Planner planner;
    PlanningProblem problem;
    LinkedList<State> plan;


    //////////////////////////////////////////////////////////////////////
    // main

    public static void main(String[] argv) {
        new PlanningGUI();
    }


    //////////////////////////////////////////////////////////////////////
    // GUI construction

    public PlanningGUI() {
        this.setSize(800, 500);
        this.setResizable(true);
        this.setBackground(Color.gray);

        mazeProblem = new MazeProblem(status);
        puzzleProblem = new PuzzleProblem(status);
        armProblem = new ArmProblem(status);

        Container cPane = this.getContentPane();
        cPane.add(status, BorderLayout.NORTH);
        tabbedPane = new JTabbedPane();
        tabbedPane.addChangeListener(
                new ChangeListener() {
                    public void stateChanged(ChangeEvent e) {
                        tabChange();
                    }
                }
        );

        // Add tab apps.
        tabbedPane.add("Maze", mazeProblem.getFullPanel());
        tabbedPane.add("8-Puzzle", puzzleProblem.getFullPanel());
        tabbedPane.add("Arm", armProblem.getFullPanel());

        cPane.add(tabbedPane, BorderLayout.CENTER);
        cPane.add(makeBottomPanel(), BorderLayout.SOUTH);

        this.setVisible(true);
        problem = mazeProblem;
    }


    JPanel makeBottomPanel() {
        JPanel panel = new JPanel();

        panel.add(new JLabel("Algorithm: "));
        panel.add(algField);
        JButton loadB = new JButton("Load");
        panel.add(loadB);
        loadB.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        loadAlgorithm();
                    }
                }
        );

        panel.add(new JLabel("    "));
        JButton planB = new JButton("Plan");
        panel.add(planB);
        planB.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        plan();
                    }
                }
        );

        panel.add(new JLabel("    "));
        panel.add(nextB);
        nextB.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        next();
                    }
                }
        );
        nextB.setEnabled(false);

        panel.add(new JLabel("    "));
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


    //////////////////////////////////////////////////////////////////////
    // GUI event handling

    void tabChange() {
        int index = tabbedPane.getSelectedIndex();
        if (index == 0) {
            problem = mazeProblem;
            nextB.setEnabled(false);
        } else if (index == 1) {
            problem = puzzleProblem;
            nextB.setEnabled(false);
        } else if (index == 2) {
            problem = armProblem;
            nextB.setEnabled(false);
        }
    }


    void plan() {
        if (planner != null) {
            System.out.println("Starting plan generation ...");
            status.setText("Starting plan generation ...");
            plan = planner.makePlan(problem, problem.getStartState());
            if (plan == null) {
                status.setText("No solution found");
                return;
            }
            problem.setPlan(plan);
            status.setText("Planning complete");
            nextB.setEnabled(true);
        } else {
            status.setText("Error: No planner loaded");
        }
    }


    void loadAlgorithm() {
        String className = algField.getText();
        try {
            planner = (Planner) (Class.forName(className)).newInstance();
            status.setText(className + " loaded");
            nextB.setEnabled(false);
        } catch (Exception e) {
            System.out.println(e);
            status.setText("Failed to load " + className);
        }
    }


    void next() {
        int index = tabbedPane.getSelectedIndex();
        if (index == 0) {
            mazeProblem.next();
        } else if (index == 1) {
            puzzleProblem.next();
        } else if (index == 2) {
            armProblem.next();
        }
    }

}
