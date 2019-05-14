import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;
import java.text.*;
import java.awt.geom.*;


public class ODESimPanel extends JPanel {

    JLabel statusLabel;           // A bar on top for messages.
    JPanel drawPanel;             // Unused.

    // Labels for displaying current concentrations:
    JLabel ALabel, BLabel, CLabel;
    // Concentrations:
    double concA, concB, concC;
    // Data points for curves.
    Vector concAPoints, concBPoints, concCPoints;

    // To read in initial concentrations:
    JTextField AField, BField, CField;
    double initConcA = 0.0, initConcB = 0.0, initConcC = 0.0;

    // Plot colors.
    Color colorA = Color.blue;
    Color colorB = Color.green;
    Color colorC = Color.yellow;

    // Two rates parameters.
    JTextField k_ABField, k_CField;
    double k_AB, k_C;


    // ODE simulation time-parameters
    double deltaT = 0.01;             // The time-interval for the ODE.
    JTextField deltaTField;           // Read this value in.
    double currentTime = 0;           // Current time.
    JLabel timeLabel;                 // Display current time.
    double endTime = 1.0;             // When to end simulation.
    JTextField endTimeField;          // Read in endTime.

    DecimalFormat df = new DecimalFormat("###.###");


    //------------------------------------------------------------------
    // Constructor

    public ODESimPanel() {
        this.setLayout(new BorderLayout());

        // Status bar:
        Border border = BorderFactory.createLineBorder(Color.black);
        statusLabel = new JLabel(" ");
        statusLabel.setBorder(border);
        this.add(statusLabel, BorderLayout.NORTH);

        // Right now, this center panel is unused.
        drawPanel = new JPanel();
        this.add(drawPanel, BorderLayout.CENTER);

        // Controls.
        this.add(makeBottomPanel(), BorderLayout.SOUTH);
    }


    //------------------------------------------------------------------
    // Screen updates

    // Report status messages on screen.

    public void status(String msg) {
        statusLabel.setForeground(Color.black);
        statusLabel.setText(msg);
    }


    // Report error messages on screen.

    public void error(String str) {
        statusLabel.setForeground(Color.red);
        statusLabel.setText("  " + str);
    }


    //------------------------------------------------------------------
    // GUI construction

    JPanel makeBottomPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));

        panel.add(makeDisplayLabels());
        panel.add(makeConcControls());
        panel.add(makeParameterControls());
        panel.add(makeSimulationRunControls());

        return panel;
    }


    JPanel makeDisplayLabels() {
        JPanel panel = new JPanel();

        Border border = BorderFactory.createTitledBorder("  Stats  ");
        panel.setBorder(border);

        panel.setLayout(new GridLayout(1, 6));

        ALabel = new JLabel(" A: ");
        border = BorderFactory.createLineBorder(Color.black);
        ALabel.setBorder(border);
        panel.add(ALabel);

        BLabel = new JLabel(" B: ");
        border = BorderFactory.createLineBorder(Color.black);
        BLabel.setBorder(border);
        panel.add(BLabel);

        CLabel = new JLabel("C: ");
        border = BorderFactory.createLineBorder(Color.black);
        CLabel.setBorder(border);
        panel.add(CLabel);

        timeLabel = new JLabel(" Time: ");
        border = BorderFactory.createLineBorder(Color.black);
        timeLabel.setBorder(border);
        panel.add(timeLabel);

        return panel;
    }


    JPanel makeConcControls() {
        JPanel panel = new JPanel();

        Border border = BorderFactory.createTitledBorder("  Initial concentration  ");
        panel.setBorder(border);

        JLabel label = new JLabel("A:");
        panel.add(label);
        AField = new JTextField(4);
        AField.setText("" + initConcA);
        panel.add(AField);

        panel.add(new JLabel("   "));

        label = new JLabel("B:");
        panel.add(label);
        BField = new JTextField(4);
        BField.setText("" + initConcB);
        panel.add(BField);

        panel.add(new JLabel("   "));

        label = new JLabel("C:");
        panel.add(label);
        CField = new JTextField(4);
        CField.setText("" + initConcC);
        panel.add(CField);

        panel.add(new JLabel("         "));

        JButton concB = new JButton("Change");
        concB.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        changeInitConc();
                    }
                }
        );
        panel.add(concB);

        return panel;
    }

    JPanel makeParameterControls() {
        JPanel panel = new JPanel();

        Border border = BorderFactory.createTitledBorder("  Parameters  ");
        panel.setBorder(border);

        JLabel label = new JLabel("<html>K<sub>AB</sub>:</html> ");
        panel.add(label);
        k_ABField = new JTextField(4);
        k_ABField.setText("" + k_AB);
        panel.add(k_ABField);

        panel.add(new JLabel("   "));

        label = new JLabel("<html>K<sub>C</sub>:</html> ");
        panel.add(label);
        k_CField = new JTextField(4);
        k_CField.setText("" + k_C);
        panel.add(k_CField);

        panel.add(new JLabel("   "));

        panel.add(new JLabel("         "));

        label = new JLabel("DeltaT: ");
        panel.add(label);
        deltaTField = new JTextField(4);
        deltaTField.setText("" + deltaT);
        panel.add(deltaTField);

        panel.add(new JLabel("         "));

        JButton paramB = new JButton("Change");
        paramB.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        changeParams();
                    }
                }
        );
        panel.add(paramB);

        return panel;
    }


    JPanel makeSimulationRunControls() {
        JPanel panel = new JPanel();

        Border border = BorderFactory.createTitledBorder("  Simulation  ");
        panel.setBorder(border);


        JLabel label = new JLabel("End time:");
        panel.add(label);
        endTimeField = new JTextField(2);
        endTimeField.setText("" + endTime);
        panel.add(endTimeField);

        panel.add(new JLabel("         "));

        JButton simB = new JButton("Simulate");
        simB.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        simulate();
                    }
                }
        );
        panel.add(simB);

        return panel;
    }


    //------------------------------------------------------------------
    // GUI actions


    void changeParams() {
        try {
            String str = k_ABField.getText();
            double newVal = Double.parseDouble(str.trim());
            if (newVal >= 0) {
                k_AB = newVal;
            } else {
                error("k_AB must be non-negative");
            }

            str = k_CField.getText();
            newVal = Double.parseDouble(str.trim());
            if (newVal >= 0) {
                k_C = newVal;
            } else {
                error("k_C must be non-negative");
            }

            str = deltaTField.getText();
            newVal = Double.parseDouble(str.trim());
            if (newVal > 0) {
                deltaT = newVal;
            } else {
                error("DeltaT must be positive");
            }

            status("Changed parameters: k_AB=" + k_AB + ", k_C=" + k_C + " deltaT=" + deltaT);
        } catch (Exception e) {
            error("Improper number in parameter field");
        }
    }


    void changeInitConc() {
        try {
            String str = AField.getText();
            double newVal = Double.parseDouble(str.trim());
            if (newVal >= 0) {
                initConcA = newVal;
            } else {
                error("A must be >= 0");
            }

            str = BField.getText();
            newVal = Double.parseDouble(str.trim());
            if (newVal >= 0) {
                initConcB = newVal;
            } else {
                error("B must be >= 0");
            }

            str = CField.getText();
            newVal = Double.parseDouble(str.trim());
            if (newVal >= 0) {
                initConcC = newVal;
            } else {
                error("C must be >= 0");
            }

            status("Changed concentrations: concA=" + initConcA + ", concB=" + initConcB + ", concC=" + initConcC);
        } catch (Exception e) {
            error("Improper number in concentration field");
        }
    }


    //------------------------------------------------------------------
    // Simulation/iteration.


    void initializeStats() {
        concAPoints = new Vector();
        concBPoints = new Vector();
        concCPoints = new Vector();
    }


    void simulate() {
        try {
            double newEndTime = Double.parseDouble(endTimeField.getText());
            if (newEndTime > 0) {
                endTime = newEndTime;
            } else {
                error("EndTime must be positive");
                return;
            }

            runSimulation();

            status("Starting ODE simulation: deltaT=" + deltaT + " endTime=" + endTime);
        } catch (NumberFormatException e) {
            error("Improper integer in runs field");
        }
    }


    void runSimulation() {
        currentTime = 0;
        concA = initConcA;
        concB = initConcB;
        concC = initConcC;

        initializeStats();

        while (currentTime <= endTime) {

            // Add current values to time-plot.
            concAPoints.add(new Point2D.Double(currentTime, concA));
            concBPoints.add(new Point2D.Double(currentTime, concB));
            concCPoints.add(new Point2D.Double(currentTime, concC));

            // Update GUI labels.
            ALabel.setText("A: " + df.format(concA));
            BLabel.setText("B: " + df.format(concB));
            CLabel.setText("C: " + df.format(concC));
            timeLabel.setText("Time: " + df.format(currentTime));

            // Set the new time.
            currentTime += deltaT;

            // Update the concentrations using differential equations.
            concA += deltaT * (-k_AB * concA * concB + k_C * concC);
            concB += deltaT * (-k_AB * concA * concB + k_C * concC);
            concC += deltaT * (-k_C * concC + k_AB * concA * concB);

        } // endwhile

        // Plot.
        plot();
    }


    void plot() {
        SimplePlotPanel plotPanel = new SimplePlotPanel(3, concAPoints.size());
        int idA = plotPanel.createNewCurve("Conc(A)", colorA);
        int idB = plotPanel.createNewCurve("Conc(B)", colorB);
        int idC = plotPanel.createNewCurve("Conc(C)", colorC);

        plotPanel.setXaxisLabel("Time");
        plotPanel.setYaxisLabel("Conc");
        plotPanel.setNumXTicks(10);
        plotPanel.setNumYTicks(10);
        plotPanel.setXYPoints(idA, concAPoints);
        plotPanel.setXYPoints(idB, concBPoints);
        plotPanel.setXYPoints(idC, concCPoints);

        JFrame frame = new JFrame();
        frame.setTitle("Concentrations vs. time");
        frame.setSize(500, 400);
        frame.getContentPane().add(plotPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
