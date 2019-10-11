package ml_knn_project;

import java.util.ArrayList;
import java.util.Random;

public class ENN extends KNearestNeighbor{
	private ArrayList<ArrayList<Object>> validationSet = new ArrayList<ArrayList<Object>>();
	private double validationSetFraction;
	private Random r = new Random();
	private ENN(ArrayList<ArrayList<Object>> trainingSet, ArrayList<ArrayList<Object>> testSet, int k,
			ZeroOneLoss zeroOne, MeanSquaredError mse) {
		super(trainingSet, testSet, k, zeroOne, mse);

	}

	public ENN(ArrayList<ArrayList<Object>> trainingSet, ArrayList<ArrayList<Object>> testSet, int k,
			ZeroOneLoss zeroOne, MeanSquaredError mse, double validationSetFraction) {
		super(trainingSet, testSet, k, zeroOne, mse);
		this.validationSetFraction = validationSetFraction;
		algorithmName = "ENN";
		// Make special training set so it doesn't destroy the og object reference
		
	}


	public void buildENNModel() {
		int originalSize = trainingSet.size();
		// Do not write any results out while building the model
		writeResultsOut = false;
		// Build the validation set from the training set
		buildValidationSet();
		
		// build the model
		boolean run = true;
		int numberOfCorrectValidations = -1; // Used to see how well the validation set is doing
		ArrayList<ArrayList<Object>> lastModel = new ArrayList<ArrayList<Object>>(); // Used to get the optimal model
		while (run) {
			ArrayList<Integer> pointsToRemove = new ArrayList<Integer>();
			// Test points
			for (int x = 0; x < trainingSet.size(); x++) {
				// Build a model with all features except for the one to be tested
				ArrayList<ArrayList<Object>>tempTrainingSet = new ArrayList<ArrayList<Object>>();
				for (int i = 0; i < trainingSet.size(); i++) {
					tempTrainingSet.add(trainingSet.get(i));
				}
				// Remove the feature and test its classification with all other examples
				ArrayList<Object> featureToTest = tempTrainingSet.get(x);
				tempTrainingSet.remove(x);

				String classifcation = classify(featureToTest, tempTrainingSet);
				String actualClass = featureToTest.get(featureToTest.size() - 1).toString();
				if (!classifcation.equals(actualClass)) {
					pointsToRemove.add(x);
				}
			}
			// Save the last model
			lastModel.clear();
			for (int i = 0; i < trainingSet.size(); i++) {
				lastModel.add(trainingSet.get(i));
			}
			
			// Remove points from training set
			int numPointsRemovedOffset = 0; // Offset needed as size of arraylist changes as points are removed
			for (int i = 0; i < pointsToRemove.size(); i++) {
				trainingSet.remove(pointsToRemove.get(i).intValue() - numPointsRemovedOffset);
				numPointsRemovedOffset++;
			}
			pointsToRemove.clear();
			
			// validate to see if loop should stop
			int lastValidationScore = numberOfCorrectValidations;
			numberOfCorrectValidations = 0;
			for (int i = 0; i < validationSet.size(); i++) {
				String actualClass = validationSet.get(i).get(validationSet.get(i).size() - 1).toString();
				String classification = classify(validationSet.get(i), trainingSet);
				if (actualClass.equals(classification)) {
					numberOfCorrectValidations++;
				}
			}
			if (numberOfCorrectValidations <= lastValidationScore) {
				// Performance is degrading, loop should stop and return use the last model
				run = false;
				trainingSet = lastModel;
				System.out.printf("Model complete %d correct this time, %d last time%n", numberOfCorrectValidations, lastValidationScore); // DELETE
				System.out.printf("Model size: %d/%d(original)%n", trainingSet.size(), originalSize);
			} else {
				System.out.printf("Model improving, %d correct this time, %d last time%n", numberOfCorrectValidations, lastValidationScore); // DELETE
			}
		}
		// Okay to write results again after model built
		writeResultsOut = true;
	}
	
	public int getModelSize() {
		return trainingSet.size();
	}

	private void shuffleTrainingSetRows() {
		ArrayList<ArrayList<Object>> shuffledTrainingSet = new ArrayList<ArrayList<Object>>();
		int size = trainingSet.size();
		for (int i = 0; i < size; i++) {
			int randIndex = r.nextInt(trainingSet.size());
			shuffledTrainingSet.add(trainingSet.get(randIndex));
			trainingSet.remove(randIndex);
		}
		trainingSet = shuffledTrainingSet;
	}

	private void buildValidationSet() {
		int validationSetSize = (int)(trainingSet.size() * validationSetFraction); // Floor of validation set fraction
		shuffleTrainingSetRows();
		for (int i = 0; i < validationSetSize; i++) {
			validationSet.add(trainingSet.get(0));
			trainingSet.remove(0);
		}
	}
}
