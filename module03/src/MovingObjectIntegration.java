public class MovingObjectIntegration {

    public static void main(String[] argv) {
        DrawTool.setXYRange(0, 3, 0, 30);
        DrawTool.display();

        double areaSumAcc = 0, areaSumVel = 0;

        double a = 4.9, t = 0, d = 0, v = 0, delT = 0.1;

        while (t <= 3) {
            t = t + delT;
            v = v + delT * a;
            d = d + delT * v;

            System.out.printf("t=%4.2f a=%4.2f v=%4.2f d=%4.2f\n", t, a, v, d);

            //------------------------------------------
            // PART 1: compile and execute.
            // Draw the rectangles for integrating a.
            DrawTool.drawRectangle(t - delT, a, delT, a);
            DrawTool.drawPoint(t, v);

            // WRITE CODE here to compute area of each rectangle:
            double areaAcc = delT * a;

            // Accumulate the sum.
            areaSumAcc = areaSumAcc + areaAcc;
            System.out.printf(" area=%4.2f areaSum=%4.2f v=%4.2f\n", areaAcc, areaSumAcc, v);
            //-------------------------------------------

            // PART 2: comment out all of Part-1 (between the dashed lines).
            // Then, un-comment all the code below.
            // Draw the rectangles for integrating v
            DrawTool.drawRectangle(t - delT, v, delT, v);
            DrawTool.drawPoint(t, d);
            double areaVel = delT * v;
            areaSumVel = areaSumVel + areaVel;
            System.out.printf(" area=%4.2f areaSum=%4.2f d=%4.2f\n", areaVel, areaSumVel, d);
        }
    }

}
