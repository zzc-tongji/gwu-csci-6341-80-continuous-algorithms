/**
 * Each instance of <code>MazeState</code> describes a single state
 * of the Maze problem.
 *
 * @see State
 */

public class MazeState extends State {

    // Pointer to parent. This needs to be set by the appropriate problem,
    // in this case, in MazeProblem.
    MazeState parent = null;

    // Size.
    int N = -1;

    // Location.
    int x = -1, y = -1;


    public MazeState(MazeState parent, int N, int x, int y) {
        this.parent = parent;
        this.N = N;
        this.x = x;
        this.y = y;
    }


    public State getParent() {
        return parent;
    }


    public boolean equals(Object obj) {
        if (!(obj instanceof MazeState)) {
            return false;
        }
        MazeState m = (MazeState) obj;
        if ((m.N == N) && (m.x == x) && (m.y == y)) {
            return true;
        }
        return false;
    }


    public String toString() {
        String str = "MazeState: [N=" + N + ", x=" + x + ", y=" + y + ", cost=" + costFromStart + " est=" + estimatedCostToGoal + "]";
        return str;
    }

}
