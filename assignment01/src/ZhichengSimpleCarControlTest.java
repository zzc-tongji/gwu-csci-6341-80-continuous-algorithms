public class ZhichengSimpleCarControlTest {

    public static void main(String[] argv) {
        // initlize
        //
        // controller for simple car (not unicycle), no acceleration
        SimpleCarSimulator simulator = new SimpleCarSimulator(false, false);
        simulator.init(50, 50, 0, null);
        // turn left
        simulator.nextStep(10, 1, 0.6);
        System.out.println("t=" + simulator.getTime() + " x=" + simulator.getX() + " y=" + simulator.getY() + " theta=" + simulator.getTheta());
        // move forward
        simulator.nextStep(10, 0, 8);
        System.out.println("t=" + simulator.getTime() + " x=" + simulator.getX() + " y=" + simulator.getY() + " theta=" + simulator.getTheta());
        // turn right
        simulator.nextStep(10, -1, 0.6);
        System.out.println("t=" + simulator.getTime() + " x=" + simulator.getX() + " y=" + simulator.getY() + " theta=" + simulator.getTheta());
        // move forward
        simulator.nextStep(10, 0, 18);
        System.out.println("t=" + simulator.getTime() + " x=" + simulator.getX() + " y=" + simulator.getY() + " theta=" + simulator.getTheta());
        // turn right
        simulator.nextStep(10, -1, 0.6);
        System.out.println("t=" + simulator.getTime() + " x=" + simulator.getX() + " y=" + simulator.getY() + " theta=" + simulator.getTheta());
        // move forward
        simulator.nextStep(10, 0, 8);
        System.out.println("t=" + simulator.getTime() + " x=" + simulator.getX() + " y=" + simulator.getY() + " theta=" + simulator.getTheta());
        // turn left
        simulator.nextStep(10, 1, 0.6);
        System.out.println("t=" + simulator.getTime() + " x=" + simulator.getX() + " y=" + simulator.getY() + " theta=" + simulator.getTheta());
        // move forward
        simulator.nextStep(10, 0, 15.6);
        System.out.println("t=" + simulator.getTime() + " x=" + simulator.getX() + " y=" + simulator.getY() + " theta=" + simulator.getTheta());
    }

}