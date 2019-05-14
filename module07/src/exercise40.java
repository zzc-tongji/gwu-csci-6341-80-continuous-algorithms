public class exercise40 {

    public static void main(String[] argv) {
        QueueControl queueControl = new QueueControl(false);
        PropHistogram histogram = new PropHistogram(0, 10, 20);
        queueControl.reset();
        for (int i = 0; i < 1000; i++) {
            queueControl.nextStep();
            histogram.add(queueControl.avgSystemTime);
        }
        histogram.display();
    }

}
