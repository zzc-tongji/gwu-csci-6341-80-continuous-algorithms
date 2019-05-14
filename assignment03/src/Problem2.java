import javax.swing.*;
import java.awt.*;

class DrawPanel2 extends JPanel {

    // time
    double t = 0;
    double delT = 0.001;
    // constant
    double g = -9.8;
    double k1 = 0.268;
    double k2 = 1.342;
    double T = 12.875;
    // parameter
    double d = 365.760;
    double v = 0;
    double a = g;

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // clear
        Dimension D = this.getSize();
        g.setColor(Color.white);
        g.fillRect(0, 0, D.width, D.height);
        // draw
        g.setColor(Color.red);
        g.fillOval(D.width / 2, 390 - (int) d, 10, 10);
    }

    void reset() {
        t = 0;
        d = 365.760;
        v = 0;
        a = g;
        Thread t = new Thread(() -> animate());
        t.start();
    }

    void animate() {
        Function D = new Function("Displacement");
        Function V = new Function("Velocity");
        Function A = new Function("Acceleration");
        while (d >= 0) {
            t = t + delT;
            if (t < T) {
                a = g - k1 * v;
            } else {
                a = g - k2 * v;
            }
            v += delT * a;
            d += delT * v;
            D.add(t, d);
            V.add(t, v);
            A.add(t, a);
            this.repaint();
            sleep();
        }
        System.out.println("Finally:");
        System.out.println("a = " + a + "m/s^2");
        System.out.println("v = " + v + "m/s");
        System.out.println("d = " + d + "m");
        Function.show(D);
        Function.show(V);
        Function.show(A);
    }

    void sleep() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

}


class Problem2 extends JFrame {

    DrawPanel2 drawPanel2;

    public Problem2() {
        this.setSize(200, 500);
        Container cPane = this.getContentPane();
        drawPanel2 = new DrawPanel2();
        JPanel panel = new JPanel();
        JButton resetB = new JButton("Reset");
        resetB.addActionListener(a -> drawPanel2.reset());
        panel.add(resetB);
        panel.add(new JLabel("    "));
        JButton quitB = new JButton("Quit");
        quitB.addActionListener(a -> System.exit(0));
        panel.add(quitB);
        cPane.add(panel, BorderLayout.SOUTH);
        cPane.add(drawPanel2, BorderLayout.CENTER);
        this.setVisible(true);
    }

    public static void main(String[] argv) {
        new Problem2();
    }

}
