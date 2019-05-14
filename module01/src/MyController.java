public class MyController {

    public static void main(String[] argv) {
        // Create an instance of Function and add points.
        Function simpleControl = new Function("Test controller");

        // Add function values.
        simpleControl.add(0, 10);
        simpleControl.add(45, 10);
        simpleControl.add(46, -1);
        simpleControl.add(250, -1);

        // Function does linear interpolation.
        simpleControl.show();

        // Use this function as the acceleration function.
        AccelCar.animateControl(simpleControl);
    }

}
