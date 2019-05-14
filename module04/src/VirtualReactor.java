import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;
import java.text.*;

public class VirtualReactor extends JFrame implements WindowListener {

    // The frame consists of a single container and a cardlayout.
    JPanel mainPanel;
    CardLayout card;

    // Individual experiments/simulators:
    MolecularSimPanel standard;
    MolecularSimPanel spatial;
    ODESimPanel odeSim;
    NonlinearReactionPanel brusselator;
    NonlinearReactionPanel rabbitLynx;
    BooleanPanel bool;
    BooleanPanel boolCellCycle;


    //------------------------------------------------------------------
    // Constructor

    public VirtualReactor() {
        this.setTitle("ReactionSim");
        this.setBackground(Color.white);
        this.setResizable(true);
        this.setSize(700, 700);

        Container cPane = this.getContentPane();

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(makeModelMenu());
        this.setJMenuBar(menuBar);

        mainPanel = new JPanel();
        card = new CardLayout();
        mainPanel.setLayout(card);
        mainPanel.setOpaque(false);

        // A+B <-> C
        standard = new MolecularSimPanel(MolecularSimPanel.STANDARD);
        spatial = new MolecularSimPanel(MolecularSimPanel.SPATIAL);
        mainPanel.add(standard, "1");
        mainPanel.add(spatial, "2");

        // ODE
        odeSim = new ODESimPanel();
        mainPanel.add(odeSim, "3");

        // Nonlinear
        brusselator = new NonlinearReactionPanel(NonlinearReactionPanel.BRUSSELATOR);
        rabbitLynx = new NonlinearReactionPanel(NonlinearReactionPanel.RABBIT_LYNX);
        mainPanel.add(rabbitLynx, "5");
        mainPanel.add(brusselator, "4");

        // Boolean
        bool = new BooleanPanel(BooleanPanel.TEST);
        mainPanel.add(bool, "6");
        boolCellCycle = new BooleanPanel(BooleanPanel.CELL_CYCLE);
        mainPanel.add(boolCellCycle, "7");

        cPane.add(mainPanel);

        this.setVisible(true);
    }


    JMenu makeModelMenu() {
        JMenu modelMenu = new JMenu("Model");

        JMenuItem m = new JMenuItem("A+B <-> C: Standard Simulation");
        m.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        card.show(mainPanel, "1");
                    }
                }
        );
        modelMenu.add(m);

        m = new JMenuItem("A+B <-> C: Spatial Simulation");
        m.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        card.show(mainPanel, "2");
                    }
                }
        );
        modelMenu.add(m);

        m = new JMenuItem("A+B <--> C: ODE Simulation");
        m.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        card.show(mainPanel, "3");
                    }
                }
        );
        modelMenu.add(m);

        m = new JMenuItem("Brusselator reaction");
        m.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        card.show(mainPanel, "4");
                    }
                }
        );
        modelMenu.add(m);

        m = new JMenuItem("Rabbit-Lynix \"reaction\"");
        m.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        card.show(mainPanel, "5");
                    }
                }
        );
        modelMenu.add(m);

        m = new JMenuItem("Boolean Network: Example");
        m.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        card.show(mainPanel, "6");
                    }
                }
        );
        modelMenu.add(m);

        m = new JMenuItem("Boolean Network: Cell-Cycle");
        m.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        card.show(mainPanel, "7");
                    }
                }
        );
        modelMenu.add(m);

        m = new JMenuItem("Exit");
        m.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent a) {
                        System.exit(0);
                    }
                }
        );
        modelMenu.add(m);

        return modelMenu;
    }


    // Implementation of WindowListener interface:

    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }

    public void windowClosed(WindowEvent e) {
        System.exit(0);
    }

    // Empty implementations of methods we don't need.
    public void windowOpened(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }


    public static void main(String[] argv) {
        new VirtualReactor();
    }

}
