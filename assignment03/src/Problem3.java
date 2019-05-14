import java.util.LinkedList;

class Problem3 implements Planner {

    boolean hasPreplaned;
    LinkedList<State> plan1;
    LinkedList<State> plan2;
    LinkedList<State> plan3;
    LinkedList<State> plan4;
    LinkedList<State> plan5;

    public Problem3() {
        hasPreplaned = false;
    }

    public LinkedList<State> makePlan(PlanningProblem problem, State start) {

        Planner aStar = new CBPlannerAStar();
        ArmProblem armProblem = (ArmProblem) problem;
        double targetX = armProblem.targetX;
        double targetY = armProblem.targetY;
        LinkedList<State> planX;
        LinkedList<State> planY = new LinkedList<>();

        if (!hasPreplaned) {
            System.out.println("< Pre-plan start. >");
            // pre-plan
            armProblem.targetX = 140;
            armProblem.targetY = 30;
            System.out.println("# pre-plan 1: [" + armProblem.targetX + ", " + armProblem.targetY + "]");
            plan1 = aStar.makePlan(armProblem, start);
            armProblem.targetX = 180;
            armProblem.targetY = 30;
            System.out.println("# pre-plan 2: [" + armProblem.targetX + ", " + armProblem.targetY + "]");
            plan2 = aStar.makePlan(armProblem, start);
            armProblem.targetX = 220;
            armProblem.targetY = 30;
            System.out.println("# pre-plan 3: [" + armProblem.targetX + ", " + armProblem.targetY + "]");
            plan3 = aStar.makePlan(armProblem, start);
            armProblem.targetX = 260;
            armProblem.targetY = 30;
            System.out.println("# pre-plan 4: [" + armProblem.targetX + ", " + armProblem.targetY + "]");
            plan4 = aStar.makePlan(armProblem, start);
            armProblem.targetX = 300;
            armProblem.targetY = 30;
            System.out.println("# pre-plan 5: [" + armProblem.targetX + ", " + armProblem.targetY + "]");
            plan5 = aStar.makePlan(armProblem, start);
            // reset
            armProblem.targetX = targetX;
            armProblem.targetY = targetY;
            System.out.println("< Pre-plan complete. >");
            hasPreplaned = true;
        }

        System.out.println("< Plan start. >");
        // plan based on pre-plan
        if (armProblem.targetX < 160) {
            System.out.println("Use pre-plan 1.");
            planY.addAll(plan1);
            planX = aStar.makePlan(armProblem, plan1.getLast());
        } else if (armProblem.targetX < 200) {
            System.out.println("Use pre-plan 2.");
            planY.addAll(plan2);
            planX = aStar.makePlan(armProblem, plan2.getLast());
        } else if (armProblem.targetX < 240) {
            System.out.println("Use pre-plan 3.");
            planY.addAll(plan3);
            planX = aStar.makePlan(armProblem, plan3.getLast());
        } else if (armProblem.targetX < 280) {
            System.out.println("Use pre-plan 4.");
            planY.addAll(plan4);
            planX = aStar.makePlan(armProblem, plan4.getLast());
        } else {
            System.out.println("Use pre-plan 5.");
            planY.addAll(plan5);
            planX = aStar.makePlan(armProblem, plan5.getLast());
        }

        // output
        if (planX == null) {
            planY = null;
            System.out.println("No solution found");
        } else {
            planY.addAll(planX);
            System.out.println("Cost: Solution of length=" + planY.size() + " found with cost=" + planY.getLast().costFromStart);
        }
        System.out.println("< Plan complete. >");
        return planY;
    }

}
