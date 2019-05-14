import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;
import java.text.*;
import java.awt.geom.*;

// A small class to store molecule-related info for each molecule.

class Molecule {

    // To test equality.
    static int IDcount = 1;
    int ID;

    // Current location.
    int x, y;

    // Direction of travel.
    double theta = UniformRandom.uniform(0.0, 2 * Math.PI);

    public Molecule(int x, int y) {
        ID = IDcount++;
        this.x = x;
        this.y = y;
    }

    public boolean equals(Object obj) {
        return (this.ID == ((Molecule) obj).ID);
    }

} //end-class-Molecule


public class MolecularSimPanel extends JPanel {

    //------------------------------------------------------------------
    // Variables

    // Two simulation models.
    public static final int STANDARD = 1;
    public static final int SPATIAL = 2;

    int model = STANDARD;            // Current model.

    JLabel statusLabel;                  // A bar on top for messages.
    JPanel drawPanel;

    int numA, numB, numC;                // Variables for molecule counts.
    LinkedList molA, molB, molC;         // Store molecule instances.
    double concA, concB, concC;          // Current concentrations.
    JLabel ALabel, BLabel, CLabel;       // Display current concentrations.
    JTextField AField, BField, CField;   // Read in initial concentrations.

    // Display sizes and colors for the three types of molecules.    
    int sizeA = 10;
    int sizeB = 13;
    int sizeC = 18;
    Color colorA = Color.blue;
    Color colorB = Color.green;
    Color colorC = Color.yellow;

    // Data for plotting.
    Vector concAPoints, concBPoints, concCPoints;

    // Parameters.
    JTextField pField, qField;
    double p = 1.0, q = 1.0;

    // Time variables (start time = 0).
    int currentTime = 0;
    int endTime = 1;
    JTextField endTimeField;
    JLabel timeLabel;

    // Number of simulation runs (for time averages)
    int numRuns = 1;
    JLabel runLabel;
    JTextField numRunsField;

    // How often to record statistics.
    int timeInterval = 1;
    JTextField intervalField;

    // Animation.
    double speed = 1;                   // Higher number => faster animation.
    JTextField speedField;           // Read in from GUI.
    boolean isStopped = true;        // Use as flag to start/stop animation.
    int moveLength = 5;              // Amount to move each molecule.
    double thetaChangeProb = 0.3;    // How often to change direction.

    // For spatial model:
    int closenessDistance = 100;     // To see if two molecules are close.

    // A reset is required for each new simulation.
    boolean resetOccurred = false;

    DecimalFormat df = new DecimalFormat("###.###");


    //------------------------------------------------------------------
    // Constructor

    public MolecularSimPanel(int model) {
        this.model = model;

        this.setLayout(new BorderLayout());
        // Status bar:
        Border border = BorderFactory.createLineBorder(Color.black);
        statusLabel = new JLabel(" ");
        statusLabel.setBorder(border);
        this.add(statusLabel, BorderLayout.NORTH);

        border = BorderFactory.createLineBorder(Color.black);
        drawPanel = new JPanel();
        drawPanel.setBorder(border);
        this.add(makeBottomPanel(), BorderLayout.SOUTH);
        this.add(drawPanel, BorderLayout.CENTER);
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

    // The bottom panel is a collection of fields, labels and buttons.

    JPanel makeBottomPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));

        panel.add(makeDisplayLabels());
        panel.add(makeConcControls());
        panel.add(makeParameterControls());
        panel.add(makeSingleRunControls());
        panel.add(makeSimulationRunControls());

        return panel;
    }


    // Display labels for displaying statistics during simulation

    JPanel makeDisplayLabels() {
        JPanel panel = new JPanel();

        Border border = BorderFactory.createTitledBorder("  Stats  ");
        panel.setBorder(border);

        panel.setLayout(new GridLayout(1, 5));

        runLabel = new JLabel(" Run: ");
        border = BorderFactory.createLineBorder(Color.black);
        runLabel.setBorder(border);
        panel.add(runLabel);

        ALabel = new JLabel(" A: ");
        border = BorderFactory.createLineBorder(Color.black);
        ALabel.setBorder(border);
        panel.add(ALabel);

        BLabel = new JLabel(" B: ");
        border = BorderFactory.createLineBorder(Color.black);
        BLabel.setBorder(border);
        panel.add(BLabel);

        CLabel = new JLabel(" C: ");
        border = BorderFactory.createLineBorder(Color.black);
        CLabel.setBorder(border);
        panel.add(CLabel);

        timeLabel = new JLabel(" Time: ");
        border = BorderFactory.createLineBorder(Color.black);
        timeLabel.setBorder(border);
        panel.add(timeLabel);

        return panel;
    }


    // Read in concentration values.

    JPanel makeConcControls() {
        JPanel panel = new JPanel();

        Border border = BorderFactory.createTitledBorder("  Initial numbers of molecules  ");
        panel.setBorder(border);

        JLabel label = new JLabel("A:");
        panel.add(label);
        AField = new JTextField(4);
        AField.setText("" + numA);
        panel.add(AField);

        panel.add(new JLabel("   "));

        label = new JLabel("B:");
        panel.add(label);
        BField = new JTextField(4);
        BField.setText("" + numB);
        panel.add(BField);

        panel.add(new JLabel("   "));

        label = new JLabel("C:");
        panel.add(label);
        CField = new JTextField(4);
        CField.setText("" + numC);
        panel.add(CField);

        panel.add(new JLabel("         "));

        JButton cB = new JButton("Change");
        cB.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        changeInitConc();
                    }
                }
        );
        panel.add(cB);

        return panel;
    }


    // Read in parameters

    JPanel makeParameterControls() {
        JPanel panel = new JPanel();

        Border border = BorderFactory.createTitledBorder("  Parameters  ");
        panel.setBorder(border);

        JLabel label = new JLabel("p:");
        panel.add(label);
        pField = new JTextField(4);
        pField.setText("" + p);
        panel.add(pField);

        panel.add(new JLabel("   "));

        label = new JLabel("q:");
        panel.add(label);
        qField = new JTextField(4);
        qField.setText("" + q);
        panel.add(qField);

        panel.add(new JLabel("         "));

        label = new JLabel("Observation-Interval:");
        panel.add(label);
        intervalField = new JTextField(4);
        intervalField.setText("" + timeInterval);
        panel.add(intervalField);

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


    // The panel for single-run controls

    JPanel makeSingleRunControls() {
        JPanel panel = new JPanel();

        Border border = BorderFactory.createTitledBorder("  Single Run with animation ");
        panel.setBorder(border);

        JButton clearB = new JButton("Reset");
        clearB.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        reset();
                    }
                }
        );
        panel.add(clearB);

        JButton nextB = new JButton("Step");
        nextB.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        nextStep();
                    }
                }
        );
        panel.add(nextB);

        JButton goB = new JButton("Go");
        goB.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        animate();
                    }
                }
        );
        panel.add(goB);

        JButton stopB = new JButton("Stop");
        stopB.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        stop();
                    }
                }
        );
        panel.add(stopB);

        JButton plotB = new JButton("Plot");
        plotB.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        plot();
                    }
                }
        );
        panel.add(plotB);

        panel.add(new JLabel("    "));

        JLabel label = new JLabel("Speed:");
        panel.add(label);
        speedField = new JTextField(4);
        speedField.setText("1.0");
        panel.add(speedField);
        JButton speedB = new JButton("Change");
        speedB.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        changeSpeed();
                    }
                }
        );
        panel.add(speedB);

        return panel;
    }


    // Statistically averaged simulations

    JPanel makeSimulationRunControls() {
        JPanel panel = new JPanel();

        Border border = BorderFactory.createTitledBorder("  Simulation (without animation) ");
        panel.setBorder(border);


        JLabel label = new JLabel("Num runs:");
        panel.add(label);
        numRunsField = new JTextField(2);
        numRunsField.setText("1");
        panel.add(numRunsField);

        panel.add(new JLabel("         "));

        label = new JLabel("End time:");
        panel.add(label);
        endTimeField = new JTextField(2);
        endTimeField.setText("1");
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


    // Read in initial numbers of molecules.

    void changeInitConc() {
        try {
            String str = AField.getText();
            int newVal = Integer.parseInt(str.trim());
            if (newVal >= 0) {
                numA = newVal;
            } else {
                error("A must be >= 0");
            }

            str = BField.getText();
            newVal = Integer.parseInt(str.trim());
            if (newVal >= 0) {
                numB = newVal;
            } else {
                error("B must be >= 0");
            }

            str = CField.getText();
            newVal = Integer.parseInt(str.trim());
            if (newVal >= 0) {
                numC = newVal;
            } else {
                error("C must be >= 0");
            }

            resetOccurred = false;
            status("Changed numbers: numA=" + numA + ", numB=" + numB + ", numC=" + numC);
        } catch (Exception e) {
            error("Improper number in concentration field");
        }
    }


    // Read in parameter values.

    void changeParams() {
        try {
            String str = pField.getText();
            double newVal = Double.parseDouble(str.trim());
            if ((newVal >= 0) && (newVal <= 1)) {
                p = newVal;
            } else {
                error("p must be between 0 and 1");
            }

            str = qField.getText();
            newVal = Double.parseDouble(str.trim());
            if ((newVal >= 0) && (newVal <= 1)) {
                q = newVal;
            } else {
                error("q must be between 0 and 1");
            }

            str = intervalField.getText();
            int newInt = Integer.parseInt(str.trim());
            if (newInt >= 1) {
                timeInterval = newInt;
            } else {
                error("Obs.Interval must be at least 1");
            }

            resetOccurred = false;
            status("Changed parameters: p=" + p + ", q=" + q + "  timeInt=" + timeInterval);
        } catch (Exception e) {
            error("Improper number in parameter field");
        }
    }


    // Change animation speed.

    void changeSpeed() {
        try {
            double newSpeed = Double.parseDouble(speedField.getText());
            if (newSpeed > 0) {
                speed = newSpeed;
            }
            status("Changed speed: " + speed);
        } catch (NumberFormatException e) {
            error("Improper number in speed field");
        }
    }


    //------------------------------------------------------------------
    // Animation


    void animate() {
        // Start a new thread each time.
        Thread t = new Thread() {
            public void run() {
                runAnimation();
            }
        };
        t.start();
    }


    void runAnimation() {
        if (!resetOccurred) {
            error("Need to reset before starting animation");
            return;
        }

        isStopped = false;
        while (!isStopped) {
            try {
                int sleepTime = (int) (100.0 / speed);
                Thread.sleep(sleepTime);
            } catch (Exception e) {
            }
            nextStep();
        }
    }

    void stop() {
        isStopped = true;
    }


    //------------------------------------------------------------------
    // Drawing and plotting

    void redraw() {
        Dimension D = drawPanel.getSize();
        Graphics g = drawPanel.getGraphics();

        // Blank out draw area.
        g.setColor(Color.white);
        g.fillRect(0, 0, D.width, D.height);

        // Draw molecules in their current positions.
        drawMolecules(molA, colorA, sizeA);
        drawMolecules(molB, colorB, sizeB);
        drawMolecules(molC, colorC, sizeC);
    }


    void drawMolecules(LinkedList mol, Color color, int size) {
        Graphics g = drawPanel.getGraphics();
        g.setColor(color);
        Dimension D = drawPanel.getSize();
        for (Iterator iter = mol.iterator(); iter.hasNext(); ) {
            Molecule m = (Molecule) iter.next();
            int topLeftX = m.x - size;  // size=radius.
            int topLeftY = D.height - (m.y - size);
            g.fillOval(topLeftX, topLeftY, 2 * size, 2 * size);
        }
    }


    // Plot a graph with each of the concentrations.

    void plot() {
        if ((concAPoints == null) || (concAPoints.size() <= 0)) {
            error("Insufficient points for a plot");
            return;
        }

        SimplePlotPanel plotPanel = new SimplePlotPanel(4, concAPoints.size());
        // All three curves on the same plot.
        int idA = plotPanel.createNewCurve("Conc(A)", colorA);
        int idB = plotPanel.createNewCurve("Conc(B)", colorB);
        int idC = plotPanel.createNewCurve("Conc(C)", colorC);
        plotPanel.setXaxisLabel("Timesteps");
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
        // NOTE: we aren't disposing of previous plot frames.
    }


    //------------------------------------------------------------------
    // Simulation: methods common to standard/spatial single run


    // A reset removes past data, initializes the molecule sets.

    void reset() {
        currentTime = 0;
        initializeMolecules();
        initializeStats();
        computeConcentrations();
        updateLabels();
        redraw();
        resetOccurred = true;
    }


    // These next two methods are separated out because they
    // are used by the main simulation as well.

    void initializeMolecules() {
        molA = makeMolecules(numA);
        molB = makeMolecules(numB);
        molC = makeMolecules(numC);
    }


    void initializeStats() {
        concAPoints = new Vector();
        concBPoints = new Vector();
        concCPoints = new Vector();
    }


    LinkedList makeMolecules(int num) {
        Dimension D = drawPanel.getSize();
        LinkedList mol = new LinkedList();
        for (int i = 0; i < num; i++) {
            // Random location for each molecule.
            int x = (int) UniformRandom.uniform((long) 10, (long) D.width - 10);
            int y = (int) UniformRandom.uniform((long) 40, (long) D.height - 20);            // Note: we use an offset of 40 from the bottom because of
            // of what appears to be a Swing error in covering the drawPanel
            // by the border of the stats panel.

            mol.add(new Molecule(x, y));
        }
        return mol;
    }


    // Each step in a simulation.

    void nextStep() {
        if (!resetOccurred) {
            error("Need to reset before animating");
            return;
        }

        // First, move molecules to new locations.
        moveMolecules(molA);
        moveMolecules(molB);
        moveMolecules(molC);

        // Reactions
        doReactions();

        // Update counts at the given intervals.
        if (currentTime % timeInterval == 0) {
            doStats();
        }

        // Advance time (discrete steps)
        currentTime++;

        // Repaint.
        redraw();
    }


    // Move given list of molecules to new random locations.

    void moveMolecules(LinkedList mol) {
        Dimension D = drawPanel.getSize();

        for (Iterator iter = mol.iterator(); iter.hasNext(); ) {

            // Extract next molecule.
            Molecule m = (Molecule) iter.next();

            // Change direction of movement? (Like brownian motion).
            if (UniformRandom.uniform() < thetaChangeProb) {
                m.theta = UniformRandom.uniform(0.0, 2.0 * Math.PI);
            }

            // Get new x,y values.
            int nextX = (int) Math.round((m.x + moveLength * Math.cos(m.theta)));
            int nextY = (int) Math.round((m.y + moveLength * Math.sin(m.theta)));
            // See if too close to boundary.
            if ((nextX < 10) || (nextX > D.width - 10) ||
                    (nextY < 40) || (nextY > D.height - 10)) {
                // If so, change direction randomly.
                m.theta = UniformRandom.uniform(0.0, 2.0 * Math.PI);
            } else {
                // Else, move.
                m.x = nextX;
                m.y = nextY;
            }

        } //end-for

    }


    void doReactions() {
        if (model == STANDARD) {
            standard();
        } else if (model == SPATIAL) {
            spatial();
        } else {
            System.out.println("ERROR: no such model");
        }
    }


    int sqrDistance(Molecule a, Molecule b) {
        return ((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
    }


    void computeConcentrations() {
        int nA = molA.size();
        int nB = molB.size();
        int nC = molC.size();

        // Note: volume is proportional to the total
        // number of A and B molecules (hence C counts for two).
        int total = nA + nB + 2 * nC;
        concA = (double) nA / total;
        concB = (double) nB / total;
        concC = (double) nC / total;
    }


    // Points for curve plotting. Each Point2D.Double instance
    // is an (x,y) value for a curve.

    void doStats() {
        computeConcentrations();
        concAPoints.add(new Point2D.Double(currentTime, concA));
        concBPoints.add(new Point2D.Double(currentTime, concB));
        concCPoints.add(new Point2D.Double(currentTime, concC));
        updateLabels();
    }


    // Update counters/labels on GUI.

    void updateLabels() {
        ALabel.setText("A: " + df.format(concA));
        BLabel.setText("B: " + df.format(concB));
        CLabel.setText("C: " + df.format(concC));
        timeLabel.setText("Time: " + currentTime);
    }


    //------------------------------------------------------------------
    // Standard model: use relative numbers of molecules as concentrations.


    void standard() {
        computeConcentrations();
        // Thus, concA = numA / (numA+numB+2*numC etc

        double alpha = concA * concB * p;
        double beta = concC * q;

        if (alpha + beta == 0) {
            // No reaction possible.
            error("Alpha=Beta=0: no reaction possible");
            return;
        }

        // Prob[A+B reaction]
        double AB_prob = alpha / (alpha + beta);
        // Prob[C reaction]
        double C_prob = beta / (alpha + beta);

        // If we're doing an AB reaction
        if (UniformRandom.uniform() < AB_prob) {

            if ((molA.size() <= 1) || (molB.size() <= 1)) {
                // Need at least one of each molecule.
                return;
            }

            // Do A+B -> C. Pick one A molecule randomly.
            int k = (int) UniformRandom.uniform(0, molA.size() - 1);
            Molecule a = (Molecule) molA.remove(k);
            // Find closest B
            int closestDistance = Integer.MAX_VALUE;
            int indexClosest = -1;
            int indexCount = -1;
            for (Iterator iter = molB.iterator(); iter.hasNext(); ) {
                indexCount++;
                Molecule b = (Molecule) iter.next();
                int d = sqrDistance(a, b);
                if (d < closestDistance) {
                    closestDistance = d;
                    indexClosest = indexCount;
                }
            }
            // Remove that B molecule.
            Molecule b = (Molecule) molB.remove(indexClosest);
            // Create a new C molecule in between.
            int x = (int) ((a.x + b.x) / 2.0);
            int y = (int) ((a.y + b.y) / 2.0);
            Molecule c = new Molecule(x, y);
            molC.add(c);

        } // end-if
        else {

            // C -> A+B
            if (molC.size() <= 1) {
                // Need at least one.
                return;
            }
            // Find a random C molecule.
            int k = (int) UniformRandom.uniform(0, molC.size() - 1);
            Molecule c = (Molecule) molC.remove(k);
            // Make an A and a B molecule.
            Molecule a = new Molecule(c.x, c.y);
            molA.add(a);
            Molecule b = new Molecule(c.x, c.y);
            molB.add(b);

        } //end-if-else

    }


    void spatial() {
        computeConcentrations();

        // For every A molecule:
        for (Iterator iterA = molA.iterator(); iterA.hasNext(); ) {

            Molecule a = (Molecule) iterA.next();

            // Find closest B molecule.
            int indexClosest = -1;
            int indexCount = -1;
            int closestDistance = Integer.MAX_VALUE;
            for (Iterator iterB = molB.iterator(); iterB.hasNext(); ) {
                indexCount++;
                Molecule b = (Molecule) iterB.next();
                int d = sqrDistance(a, b);
                if (d < closestDistance) {
                    closestDistance = d;
                    indexClosest = indexCount;
                }
            }

            // See if it's close enough.
            if ((closestDistance < closenessDistance) &&
                    (UniformRandom.uniform() < p)) {
                // See if there are enough remaining.
                if ((molA.size() > 1) && (molB.size() > 1)) {
                    // Make a C molecule. Remove the A and B molecules.
                    iterA.remove();
                    Molecule b = (Molecule) molB.remove(indexClosest);
                    int x = (int) ((a.x + b.x) / 2.0);
                    int y = (int) ((a.y + b.y) / 2.0);
                    Molecule c = new Molecule(x, y);
                    molC.add(c);
                }
            } // end-if

        } // end-iterA

        // See if there are enough C molecules.
        if (molC.size() <= 1) {
            return;
        }

        // For every C molecule:
        for (Iterator iterC = molC.iterator(); iterC.hasNext(); ) {

            Molecule c = (Molecule) iterC.next();

            // Count molecules close enough.
            int count = countCloseMolecules(molA, c);
            count += countCloseMolecules(molB, c);
            count += countCloseMolecules(molC, c);
            if ((count >= 1) && (UniformRandom.uniform() < q)) {
                // Remove and make A,B
                iterC.remove();
                Molecule a = new Molecule(c.x, c.y);
                molA.add(a);
                Molecule b = new Molecule(c.x, c.y);
                molB.add(b);
            }

        } //for-C

    }


    // Given a molecule c, count the number of other molecules
    // that are "closeby", i.e., within distance "closenessDistance".

    int countCloseMolecules(LinkedList mol, Molecule c) {
        int count = 0;
        for (Iterator iter = mol.iterator(); iter.hasNext(); ) {
            Molecule m = (Molecule) iter.next();
            if (!m.equals(c)) {
                int d = sqrDistance(m, c);
                if (d < closenessDistance) {
                    count++;
                }
            }
        }
        return count;
    }


    //------------------------------------------------------------------
    // Simulation: multiple runs

    void simulate() {
        try {
            // First extract simulation parameters.
            int newRuns = Integer.parseInt(numRunsField.getText());
            if (newRuns > 0) {
                numRuns = newRuns;
            }

            int newEndTime = Integer.parseInt(endTimeField.getText());
            if (newEndTime > 0) {
                endTime = newEndTime;
            }

            runSimulation();

            status("Starting simulation: numRuns=" + numRuns + " endTime=" + endTime);
        } catch (NumberFormatException e) {
            error("Improper integer in runs field");
        }
    }


    void runSimulation() {
        // Make and initialize vectors.
        int vectorSize = (endTime / timeInterval);
        concAPoints = makePointVector(vectorSize, timeInterval);
        concBPoints = makePointVector(vectorSize, timeInterval);
        concCPoints = makePointVector(vectorSize, timeInterval);

        int run = 0;

        while (run < numRuns) {

            runLabel.setText("Run: " + run);
            initializeMolecules();
            int index = 0;

            // Do a run.
            for (currentTime = 0; currentTime < endTime; currentTime++) {
                timeLabel.setText("Time: " + currentTime);
                if (model == SPATIAL) {
                    moveMolecules(molA);
                    moveMolecules(molB);
                    moveMolecules(molC);
                }
                doReactions();
                if (currentTime % timeInterval == 0) {
                    // Stats.
                    computeConcentrations();
                    Point2D.Double pA = (Point2D.Double) concAPoints.get(index);
                    Point2D.Double pB = (Point2D.Double) concBPoints.get(index);
                    Point2D.Double pC = (Point2D.Double) concCPoints.get(index);
                    // Accumulate totals.
                    if ((pA != null) && (pB != null) && (pC != null)) {
                        pA.y += concA;
                        pB.y += concB;
                        pC.y += concC;
                    }
                    // We need the index to get the right total to add to.
                    index++;
                } // end-if
            } // end-for

            run++;

        } // end-while

        // Compute average across runs.
        computeAverage(concAPoints, numRuns);
        computeAverage(concBPoints, numRuns);
        computeAverage(concCPoints, numRuns);

        // Plot the curves.
        plot();
    }


    // Initialize the totals for averaging across runs.

    Vector makePointVector(int size, int timeInterval) {
        Vector v = new Vector();
        for (int i = 0; i < size; i++) {
            v.add(new Point2D.Double((double) i * timeInterval, 0.0));
        }
        return v;
    }


    void computeAverage(Vector v, int numRuns) {
        for (int i = 0; i < v.size(); i++) {
            Point2D.Double p = (Point2D.Double) v.get(i);
            p.y = p.y / (double) numRuns;
        }
    }


}
