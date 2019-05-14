// Template for implementing Cost-Based Planner.

import java.util.*;


public class ZhichengCBPlannerAStar {

    // Limit the total number of expansions.
    static int maxMoves = 100000;

    // The frontier = all those states that have been generated but not
    // yet explored (visited).
    LinkedList<ZhichengState> frontier;

    // The list of all states that have been visited.
    LinkedList<ZhichengState> visitedStates;

    // Count # moves.
    int numMoves;


    public LinkedList<ZhichengState> makePlan(ZhichengProblem problem) {
        // Initialize.
        frontier = new LinkedList<ZhichengState>();
        visitedStates = new LinkedList<ZhichengState>();
        numMoves = 0;

        // The start node is the first one to place in frontier.
        frontier.add(problem.start);

        while (numMoves < maxMoves) {

            // If nothing to explore, we're done.
            if (frontier.size() == 0) {
                break;
            }

            // Get first node in frontier and expand.
            ZhichengState currentState = removeBest();
            // problem.drawState (currentState);

            // If we're at a goal node, build the solution.
            if (problem.satisfiesGoal(currentState)) {
                return makeSolution(currentState);
            }

            numMoves++;

            // Put in visited list.
            visitedStates.add(currentState);

            // Expand current state (look at its neighbors) and place in frontier.
            ArrayList<ZhichengState> neighbors = problem.getNeighbors(currentState);
            for (ZhichengState s : neighbors) {
                if (!visitedStates.contains(s)) {
                    int index = frontier.indexOf(s);
                    if (index >= 0) {
                        ZhichengState altS = frontier.get(index);
                        if (s.costFromStart < altS.costFromStart) {
                            frontier.set(index, s);
                        }
                    } else {
                        frontier.add(s);
                    }
                }
            }

            if (numMoves % 100 == 0) {
                System.out.println("After " + numMoves + ": |F|=" + frontier.size() + "  |V|=" + visitedStates.size());
            }

        } // endwhile

        System.out.println("Cost-based: No solution found after " + numMoves + " moves");
        return null;
    }


    ZhichengState removeBest() {
        // INSERT YOUR CODE HERE
        // Pick the state s with the least s.costFromStart
        int selectedIndex = 0;
        for (int i = 0; i < frontier.size(); i++) {
            if (frontier.get(i).costFromStart + frontier.get(i).estimatedCostToGoal < frontier.get(selectedIndex).costFromStart + frontier.get(selectedIndex).estimatedCostToGoal) {
                selectedIndex = i;
            }
        }
        return frontier.remove(selectedIndex);
    }


    public LinkedList<ZhichengState> makeSolution(ZhichengState goalState) {
        LinkedList<ZhichengState> solution = new LinkedList<>();
        solution.add(goalState);

        // Start from the goal and work backwards, following
        // parent pointers.
        ZhichengState currentState = goalState;
        while (currentState.getParent() != null) {
            solution.addFirst(currentState.getParent());
            currentState = currentState.getParent();
        }

        System.out.println("Cost: Solution of length=" + solution.size() + " found with cost=" + goalState.costFromStart + " after " + numMoves + " moves");

        return solution;
    }

}
