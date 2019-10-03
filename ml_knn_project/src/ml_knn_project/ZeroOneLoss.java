package ml_knn_project;

import java.text.DecimalFormat;
import java.util.ArrayList; //imports the ArrayList Data structure, which is used to store values from our test set. 

public class ZeroOneLoss {
	//Global Variables
	private ArrayList<ArrayList<Object>> teSet;//the actual test array which is our guesses will be compared against. 
	private int incorrectGuess;//counts the number of incorrect classifications done by the nearest neighbor algorithm. 
	private String testClass; //stores the test class values from the 
	private double calculation;//the actual calculated value, which will return the accuracy of our model. 
	
	public ZeroOneLoss() {
		//the constructor for the Loss_01 class, which takes in the test array and sets our incorrect guess number to zero
		incorrectGuess =0;//sets the incorrect guess number equal to 0
	}
	
	public void evaluation(String guess, String accurateClass) {
		//takes in the actual class name , and the algorithms guess and compares them. 
		testClass=accurateClass;
		if(guess.compareTo(testClass)==0) {//looks to see if the class is correctly identified. If it is, returns 0 and otherwise increase the count of false_value by one and then returns false_value
			return;
		}
		else {
			incorrectGuess++;//this is the count of the false value, increased every time the class is incorrectly identified.
		}
		
	}
	
	public void makeCalculations() {//this method takes in the false values, and then calculates an error rate based upon the false values and the number of total classes in the test value. 
		//calculation=(double)((false_value*100)/(te_matrix.size()));
	}
	
	public void printError() {//this method takes the errorCalculations for a test set, and prints it into the screen. 
		DecimalFormat df= new DecimalFormat("0.00");//this implements the decimal format, and sets it to a 0.00 value. 
		System.out.println("The error rate for the data set is: "+df.format(calculation)+"%");//prints out the percentage value for the error calculation.
	}
}
