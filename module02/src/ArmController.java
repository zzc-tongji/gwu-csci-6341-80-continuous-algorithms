public interface ArmController {

    public void init(int numLinks, boolean isTorque);

    public double[] getDeltaAngles();

    public double[] getTorques();

}
