import java.util.*;

public class MazeHandPlanner implements Planner {

    public LinkedList<State> makePlan(PlanningProblem problem, State start) {
        LinkedList<State> plan = new LinkedList<State>();
        State s = new MazeState(null, 10, 1, 1);
        plan.add(s);
        plan.add(new MazeState(null, 10, 1, 2));
        plan.add(new MazeState(null, 10, 1, 3));
        plan.add(new MazeState(null, 10, 2, 3));
        plan.add(new MazeState(null, 10, 3, 3));
        plan.add(new MazeState(null, 10, 4, 3));
        return plan;
    }

}
