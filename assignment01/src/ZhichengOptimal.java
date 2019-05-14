public class ZhichengOptimal {
    public static void main(String[] argv) {
        /*
         * Variable `result` is an array of integer, for each item:
         * - if the item represents a valid solution, its value is the global time (ms).
         * - else, its value is `Math.Integer.MAX_VALUE`.
         *
         * 1st index: A [0-32]
         * range: 0.00 - 1.60 rad
         * interval: 0.05
         *
         * 2nd index: v1 [0-10]
         * range: 0 - 10
         * interval: 1
         *
         * 3rd index: v2 [0-10]
         * range: 0 - 10
         * interval: 1
         *
         * */
        int[][][] result = new int[33][11][11];
        ZhichengSolution solution;

        System.out.println();
        System.out.println("Calculating...");
        for (int i = 0; i < 33; i++) {
            System.out.println(" A = " + 5.0 * i / 100.0);
            for (int j = 0; j < 11; j++) {
                for (int k = 0; k < 11; k++) {
                    solution = new ZhichengSolution(5 * i, j, k);
                    if (solution.run()) {
                        result[i][j][k] = solution.getGlobalTime();
                    } else {
                        result[i][j][k] = Integer.MAX_VALUE;
                    }
                }
            }
        }
        System.out.println();

        System.out.println("Comparing...");
        int[] selectedItem = new int[3];
        int selectedValue = Integer.MAX_VALUE;
        for (int i = 0; i < 33; i++) {
            System.out.println(" A = " + 5.0 * i / 100.0);
            for (int j = 0; j < 11; j++) {
                for (int k = 0; k < 11; k++) {
                    if (result[i][j][k] < selectedValue) {
                        selectedValue = result[i][j][k];
                        selectedItem[0] = i;
                        selectedItem[1] = j;
                        selectedItem[2] = k;
                    }
                }
            }
        }
        System.out.println();

        System.out.println("Parameters of Optimal Solution:");
        System.out.println(" A = " + 5.0 * selectedItem[0] / 100.0 + "rad");
        System.out.println("v1 = " + selectedItem[1]);
        System.out.println("v2 = " + selectedItem[2]);
        System.out.println();

        solution = new ZhichengSolution(5 * selectedItem[0], selectedItem[1], selectedItem[2]);
        solution.run();
        System.out.println("Steps of Optimal Solution: (" + solution.getGlobalTime() + "ms)");
        System.out.println(solution.getStepList());
        System.out.println();
    }
}
