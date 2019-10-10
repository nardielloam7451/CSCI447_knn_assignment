package ml_knn_project;


import java.util.ArrayList; //imports the ArrayList Data structure, which is used to store values from our test set. 

public class ZeroOneLoss implements LossFunction {
	CSVReader writer = new CSVReader("Results_01loss.csv");
	String algorithmName;
	String dataSetName;
	String hyperParams;
	ArrayList<String[]> results = new ArrayList<String[]>();
	private boolean verbosePrinting;
	final int FOLDNUMBER = 0;
	final int GUESS = 1;
	final int ACTUAL = 2;
	public ZeroOneLoss(String algorithmName, String dataSetName, String hyperParams) {
		this.algorithmName = algorithmName;
		this.dataSetName = dataSetName;
		this.hyperParams = hyperParams;
	}
	
	public ZeroOneLoss(boolean verbosePrinting, String algorithmName, String dataSetName, String hyperParams) {
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
		ArrayList<Double> correctGuessRatioPerFold = new ArrayList<Double>();
		// Get the results for 0/1 accuracy over every fold
		for (int i = 0; i < results.size(); i++) {
			String currentFoldNumber = results.get(i)[FOLDNUMBER];
			if (!lastFoldNumber.equals(currentFoldNumber) || i == results.size() - 1) {
				//System.err.println("HIT");
				if (i == results.size() - 1) {
					if (results.get(i)[GUESS].equals(results.get(i)[ACTUAL])) {
						matchCount++;
					} else {
						mismatchCount++;
					}
				}
				double correctGuessRatio = (double)(matchCount) / (matchCount + mismatchCount);
				correctGuessRatioPerFold.add(new Double(correctGuessRatio));
				matchCount = 0;
				mismatchCount = 0;
				if (verbosePrinting) {
					System.out.printf("0/1 accuracy for fold %d is %f%n", lastFoldNumber, correctGuessRatio);
				}
				lastFoldNumber = currentFoldNumber;
				
			} 
			if (results.get(i)[GUESS].equals(results.get(i)[ACTUAL])) {
				matchCount++;
			} else {
				mismatchCount++;
			}
		}

		// Average the results for 0/1 accuracy across all of the folds
		double accuracyAverage = 0;
		for (int i = 0; i < correctGuessRatioPerFold.size(); i++) {
			accuracyAverage += correctGuessRatioPerFold.get(i).doubleValue();
		}
		accuracyAverage = accuracyAverage / correctGuessRatioPerFold.size();
		System.out.printf("%s accuracy average = %f | correctGuessRatioPerFold.size() = %d%n", algorithmName, accuracyAverage, correctGuessRatioPerFold.size()); // DELETE
		String stringToWrite = String.format("%s,%s,%s,%f", algorithmName, dataSetName, hyperParams, accuracyAverage);
		writer.writer(stringToWrite);
	}

}
