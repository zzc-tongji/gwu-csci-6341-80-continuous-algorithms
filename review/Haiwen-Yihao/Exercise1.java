public class Exercise1 {
    public static void main(String[] args) {
        double[][] transition = {
                {0.9, 0.075, 0.025},
                {0.15, 0.8, 0.05},
                {0.25, 0.25, 0.5}
        };

        double[] prev = {0.1, 0.2, 0.7};
        double[] next = {0, 0, 0};

        int num = 30;

        Function f1 = new Function("f1");
        Function f2 = new Function("f2");
        Function f3 = new Function("f3");

        for (int cnt = 0; cnt < 5; cnt++) {
            //  Set your initial state of the distribution
            prev[0] = 0.1 + 0.05 * cnt;
            prev[1] = 0.2 + 0.05 * cnt;
            prev[2] = 0.7 - 0.1 * cnt;

        

            for (int k = 0; k < num; k++) {
                // Calculate the next state using previous state and transition matrix
                if( cnt == 0){
                    f1.add(k, next[0]);
                    f1.add(k, next[1]);
                    f1.add(k, next[2]);
                }

                 for (int i = 0; i < 3; i++) {
                    next[i] = 0;
                }

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        next[i] += prev[j] * transition[j][i];
                    }
                }
                    f1.add(k, next[0]);
                    f1.add(k, next[1]);
                    f1.add(k, next[2]);
            }

        }


        f1.show();
        f2.show();
        f3.show();

    }

}
