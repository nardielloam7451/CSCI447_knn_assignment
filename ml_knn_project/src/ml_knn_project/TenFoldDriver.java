package ml_knn_project;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;



/**
 * The Class TenFoldDriver
 */
public class TenFoldDriver {
	/** The files. */
	static String[] classificationFiles = {"car_data.csv", "segmentation_data.csv", "abalone_data.csv"};
	/** Indexes for what will be training / test sets */
	final int TRAININGSET = 0;
	final int TESTSET = 1;


	/** Random number generator */
	/** Seeded so that experiments may be repeated with the same values. */
	private static Random randomizer = new Random(-9);
	/**
	 * Gets the program going
	 *
	 */
	public static void main(String[] args) {
		/* If you want to have all the print statements output to a file called output.txt instead of the console
		 * uncomment the try/catch block */
		//		try {
		//			System.setOut(new PrintStream(new FileOutputStream("output.txt")));
		//		} catch (FileNotFoundException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//			System.exit(1);
		//		} // DELETE
		System.out.printf("%n%n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~%n");
		System.out.printf("%n%n~~~~~~~~~~~~~~~~ start ~~~~~~~~~~~~~~~~~%n");
		System.out.printf("%n%n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~%n");
		for (String file : classificationFiles) {
			new TenFoldDriver(file);
			System.out.printf("%n%n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~%n");
			System.out.printf("%n%n~~~~~~~~~~~~~~~~ fin ~~~~~~~~~~~~~~~~~~%n");
			System.out.printf("%n%n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~%n");
		}
	}

	/**
	 * Instantiates a new ten fold driver.
	 */
	public TenFoldDriver(String file) {
		// Create a new reader object for reading the CSV's in
		CSVReader reader = new CSVReader(file, "in");
		// Throw the data into a string
		String allDataAsString = reader.reader();
		// Split the string for parsing along the newline
		String[] rows = allDataAsString.split("\n");

		// During the preprocessing step, we added header metadata to the data sets
		// This includes a header name, to identify the data later if desired
		// as well as variable type
		String headerMetaData = rows[0]; // header metadata
		String variableTypes = rows[1];  // variable type

		// Puts the header into an array list for parsing purposes
		ArrayList<String> headerData = processVariableTypesHeader(variableTypes);

		// The data matrix, a 2d array list / matrix
		ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();

		// Build the matrix from the string data starting at index 2 (where the header/metadata ends)
		for (int i = 2; i < rows.length; i++) {
			// Split the row into the appropriate size
			String[] row = rows[i].split(",");

			// Variable names are getting confusing, but dataRow is an arrayList of row that has been parsed to appropriate types
			ArrayList<Object> dataRow = new ArrayList<Object>();
			for (int j = 0; j < row.length;j++) {
				Object atom = parseData(row[j], headerData.get(j));
				dataRow.add(atom);
			}
			// Insert the row created into the matrix & repeat the loop for every row
			data.add(dataRow);
		}
		// printMatrix(data);
		System.out.printf("%n%n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~%n");
		data = shuffleRows(data);
		// Triply nested array list containing the testing set and the training set as partitions
		ArrayList<ArrayList<ArrayList<Object>>> partitionedDataSet = new ArrayList<ArrayList<ArrayList<Object>>>();
		System.out.printf(    "~~~~~~~~~~~~~~File: %s", file);
		System.out.printf("%n%n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~%n");
		for (int i = 0; i < 10 /* folds */; i++) {
			System.out.printf("Fold index: %d%n", i);
			partitionedDataSet = makeTrainingAndTestSet(data, i);
			ArrayList<ArrayList<Object>> trainingSet = partitionedDataSet.get(TRAININGSET);
			ArrayList<ArrayList<Object>> testSet = partitionedDataSet.get(TESTSET);
			knn(cloneModel(trainingSet), cloneModel(testSet));
			enn(cloneModel(trainingSet), cloneModel(testSet));
			cnn(cloneModel(trainingSet), cloneModel(testSet));
			System.out.printf("%n%n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~%n");
		}
	}

	private ArrayList<ArrayList<Object>> cloneModel(ArrayList<ArrayList<Object>> modelToClone) {
		ArrayList<ArrayList<Object>> clone = new ArrayList<ArrayList<Object>>();
		for (int i = 0; i < modelToClone.size(); i++) {
			clone.add(modelToClone.get(i));
		}
		return clone;
	}

	private void knn(ArrayList<ArrayList<Object>> trainingSet, ArrayList<ArrayList<Object>> testSet) {
		KNearestNeighbor knn = new KNearestNeighbor(trainingSet, testSet, 2, null, null);
		int correctCount = 0;
		int wrongCount = 0;
		for (int i = 0; i < testSet.size(); i++) {
			String classificationResult = knn.classify(testSet.get(i));
			String actualClass = testSet.get(i).get(testSet.get(i).size() - 1).toString();
			if (classificationResult.equals(actualClass)) {
				correctCount++;
				//System.out.printf("KNN Correct! %s=%s | %d/%d%n",classificationResult, actualClass, correctCount, correctCount+wrongCount);
			} else {
				wrongCount++;
				//System.out.printf("KNN Wrong! %s!=%s | %d/%d%n",classificationResult, actualClass, correctCount, correctCount+wrongCount);
			}
		}
		System.out.printf("KNN score %d/%d%n", correctCount, correctCount+wrongCount);
	}
	private void enn(ArrayList<ArrayList<Object>> trainingSet, ArrayList<ArrayList<Object>> testSet) {
		ENN enn = new ENN(trainingSet, testSet, 2, null, null, 0.2124);
		enn.buildModel();
		int correctCount = 0;
		int wrongCount = 0;
		for (int i = 0; i < testSet.size(); i++) {
			String classificationResult = enn.classify(testSet.get(i));
			String actualClass = testSet.get(i).get(testSet.get(i).size() - 1).toString();
			if (classificationResult.equals(actualClass)) {
				correctCount++;
				//System.out.printf("ENN Correct! %s=%s | %d/%d%n",classificationResult, actualClass, correctCount, correctCount+wrongCount);
			} else {
				wrongCount++;
				//System.out.printf("ENN Wrong! %s!=%s | %d/%d%n",classificationResult, actualClass, correctCount, correctCount+wrongCount);
			}
		}
		System.out.printf("ENN score %d/%d%n", correctCount, correctCount+wrongCount);
	}
	private void cnn(ArrayList<ArrayList<Object>> trainingSet, ArrayList<ArrayList<Object>> testSet) {
		CNN cnn = new CNN(trainingSet, testSet, 2, null, null);
		cnn.buildModel();
		int correctCount = 0;
		int wrongCount = 0;
		for (int i = 0; i < testSet.size(); i++) {
			String classificationResult = cnn.classify(testSet.get(i));
			String actualClass = testSet.get(i).get(testSet.get(i).size() - 1).toString();
			if (classificationResult.equals(actualClass)) {
				correctCount++;
				//System.out.printf("CNN Correct! %s=%s | %d/%d%n",classificationResult, actualClass, correctCount, correctCount+wrongCount);
			} else {
				wrongCount++;
				//System.out.printf("CNN Wrong! %s!=%s | %d/%d%n",classificationResult, actualClass, correctCount, correctCount+wrongCount);
			}
		}
		System.out.printf("CNN score %d/%d%n", correctCount, correctCount+wrongCount);
	}


	private void printMatrixSize(ArrayList<ArrayList<Object>> matrix) {
		System.out.printf("Matrix Size (Number of rows): %d%n", matrix.size());
		for (int rowNumber = 0; rowNumber < matrix.size(); rowNumber++) {
			System.out.printf("Row %d: Size: %d%n", rowNumber, matrix.get(rowNumber).size());
		}
	}

	/**
	 * Process variable types header.
	 *
	 * @param variableTypes the variable types
	 * @return the array list
	 */
	// Parses the variable types header so that appropriate data types may be added later
	private ArrayList<String> processVariableTypesHeader(String variableTypes) {
		// Safety checking/sanitizing for parsing later
		variableTypes = variableTypes.toUpperCase();
		ArrayList<String> headerData = new ArrayList<String>();
		// Add the variable types to an array
		String[] arr = variableTypes.split(",");
		// Put those elements into an arrayList
		for (String s : arr) {
			headerData.add(s);
		}
		return headerData;
	}

	/**
	 * Parses the data.
	 *
	 * @param stringToParse the string to parse
	 * @param variableType the variable type
	 * @return the correctly parsed object of type (int or double or string or boolean, etc.)
	 */
	// Parses a string to it's appropriate type and throws it into a matrix later
	private Object parseData(String stringToParse, String variableType) {
		Object o = null;
		switch(variableType) {
		case "INTEGER":
		case "INT":
			o = Integer.parseInt(stringToParse);
			break;
		case "DOUBLE":
			o = Double.parseDouble(stringToParse);
			break;
		case "BOOLEAN":
		case "BOOL":
			if(stringToParse.equalsIgnoreCase("true") || stringToParse.equals("1")) {
				o = true;
			} else {
				o = false;
			}
			break;
		case "STRING":
			// Remove any whitespace just in case...Sanitizing for sanity
			stringToParse.replace("\n", "");
			stringToParse.replace("\r", "");
			o = stringToParse;
			break;
		default:
			System.err.printf("%n%nDo not recognize type %s, implement new parsing condition, or check header in csv%n", variableType);
			System.exit(1);
		}
		if (o == null) {
			System.err.printf("%n%nUnable to parse %s to any objects, variable type is %s", stringToParse, variableType);
			System.exit(1);
		}
		return o;
	}

	/**
	 * Prints the matrix.
	 *
	 * @param matrix the matrix to be printed
	 */
	private void printMatrix(ArrayList<ArrayList<Object>> matrix) {
		// test print the matrix
		for (int i = 0; i < matrix.size(); i++) {
			for (int j = 0; j < matrix.get(i).size(); j++) {
				String s = matrix.get(i).get(j).toString();
				System.out.printf("%s", s);
				// Check to see if it's the last index. If not print some commas for separation
				if (!(j == matrix.get(i).size() - 1)) {
					System.out.print(", ");
				}
			}
			System.out.println();
		}
		System.out.printf("Matrix Size (Row x Columns) = %d x %d%n", matrix.size(), matrix.get(0).size());
	}

	/**
	 * Shuffle rows.
	 *
	 * @param matrix the matrix
	 * @return a shuffled matrix (one with the rows in random order)
	 */
	private ArrayList<ArrayList<Object>> shuffleRows(ArrayList<ArrayList<Object>> matrix) {

		ArrayList<ArrayList<Object>> shuffledMatrix = new ArrayList<ArrayList<Object>>();
		// Pick rows out of a hat and list them in order on a new matrix
		while(!matrix.isEmpty()) {
			int randomIndex = randomizer.nextInt(matrix.size());
			ArrayList<Object> pick = matrix.get(randomIndex);
			shuffledMatrix.add(pick);
			matrix.remove(randomIndex);
		}
		return shuffledMatrix;
	}

	/**
	 *
	 * @param matrix to have scrambled
	 * @return a matrix with > 10% of the columns selected at random scrambled
	 */
	private ArrayList<ArrayList<Object>> scrambleColumns(ArrayList<ArrayList<Object>> matrix) {
		// Count the number of columns in the matrix
		int	numColumns = matrix.get(0).size();
		// See what percentage of the set a single column is
		double singleColumnPercentage = 1.0 / numColumns;
		int numberOfColumnsToScramble = (int)(0.1/singleColumnPercentage)+1; // Finds the ceiling of 10/singleColumnPercentage
		ArrayList<Integer> columnIndecesToScramble = new ArrayList<Integer>();
		// Populate the arraylist with all possible column indeces
		for (int i = 0; i < matrix.get(0).size(); i++) {
			columnIndecesToScramble.add(i);
		}
		// Prune the matrix down to 10% with the columns to be scrambled indicated
		for (int i = columnIndecesToScramble.size(); i > numberOfColumnsToScramble; i--) {
			int prunedIndex = randomizer.nextInt(i);
			columnIndecesToScramble.remove(prunedIndex);
		}

		// System.out.printf("Scrambling %d columns, columnIndecesToScramble size = %d.%nAre these equal?%n%n", numberOfColumnsToScramble, columnIndecesToScramble.size());

		// Scramble the desired columns
		int elementsPerColumn = matrix.size();
		for (int i = 0; i < numberOfColumnsToScramble; i++) {
			System.out.printf("Scrambling column: %d%n", columnIndecesToScramble.get(i));
			ArrayList<Integer> inOrder = new ArrayList<Integer>();
			ArrayList<Integer> randomOrder = new ArrayList<Integer>();

			// Populate in order
			for (int j = 0; j < elementsPerColumn; j++) {
				inOrder.add(j);
			}
			// Destroy in order to have a randomized population for randomOrder
			while(!inOrder.isEmpty()) {
				int randomIndex = randomizer.nextInt(inOrder.size());
				randomOrder.add(inOrder.get(randomIndex));
				inOrder.remove(randomIndex);
			}

			// Random order is now a map of the indeces to change in the column
			// Put the column in order into an array list
			ArrayList<Object> column = new ArrayList<Object>();
			int columnIndex = columnIndecesToScramble.get(i);
			for (int p = 0; p < matrix.size(); p++) {
				column.add(matrix.get(p).get(columnIndex));
			}

			// Use the column and random order to remap values back into the matrix
			for (int p = 0; p < elementsPerColumn; p++) {
				matrix.get(p).set(columnIndex, column.get(randomOrder.get(p)));
			}

		}
		return matrix;
	}

	/**
	 * Make training and test set.
	 *
	 * @param matrix the matrix to split into training and test sets
	 * @param foldToSplitOn the fold to split on, or the fold to be withheld for testing starting at index 0
	 * @return A 3d arraylist containing the training set as its first element and the test set as its second
	 */
	private ArrayList<ArrayList<ArrayList<Object>>> makeTrainingAndTestSet(ArrayList<ArrayList<Object>> matrix, int foldToSplitOn) {
		// Need to split across 10 folds as evenly as possible
		int approximateRowsPerFold = matrix.size()/10;
		int remainder = matrix.size() % 10;
		// Index 'i' in this array corresponds to how big fold 'i' should be
		int[] foldSizes = new int[10];
		for (int i = 0; i < 10; i++) {
			foldSizes[i] = approximateRowsPerFold;
			if (remainder > 0) {
				foldSizes[i]++;
				remainder--;
			}
			// System.out.printf("Fold: %d, Size: %d%n", i, foldSizes[i]);
		}
		// Build the training and test sets
		ArrayList<ArrayList<Object>> trainingSet = new ArrayList<ArrayList<Object>>();
		ArrayList<ArrayList<Object>> testSet = new ArrayList<ArrayList<Object>>();
		int counter = 0;
		for (int i = 0; i < foldSizes.length; i++) {
			for (int j = 0; j < foldSizes[i]; j++) {
				// Add to the test set
				if (i == foldToSplitOn) {
					testSet.add(matrix.get(j+counter));
				} else { // add to training set
					trainingSet.add(matrix.get(j+counter));
				}
			}
			counter += foldSizes[i];
			// System.out.printf("Counter = %d%ni=%d%n%n", counter, i);
		}
		ArrayList<ArrayList<ArrayList<Object>>> partitionedMatrix = new ArrayList<ArrayList<ArrayList<Object>>>();
		partitionedMatrix.add(trainingSet);
		partitionedMatrix.add(testSet);
		return partitionedMatrix;
	}
}