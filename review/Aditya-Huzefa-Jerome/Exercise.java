import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

class HeatExample {

    int L = 5;
    int H = 5;
    
    double tempI = 0;
    
    double tempL = 0;
    double tempT = 100;
    double tempB = 100;
    double tempR = 0;

    double[][] matrix;

	static double ALPHA_COPPER = 1.11E-4;
	static double ALPHA_ALUMINIUM = 9.7E-5;
	static double ALPHA_WATER = 1.43E-7;
	static double ALPHA_AIR = 1.9E-5;
	static double ALPHA_OIL = 7.38E-8;
	
    double alpha = 0.000111;
    double dx = 0.0025, dy = 0.0025;
    double dt = 0.01;
    double rx, ry;
    
	double maxT = 100;
    double minT = 0;
    
    int it = 0;
    double epsilon = 1E-4;
    boolean unstable = true, running = false;
    
	HeatExample(double alpha, double epsilon) {
		this.alpha = alpha;
		this.epsilon = epsilon;
        rx = alpha * dt / (dx * dx);
        ry = alpha * dt / (dy * dy);
        createMatrix();
    }
    
    void createMatrix() {
        matrix = new double[L][H];

        for (int i = 0; i < L; i++) {
            for (int j = 0; j < H; j++) {
                matrix[i][j] = tempI * 1.0;
            }
        }

        for (int i = 0; i < L; i++) {
            matrix[i][0] = tempT;
            matrix[0][i] = tempL;
            matrix[L - 1][i] = tempR;
			matrix[i][L - 1] = tempB;
        }
    }
	
	double[][] getClone(double[][] matrix) {
		double[][] clone = new double[L][H];
        for (int i = 0; i < L; i++) {
            for (int j = 0; j < H; j++) {
            	clone[i][j] = matrix[i][j];
            }
        }
		return clone;
	}
	
	void printMatrix() {
		
		// Ignore values at corners
		System.out.println();
		for (int j = 0; j < L; j++) {
            for (int i = 0; i < H; i++) {
            	System.out.print(matrix[i][j]+ " ");
            }
			System.out.println();
        }
		System.out.println();
	}

    boolean updateMatrix() {
    	// Create a clone of matrix
        double[][] clone = getClone(matrix);

        // Update using the implicit method
        for (int j = 1; j < H - 1; j++) {
            for (int i = 1; i < L - 1; i++) {

				// TODO:
				// Implement the equation and update the matrix accordingly


				matrix[j][i]=clone[j][i]+rx*(clone[j][i+1]-2*clone[j][i]+clone[j][i-1])+ry*(clone[j+1][i]-2*clone[j][i]+clone[j-1][i]);
				
               	
            }
        }
        
        // Check for maximum corresponding difference, return false if it is less than epsilon
        double e = getMaxCorrespondingDifference(matrix, clone);
        if (e <= epsilon && e != -Double.MAX_VALUE)
            return false;
		return true;

    }

    double getMaxCorrespondingDifference(double[][] matrix, double[][] clone) {
        double maxError = -Double.MAX_VALUE;
		
		// TODO:
        // calculate and return the maximum difference between the corresponding values of matrix and clone
		// refer to the pseudocode for help
		for (int i=0;i<L;i++)
		{
			for (int j=0; j<H; j++) 
			{
				double e=Math.abs(clone[i][j] - matrix[i][j]);
				if (e > maxError && e != 0.0) maxError = e;
				
			}
		}
		
		
		
		
        return maxError;
    }
    
}


public class Exercise {	
	public static void main(String... args){
		HeatExample he = new HeatExample(HeatExample.ALPHA_COPPER, 1E-5);
		he.printMatrix();
		while (he.updateMatrix()) {
			he.printMatrix();
		}
		he.printMatrix();
	}
}