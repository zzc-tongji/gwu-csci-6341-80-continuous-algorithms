import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;


class DrawPanel extends JPanel {

    double t = 0;
    double d = 0;
    double v = 0;
    double a = 4.9;
    double delT = 0.1;

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Clear.
        Dimension D = this.getSize();
        g.setColor(Color.white);
        g.fillRect(0, 0, D.width, D.height);

        g.setColor(Color.red);
        int x = (int) d;
        int y = D.height / 2;
        g.fillOval(x, y, 10, 10);
    }


    void reset() {
        t = 0;
        d = 0;
        v = 0;
        Thread t = new Thread(() -> animate());
        t.start();
    }


    void animate() {
        Function V = new Function("Velocity");
        Function D = new Function("Distance");
        while (t <= 12) {
            t = t + delT;
            v = v + delT * a;
            d = d + delT * v;
            D.add(t, d);
            V.add(t, v);
            this.repaint();
            sleep();
        }
        Function.show(V, D);
    }

    void sleep() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

}


public class MovingObject extends JFrame {

    DrawPanel drawPanel;

    public MovingObject() {
        this.setSize(500, 200);
        Container cPane = this.getContentPane();
        drawPanel = new DrawPanel();

        JPanel panel = new JPanel();
        JButton resetB = new JButton("Reset");
        resetB.addActionListener(a -> drawPanel.reset());
        panel.add(resetB);
        panel.add(new JLabel("    "));
        JButton quitB = new JButton("Quit");
        quitB.addActionListener(a -> System.exit(0));
        panel.add(quitB);
        cPane.add(panel, BorderLayout.SOUTH);
        cPane.add(drawPanel, BorderLayout.CENTER);
        this.setVisible(true);
    }


    public static void main(String[] argv) {
        new MovingObject();
    }


}
