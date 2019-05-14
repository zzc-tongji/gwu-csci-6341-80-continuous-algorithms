import java.util.*;

public class PuzzleHandPlanner implements Planner {

    public LinkedList<State> makePlan(PlanningProblem problem, State start) {
        LinkedList<State> plan = new LinkedList<State>();

        int[][] grid = {
                {4, 2, 6},
                {7, 1, 8},
                {5, 3, 0}
        };
        PuzzleState s = new PuzzleState(null, 3, grid);
        plan.add(s);


        grid = new int[][]{
                {4, 2, 6},
                {7, 1, 0},
                {5, 3, 8}
        };
        s = new PuzzleState(null, 3, grid);
        plan.add(s);

        grid = new int[][]{
                {4, 2, 0},
                {7, 1, 6},
                {5, 3, 8}
        };
        s = new PuzzleState(null, 3, grid);
        plan.add(s);

        grid = new int[][]{
                {4, 0, 2},
                {7, 1, 6},
                {5, 3, 8}
        };
        s = new PuzzleState(null, 3, grid);
        plan.add(s);

        return plan;
    }

}
