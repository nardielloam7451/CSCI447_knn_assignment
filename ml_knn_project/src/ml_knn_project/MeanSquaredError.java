package ml_knn_project;

import java.util.ArrayList;//imports the array list data structure, used to store the test set
import java.Math;//imports the math library for Java, which lets us use the square root and mean methods. 

public class MeanSquaredError {//implements the mean squared error loss funciton for regression analysis. 
	private double sumCalculation;//stores the sum of the subtracted values to make the calculations easier.
	
	public void MeanSquaredError() {
		//the constructor of the Mean Squared Error class, which sets the sumCalculation to zero. 
	}
	public double mseCalculation(ArrayList<ArrayList<Object>> teSet, int featureChoice, ArrayList<double>algGuesses) {
		//takes in the testSet, looks for a specific feature, and preforms the calculation for that feature.
		for(int i =0; i<teSet.length();i++) {
			//goes through the test array, looks at a value at a specific row in a column, and subtracts from that value the algorithm's guess. This is then added to a running sum of the data, which will be used in later calculations. 
			sumCalculation+=pow(((double)teSet.get(i).get(featureChoice))-algGuesses[i],2);
		}
		return(sumCalcuation/teSet.length());//returns the MSE of the regression function
		
	}
	

}
