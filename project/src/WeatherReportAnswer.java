import java.util.ArrayList;

public class WeatherReportAnswer {

    public static void main(String[] args) throws Exception {
        final int maxTrial = 1000;
        final int stateNumber = 3;
        /*
         * initial state
         *
         * - sunny:
         *   new double[][]{
         *     {1},
         *     {0},
         *     {0}
         *   }
         *
         * - cloudy:
         *   new double[][]{
         *     {0},
         *     {1},
         *     {0}
         *   }
         *
         * - rainy:
         *   new double[][]{
         *     {0},
         *     {0},
         *     {1}
         *   }
         *
         * */
        Matrix initialState = new Matrix(new double[][]{
                {1},
                {0},
                {0}
        });
        Matrix transitionMatrix = new Matrix(new double[][]{
                {0.65, 0.25, 0.25},
                {0.1, 0.25, 0.15},
                {0.25, 0.5, 0.6}
        });
        Matrix previousState;
        Matrix state = initialState;
        ArrayList<Function> functionList = new ArrayList<>();
        functionList.add(new Function("State 1 (Sunny)", "Day"));
        functionList.add(new Function("State 2 (Cloudy)", "Day"));
        functionList.add(new Function("State 3 (Rainy)", "Day"));
        for (int i = 0; i < stateNumber; i++) {
            functionList.get(i).add(0, state.getValue(0, 0));
        }
        // # Day 0
        System.out.println("# Day 0");
        System.out.println(state);
        System.out.println();
        for (int i = 1; i <= maxTrial; i++) {
            previousState = state;
            state = Matrix.matrixMultiply(transitionMatrix, previousState);
            System.out.println("# Day " + i);
            System.out.println(state);
            System.out.println();
            for (int j = 0; j < stateNumber; j++) {
                functionList.get(j).add(i, state.getValue(j, 0));
            }
            if (state.equals(previousState)) {
                System.out.println("The stationary distribution appears at day " + i + ".");
                Function.show(functionList.get(0), functionList.get(1), functionList.get(2));
                return;
            }
        }
        System.out.println("The stationary distribution does not appears after " + maxTrial + " day(s).");
        Function.show(functionList.get(0), functionList.get(1), functionList.get(2));
    }

}
