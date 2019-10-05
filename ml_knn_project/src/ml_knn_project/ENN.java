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
	}
	
	
	public void buildModel() {
		
	}
	
	private void shuffleTrainingSetRows() {
		ArrayList<ArrayList<Object>> shuffledTrainingSet = new ArrayList<ArrayList<Object>>();
		for (int i = 0; i < trainingSet.size(); i++) {
			int randIndex = r.nextInt(trainingSet.size());
			shuffledTrainingSet.add(trainingSet.get(randIndex));
			trainingSet.remove(randIndex);
		}
		trainingSet = shuffledTrainingSet;
	}
	
	private void buildValidationSet(ArrayList<ArrayList<Object>> trainingSet) {
		int validationSetSize = (int)(trainingSet.size() * validationSetFraction);
		for (int i = 0; i < validationSetSize; i++) {
			
		}
	}

}
