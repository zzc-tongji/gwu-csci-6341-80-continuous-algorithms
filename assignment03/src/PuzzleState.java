/**
 * Each instance of <code>PuzzleState</code> describes a single state
 * of the Puzzle problem.
 *
 * @see State
 */

public class PuzzleState extends State {

    // Pointer to parent. This needs to be set by the appropriate problem,
    // in this case, in MazeProblem.
    PuzzleState parent;

    int size;

    // Each state is a configuration of the puzzle.
    int[][] grid;


    public PuzzleState(PuzzleState parent, int size, int[][] grid) {
        this.parent = parent;
        this.size = size;
        this.grid = grid;
    }


    public State getParent() {
        return parent;
    }


    public boolean equals(Object obj) {
        if (!(obj instanceof PuzzleState)) {
            return false;
        }

        // Check whole config.
        PuzzleState p = (PuzzleState) obj;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (p.grid[i][j] != grid[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }


    public String toString() {
        String str = "PuzzleState: size=" + size + ", cost=" + costFromStart + " est=" + estimatedCostToGoal + "\n";
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                str += " " + grid[i][j];
            }
            str += "\n";
        }
        return str;
    }

}
