public class Exercise2 {
    public static void main(String[] args) {
        double[][] transition = {
                {0.9, 0.075, 0.025},
                {0.15, 0.8, 0.05},
                {0.25, 0.25, 0.5}
        };

        double[] prev = {0.1, 0.2, 0.7};
        double[] next;

        int num = 30;

        for (int cnt = 0; cnt < 5; cnt++) {
            //  Set your initial state of the distribution

            for (int k = 0; k < num; k++) {
                // Calculate the next state using previous state and transition matrix
            }

        }

    }

}
