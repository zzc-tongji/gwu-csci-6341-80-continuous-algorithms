import java.util.ArrayList;

public class Maze2 {

    public static void main(String[] args) throws Exception {
        final int maxTrial = 1000;
        final int stateNumber = 4;
        Matrix initialState = new Matrix(new double[][]{
                {1},
                {0},
                {0},
                {0},
        });
        Matrix transitionMatrix = new Matrix(new double[][]{
                {0, 0.5, 0.5, 0},
                {0.5, 0, 0, 0.5},
                {0.5, 0, 0, 0.5},
                {0, 0.5, 0.5, 0}
        });
        Matrix previousState;
        Matrix state = initialState;
        ArrayList<Function> functionList = new ArrayList<>();
        for (int i = 0; i < stateNumber; i++) {
            functionList.add(new Function("State " + (i + 1) + " (Grid " + (i + 1) + ")", "Move"));
        }
        // # Move 0
        System.out.println("# Move 0");
        System.out.println(state);
        System.out.println();
        for (int i = 0; i < stateNumber; i++) {
            functionList.get(i).add(0, state.getValue(0, 0));
        }
        for (int i = 1; i <= maxTrial; i++) {
            previousState = state;
            state = Matrix.matrixMultiply(transitionMatrix, previousState);
            System.out.println("# Move " + i);
            System.out.println(state);
            System.out.println();
            if (i <= 50) {
                for (int j = 0; j < stateNumber; j++) {
                    functionList.get(j).add(i, state.getValue(j, 0));
                }
            }
            if (state.equals(previousState)) {
                System.out.println("The stationary distribution appears at move " + i + ".");
                Function.show(functionList.get(0), functionList.get(1), functionList.get(2), functionList.get(3));
                return;
            }
        }
        System.out.println("The stationary distribution does not appears after " + maxTrial + " move(s).");
        Function.show(functionList.get(0), functionList.get(1), functionList.get(2), functionList.get(3));
    }

}
