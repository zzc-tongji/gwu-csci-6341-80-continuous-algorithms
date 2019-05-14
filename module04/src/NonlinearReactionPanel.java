import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;
import java.text.*;
import java.awt.geom.*;

public class NonlinearReactionPanel extends JPanel {

    public static final int BRUSSELATOR = 1;
    public static final int RABBIT_LYNX = 2;

    JLabel statusLabel;           // A bar on top for messages.
    JPanel drawPanel;             // Not used.

    // X,Y related variables.
    double concX, concY;                  // Current concentrations.
    JLabel XLabel, YLabel;                // Display concentrations.
    double initConcX = 0, initConcY = 0;      // Initial conc's.
    // Brusselator: initX=0.4, initY=0.5
    // Rabbit-lynx: initX=1.5, initY=1.0

    JTextField XField, YField;            // Read in initial conc's.
    Vector concXPoints, concYPoints;      // For plot data.
    // Plot colors.
    Color colorX = Color.blue;
    Color colorY = Color.green;

    // Rate variables.
    double k1, k2, k3, k4;
    JTextField k1Field, k2Field, k3Field, k4Field;
    // For Brusselator: k1=k2=k3=k4=1.
    // For Rabbit-Lynx: k1=2.4, k2=4.2, k3=5.1, k4 unused.

    // Two additional constants used in Brusselator. a=1, b=3.
    double a = 1, b = 3;

    // ODE simulation time-parameters
    double deltaT = 0.01;             // The time-interval for the ODE.
    JTextField deltaTField;           // Read this value in.
    double currentTime = 0;           // Current time.
    JLabel timeLabel;                 // Display current time.
    double endTime = 10.0;            // When to end simulation.
    JTextField endTimeField;          // Read in endTime.

    DecimalFormat df = new DecimalFormat("###.###");

    // Default model:
    int model = BRUSSELATOR;


    //------------------------------------------------------------------
    // Constructor

    public NonlinearReactionPanel(int model) {
        if (model == BRUSSELATOR) {
            this.model = BRUSSELATOR;
            // a = 1;  b = 3;
            // k1 = 1;  k2 = 1;  k3 = 1;  k4 = 1;
        } else if (model == RABBIT_LYNX) {
            this.model = RABBIT_LYNX;
            // k1 = 2.4;  k2 = 4.2;  k3 = 5.1; k4 = 0;
        }

        this.setLayout(new BorderLayout());

        // Status bar:
        Border border = BorderFactory.createLineBorder(Color.black);
        statusLabel = new JLabel(" ");
        statusLabel.setBorder(border);
        this.add(statusLabel, BorderLayout.NORTH);

        // Unused panel.
        drawPanel = new JPanel();
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
        panel.setLayout(new GridLayout(4, 1));

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

        XLabel = new JLabel(" X: ");
        border = BorderFactory.createLineBorder(Color.black);
        XLabel.setBorder(border);
        panel.add(XLabel);

        YLabel = new JLabel(" Y: ");
        border = BorderFactory.createLineBorder(Color.black);
        YLabel.setBorder(border);
        panel.add(YLabel);

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

        JLabel label = new JLabel("X:");
        panel.add(label);
        XField = new JTextField(4);
        XField.setText("" + initConcX);
        panel.add(XField);

        panel.add(new JLabel("   "));

        label = new JLabel("Y:");
        panel.add(label);
        YField = new JTextField(4);
        YField.setText("" + initConcY);
        panel.add(YField);

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

        JLabel label = new JLabel("k1:");
        panel.add(label);
        k1Field = new JTextField(4);
        k1Field.setText("" + k1);
        panel.add(k1Field);

        label = new JLabel("k2:");
        panel.add(label);
        k2Field = new JTextField(4);
        k2Field.setText("" + k2);
        panel.add(k2Field);

        label = new JLabel("k3:");
        panel.add(label);
        k3Field = new JTextField(4);
        k3Field.setText("" + k3);
        panel.add(k3Field);

        label = new JLabel("k4:");
        panel.add(label);
        k4Field = new JTextField(4);
        k4Field.setText("" + k4);
        panel.add(k4Field);

        panel.add(new JLabel("   "));

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


        JLabel label = new JLabel("DeltaT:");
        panel.add(label);
        deltaTField = new JTextField(2);
        deltaTField.setText("" + deltaT);
        panel.add(deltaTField);

        panel.add(new JLabel("    "));

        label = new JLabel("End time:");
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
            String str = k1Field.getText();
            double newVal = Double.parseDouble(str.trim());
            if (newVal >= 0) {
                k1 = newVal;
            } else {
                error("k1 must be non-negative");
            }

            str = k2Field.getText();
            newVal = Double.parseDouble(str.trim());
            if (newVal >= 0) {
                k2 = newVal;
            } else {
                error("k2 must be non-negative");
            }

            str = k3Field.getText();
            newVal = Double.parseDouble(str.trim());
            if (newVal >= 0) {
                k3 = newVal;
            } else {
                error("k3 must be non-negative");
            }

            str = k3Field.getText();
            newVal = Double.parseDouble(str.trim());
            if (newVal >= 0) {
                k3 = newVal;
            } else {
                error("k3 must be non-negative");
            }

            status("Changed k1=" + k1 + " k2=" + k2 + " k3=" + k3 + " k4=" + k4);

        } catch (Exception e) {
            error("Improper number in parameter field");
        }
    }


    void changeInitConc() {
        try {
            String str = XField.getText();
            double newVal = Double.parseDouble(str.trim());
            if (newVal >= 0) {
                initConcX = newVal;
            } else {
                error("X must be >= 0");
            }

            str = YField.getText();
            newVal = Double.parseDouble(str.trim());
            if (newVal >= 0) {
                initConcY = newVal;
            } else {
                error("Y must be >= 0");
            }

            status("Changed initial concentrations: concX=" + initConcX + " concY=" + initConcY);
        } catch (Exception e) {
            error("Improper number in concentration field");
        }
    }


    //------------------------------------------------------------------
    // Simulation/iteration.

    void initializeStats() {
        concXPoints = new Vector();
        concYPoints = new Vector();
    }


    void simulate() {
        try {
            String str = deltaTField.getText();
            double newVal = Double.parseDouble(str.trim());
            if (newVal > 0) {
                deltaT = newVal;
            } else {
                error("DeltaT must be positive");
            }

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
        concX = initConcX;
        concY = initConcY;

        initializeStats();

        while (currentTime <= endTime) {

            // Add current values to time-plot.
            concXPoints.add(new Point2D.Double(currentTime, concX));
            concYPoints.add(new Point2D.Double(currentTime, concY));

            // Update GUI labels.
            XLabel.setText("X: " + df.format(concX));
            YLabel.setText("Y: " + df.format(concY));
            timeLabel.setText("Time: " + df.format(currentTime));

            // Set the new time.
            currentTime += deltaT;

            // Update the concentrations using differential equations.
            if (model == BRUSSELATOR) {
                concX += deltaT * (a * k1 - b * k2 * concX + k3 * concX * concX * concY - k4 * concX);
                concY += deltaT * (b * k2 * concX - k3 * concX * concX * concY);
            } else if (model == RABBIT_LYNX) {
                concX += deltaT * (k1 * concX - k2 * concX * concY);
                concY += deltaT * (k2 * concX * concY - k3 * concY);
            }

        } // end-while

        // Plot.
        plot();
    }


    void plot() {
        SimplePlotPanel plotPanel = new SimplePlotPanel(4, concXPoints.size());
        int idX = plotPanel.createNewCurve("Conc(X)", colorX);
        int idY = plotPanel.createNewCurve("Conc(Y)", colorY);

        plotPanel.setXaxisLabel("Time");
        plotPanel.setYaxisLabel("Conc");
        plotPanel.setNumXTicks(10);
        plotPanel.setNumYTicks(10);
        plotPanel.setXYPoints(idX, concXPoints);
        plotPanel.setXYPoints(idY, concYPoints);

        JFrame frame = new JFrame();
        frame.setTitle("Concentrations vs. time");
        frame.setSize(500, 400);
        frame.getContentPane().add(plotPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
