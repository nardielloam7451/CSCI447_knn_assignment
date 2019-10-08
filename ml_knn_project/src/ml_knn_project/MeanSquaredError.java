package ml_knn_project;

import java.util.ArrayList;//imports the array list data structure, used to store the test set

public class MeanSquaredError implements LossFunction {//implements the mean squared error loss funciton for regression analysis. 
	CSVReader writer = new CSVReader("Results_mseLoss.csv");
	String algorithmName;
	String dataSetName;
	String hyperParams;
	ArrayList<String[]> results = new ArrayList<String[]>();
	final int FOLDNUMBER = 0;
	final int GUESS = 1;
	final int ACTUAL = 2;
	boolean verbosePrinting = false;
	public MeanSquaredError(String algorithmName, String dataSetName, String hyperParams) {
		this.algorithmName = algorithmName;
		this.dataSetName = dataSetName;
		this.hyperParams = hyperParams;
	}
	
	public MeanSquaredError(boolean verbosePrinting, String algorithmName, String dataSetName, String hyperParams) {
		this.algorithmName = algorithmName;
		this.dataSetName = dataSetName;
		this.hyperParams = hyperParams;
		this.verbosePrinting = verbosePrinting;
	}
	
	public void addResult(int foldNumber, String guess, String actual) {
		String[] resultEntry = new String[3];
		resultEntry[FOLDNUMBER] = String.format("%d", foldNumber);
		resultEntry[GUESS] = guess;
		resultEntry[ACTUAL] = actual;
		results.add(resultEntry);
	}
	
	public void writeResults() {
		String lastFoldNumber = results.get(0)[FOLDNUMBER];
		int matchCount = 0;
		int mismatchCount = 0;
		double mse = 0;
		ArrayList<Double> meanSquaredErrorPerFold = new ArrayList<Double>();
		// Get the results for 0/1 accuracy over every fold
		int foldCounter = 0;
		for (int i = 0; i < results.size(); i++) {
			String currentFoldNumber = results.get(i)[FOLDNUMBER];
			if (!lastFoldNumber.equals(currentFoldNumber)) {
				//System.err.println("HIT");
				
				if(verbosePrinting) {
					System.out.printf("MSE for fold %d is %f%n", i, Math.pow(mse, 0.5) * 1/foldCounter);
				}
				meanSquaredErrorPerFold.add(Math.pow(mse, 0.5) * 1/foldCounter);
				mse = 0;
				foldCounter = 0;
				lastFoldNumber = currentFoldNumber;
			} 
			double guessResult =  Double.parseDouble(results.get(i)[GUESS]);
			double actualResult = Double.parseDouble(results.get(i)[ACTUAL]);
			double squaredDifference = Math.pow(guessResult-actualResult, 2);
			mse += squaredDifference;
			foldCounter++;
		}
		
		// Average the results for 0/1 accuracy across all of the folds
		double accuracyAverage = 0;
		for (int i = 0; i < meanSquaredErrorPerFold.size(); i++) {
			accuracyAverage += meanSquaredErrorPerFold.get(i).doubleValue();
		}
		accuracyAverage = accuracyAverage / meanSquaredErrorPerFold.size();
		System.out.printf("%s accuracy average = %f | correctGuessRatioPerFold.size() = %d%n", algorithmName, accuracyAverage, meanSquaredErrorPerFold.size()); // DELETE
		String stringToWrite = String.format("%s,%s,%s,%f", algorithmName, dataSetName, hyperParams, accuracyAverage);
		writer.writer(stringToWrite);
	}
}