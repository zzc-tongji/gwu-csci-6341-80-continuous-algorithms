public class Raindrop {

    public static void main(String[] argv) {
        double numTrials = 100000;
        double s = 1;
        double p = 0.5;
        PropHistogram histogramX = new PropHistogram(-15, 15, 30);
        PropHistogram histogramT = new PropHistogram(0, 40, 40);
        double sumT = 0;
        for (int n = 0; n < numTrials; n++) {
            double x = 0;
            double h = 10;
            double t = 0;
            while (h > 0) {
                if (UniformRandom.uniform() < p) {
                    h -= s;
                } else if (UniformRandom.uniform() < 0.5) {
                    x += s;
                } else {
                    x -= s;
                }
                t += 1;
            }
            histogramX.add(x);
            histogramT.add(t);
            sumT += t;
        }
        //histogramX.display();
        //histogramT.display();
        System.out.println("E[T] = " + sumT / numTrials);
    }

}