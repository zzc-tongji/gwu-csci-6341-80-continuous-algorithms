public class ZhichengDubinCarControlTest {

    public static void main(String[] argv) {
        // initlize
        //
        // controller for dubin car, no acceleration
        DubinCarSimulator simulator = new DubinCarSimulator(false);
        simulator.init(50, 50, 0, null);
        // turn left
        simulator.nextStep(0, 5, 0.6);
        System.out.println("t=" + simulator.getTime() + " x=" + simulator.getX() + " y=" + simulator.getY() + " theta=" + simulator.getTheta());
        // move forward
        simulator.nextStep(10, 10, 3);
        System.out.println("t=" + simulator.getTime() + " x=" + simulator.getX() + " y=" + simulator.getY() + " theta=" + simulator.getTheta());
        // turn right
        simulator.nextStep(5, 0, 0.6);
        System.out.println("t=" + simulator.getTime() + " x=" + simulator.getX() + " y=" + simulator.getY() + " theta=" + simulator.getTheta());
        // move forward
        simulator.nextStep(10, 10, 3);
        System.out.println("t=" + simulator.getTime() + " x=" + simulator.getX() + " y=" + simulator.getY() + " theta=" + simulator.getTheta());
        // turn right
        simulator.nextStep(5, 0, 0.6);
        System.out.println("t=" + simulator.getTime() + " x=" + simulator.getX() + " y=" + simulator.getY() + " theta=" + simulator.getTheta());
        // move forward
        simulator.nextStep(10, 10, 3);
        System.out.println("t=" + simulator.getTime() + " x=" + simulator.getX() + " y=" + simulator.getY() + " theta=" + simulator.getTheta());
        // turn left
        simulator.nextStep(0, 5, 0.6);
        System.out.println("t=" + simulator.getTime() + " x=" + simulator.getX() + " y=" + simulator.getY() + " theta=" + simulator.getTheta());
        // move forward
        simulator.nextStep(10, 10, 1.1);
        System.out.println("t=" + simulator.getTime() + " x=" + simulator.getX() + " y=" + simulator.getY() + " theta=" + simulator.getTheta());
    }

}