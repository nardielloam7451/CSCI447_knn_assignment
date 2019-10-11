package ml_knn_project;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class Pam extends ENN {
	private Random r = new Random();//creates a random number generator for selecting the centeroids initially. 
	private int numberOfClusters;
	private String timeStamp;
	private CSVReader PAM_ModelWriter; 
	Distance euclid = new EuclidianDistance();
	AbstractMap<String,Double> distanceLookupMap = new HashMap<String,Double>();
	
	public Pam(ArrayList<ArrayList<Object>> trainingSet, ArrayList<ArrayList<Object>> testSet, int k,
			ZeroOneLoss zeroOne, MeanSquaredError mse, double validationSetFraction, String timeStamp) {
		super(trainingSet, testSet, k, zeroOne, mse, validationSetFraction);
		this.timeStamp = timeStamp;
	}

	/**
	 * Initialize clusters
	 * @param trainSet the set to draw medoids from
	 * @param numberOfClusters the number of clusters to make
	 */
	public ArrayList<ArrayList<Object>> medoidModel(ArrayList<ArrayList<Object>> trainSet, int numberOfClusters){
		ArrayList<ArrayList<Object>> clusterModel = new ArrayList<ArrayList<Object>>();
		ArrayList<Integer> initialClusterIndexList = new ArrayList<Integer>();
		for (int i = 0; i < numberOfClusters; i++) {
			Integer randIndex = r.nextInt(trainSet.size());
			while (initialClusterIndexList.contains(randIndex)) {
				randIndex = r.nextInt(numberOfClusters);
			}
			initialClusterIndexList.add(randIndex);
		}
		for (int i = 0; i < initialClusterIndexList.size(); i++) {
			clusterModel.add(trainSet.get(initialClusterIndexList.get(i).intValue()));
		}
		return clusterModel;
	}

	public void setClassifyNumberOfClusters() {
		ArrayList<ArrayList<Object>> trainingSetClone = cloneModel(trainingSet);
		buildENNModel();
		numberOfClusters = getModelSize();
		trainingSet = trainingSetClone;
		System.out.printf("Number of PAM clusters = %d%n", numberOfClusters);
	}

	public void setRegressionNumberOfClusters() {
		numberOfClusters = (int)(0.25 * trainingSet.size());
	}

	public void buildMedoidModel() {
		ArrayList<ArrayList<Object>> medoidModel = medoidModel(trainingSet, numberOfClusters);
		//AbstractMap< ArrayList<Object>,Integer> medoidMap = buildMedoidMap(medoidModel);
		buildDistanceLookupMap();
		double distortion = calculateDistortion(medoidModel);
		boolean run = true;
		System.out.printf("Building PAM Model%n");
		Timer pamTimer = new Timer("Building model for PAM"); // maybe delete
		while (run) {
			ArrayList<ArrayList<Object>> medoidModelClone = cloneModel(medoidModel);
			for (int i = 0; i < medoidModel.size(); i++) {
				
				// Build some random indeces to search first
				ArrayList<Integer> trash = new ArrayList<Integer>();
				ArrayList<Integer> randomIndexOrder = new ArrayList<Integer>();
				for (int j = 0; j < trainingSet.size(); j++) {
					trash.add(j);
				}
				
				while (!trash.isEmpty()) {
					int randomIndex = r.nextInt(trash.size());
					randomIndexOrder.add(trash.get(randomIndex));
					trash.remove(randomIndex);
				}
				
				/**
				 * This algorithm is so slow...Need to cut it off at some point. 45 minutes per fold seems fair.
				 */
				if (pamTimer.checkTimeInMinutes() > 45) {
					System.out.printf("%n!!!!!!!!!%nPAM is too slow. It's been 45 minutes since this fold began. For the sake of having a project finish on time, the model is not optimizing itself all of the way.%n%n");
					trainingSet = medoidModel;
					writePamToCSV();
					pamTimer.stop();
					return;
				}
				Timer setTimer = new Timer();
				for (int j = 0; j < trainingSet.size(); j++) {
					// just check for up to 3 minutes per fold so a diverse number of medoids can be checked
					if (setTimer.checkTimeInMinutes() > 3) {
						setTimer.stop();
						break;
					}
					int currentIndex = randomIndexOrder.get(j);
					while (medoidModel.contains(trainingSet.get(currentIndex)) && j < trainingSet.size() - 1) {
						j++;
						currentIndex = randomIndexOrder.get(j);
					}
					// Just in case the last index of the training set is part of the medoid model, break here
					if (j == trainingSet.size() - 1) {
						break;
					}

					// swap the current medoid with the element in the training set
					ArrayList<Object> oldMedoid = medoidModel.get(i);
					ArrayList<Object> newMedoid = trainingSet.get(currentIndex);
					medoidModel.set(i, newMedoid);
					double newDistortion = calculateDistortion(medoidModel);

					if (newDistortion < distortion) {
						System.out.printf("Medoid %d/%d changed, distortion = %f%n", i, medoidModel.size(), newDistortion);
						distortion = newDistortion;
					} else {
						//System.out.printf("No change to medoid %d, distortion = %f%n", i, distortion);
						medoidModel.set(i, oldMedoid);
					}
				}
			}
			// check to see if medoids are done being optimized
			if (medoidModelClone.equals(medoidModel)) {
				run = false;
			}
		}
		trainingSet = medoidModel;
		writePamToCSV();
		pamTimer.stop();
	}

	private AbstractMap< ArrayList<Object>,Integer> buildMedoidMap(ArrayList<ArrayList<Object>> medoidModel) {
		//System.out.printf("Calculating medoid map...%n");
		AbstractMap< ArrayList<Object>,Integer> medoidMap = new HashMap<ArrayList<Object>,Integer>();
		for (int i = 0; i < trainingSet.size(); i++) {
			// gets the first nearest neighbor
			int nearestClusterIndex = medoidModel.indexOf( getNearestNeighbors(1, medoidModel, trainingSet.get(i)).get(0) );
			medoidMap.put(trainingSet.get(i), nearestClusterIndex);
		}
		//System.out.printf("Done building medoid map...%n");
		return medoidMap;
	}
	
	private double getDistance(ArrayList<Object> featureVector1, ArrayList<Object> featureVector2) {
		String hashKey = getLookupHash(featureVector1, featureVector2);
		double distance = distanceLookupMap.get(hashKey);
		return distance;
	}

	private void buildDistanceLookupMap() {
		//AbstractMap<String,Double> distanceLookupMap = new HashMap<String,Double>();
		for (int i = 0; i < trainingSet.size(); i++) {
			for (int j = 0; j < trainingSet.size(); j++) {
				String hash = getLookupHash(trainingSet.get(i), trainingSet.get(j));
				Double distance = euclid.getDistance(trainingSet.get(i), trainingSet.get(j));
				distanceLookupMap.put(hash, distance);
			}	
		}
	}
	
	private String getLookupHash(ArrayList<Object> featureVector1, ArrayList<Object> featureVector2) {
		String hash1 = featureVector1.toString();
		String hash2 = featureVector2.toString();
		String hash;
		// if hash1 is before hash 2
		if (hash1.compareTo(hash2) < 0) {
			hash = hash1 + hash2;
		} else {
			hash = hash2 + hash1;
		}
		return hash;
	}
	
	private double calculateDistortion(ArrayList<ArrayList<Object>> medoidModel) {
		//System.out.printf("Calculating Distortion...%n");
		AbstractMap< ArrayList<Object>,Integer> medoidMap = buildMedoidMap(medoidModel);
		double distortion = 0;
		Object[] keys = medoidMap.keySet().toArray();
		for (int i = 0; i < keys.length; i++) { 
			double distance = getDistance((ArrayList<Object>)keys[i], medoidModel.get(medoidMap.get(keys[i]).intValue()));
			distortion += distance;
		}
		//System.out.printf("Done calculating distortion = %f%n", distortion);
		return distortion;
	}

	private ArrayList<ArrayList<Object>> cloneModel(ArrayList<ArrayList<Object>> modelToClone) {
		ArrayList<ArrayList<Object>> clone = new ArrayList<ArrayList<Object>>(modelToClone.size());
		for (int i = 0; i < modelToClone.size(); i++) {
			clone.add(modelToClone.get(i));
		}
		return clone;
	}
	
	private void writePamToCSV() {
		CSVReader writer = new CSVReader(timeStamp + "_PAM_Model.csv");
		writer.writer("Begin PAM model");
		for (int i = 0; i < trainingSet.size(); i++) {
			String stringToWrite = "";
			for (int j = 0; j < trainingSet.get(i).size(); j++) {
				stringToWrite = stringToWrite + trainingSet.get(i).get(j).toString();
				if (j != trainingSet.get(i).size() - 1) {
					stringToWrite = stringToWrite + ",";
				}
				writer.writer(stringToWrite);
			}
		}
		
		writer.writer("End PAM model");
		writer.writer("Begin Test Set");
		for (int i = 0; i < testSet.size(); i++) {
			String stringToWrite = "";
			for (int j = 0; j < testSet.get(i).size(); j++) {
				stringToWrite = stringToWrite + testSet.get(i).get(j).toString();
				if (j != testSet.get(i).size() - 1) {
					stringToWrite = stringToWrite + ",";
				}
			}
			writer.writer(stringToWrite);
		}
		writer.writer("End Test Set");
	}
}