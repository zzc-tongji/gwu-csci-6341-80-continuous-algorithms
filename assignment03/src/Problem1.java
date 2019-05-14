import javax.swing.*;
import java.awt.*;

class DrawPanel1 extends JPanel {

    // time
    double t = 0;
    double delT = 0.001;
    // parameter of the speeding Driver
    double d1 = 0.01;
    double v1 = 44.704;
    // parameter of the state trooper
    double d2 = 0;
    double v2 = 0;
    double a2 = 5.364;

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // clear
        Dimension D = this.getSize();
        g.setColor(Color.white);
        g.fillRect(0, 0, D.width, D.height);
        // draw the speeding driver
        g.setColor(Color.red);
        g.fillOval(20 + (int) d1, (int) D.height / 3, 10, 10);
        // draw the state trooper
        g.setColor(Color.blue);
        g.fillOval(20 + (int) d2, (int) D.height * 2 / 3, 10, 10);
    }

    void reset() {
        t = 0;
        d1 = 0.01;
        d2 = 0;
        v2 = 0;
        Thread t = new Thread(() -> animate());
        t.start();
    }

    void animate() {
        Function D1 = new Function("Displacement of the Speeding Driver");
        Function D2 = new Function("Displacement of the State Trooper");
        Function V1 = new Function("Velocity of the Speeding Driver");
        Function V2 = new Function("Velocity of the State Trooper");
        while (d1 > d2) {
            t = t + delT;
            d1 += delT * v1;
            v2 += delT * a2;
            d2 += delT * v2;
            D1.add(t, d1);
            D2.add(t, d2);
            V1.add(t, v1);
            V2.add(t, v2);
            this.repaint();
            sleep();
        }
        Function.show(D1, D2);
        Function.show(V1, V2);
    }

    void sleep() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

}


class Problem1 extends JFrame {

    DrawPanel1 drawPanel1;

    public Problem1() {
        this.setSize(800, 200);
        Container cPane = this.getContentPane();
        drawPanel1 = new DrawPanel1();
        JPanel panel = new JPanel();
        JButton resetB = new JButton("Reset");
        resetB.addActionListener(a -> drawPanel1.reset());
        panel.add(resetB);
        panel.add(new JLabel("    "));
        JButton quitB = new JButton("Quit");
        quitB.addActionListener(a -> System.exit(0));
        panel.add(quitB);
        cPane.add(panel, BorderLayout.SOUTH);
        cPane.add(drawPanel1, BorderLayout.CENTER);
        this.setVisible(true);
    }

    public static void main(String[] argv) {
        new Problem1();
    }

}
