package ml_knn_project;

import java.util.ArrayList;
import java.util.Random;

public class Pam extends ENN {
	private int numClusters; //keeps track over the number of clusters in the array. 
	private Random r = new Random();//creates a random number generator for selecting the centeroids initially. 
	
	public Pam(ArrayList<ArrayList<Object>> trainingSet, ArrayList<ArrayList<Object>> testSet, int k,
			ZeroOneLoss zeroOne, MeanSquaredError mse, double validationSetFraction) {
		super(trainingSet, testSet, k, zeroOne, mse, validationSetFraction);
		this.numClusters=0;//sets the original number of clusters to 0
		// TODO Auto-Generated constructor for the k-means class
	}
	public void classClusters() {
		//the function that actually makes the cluster for our classification data sets. 
		ArrayList<ArrayList<Object>> cloneSet = new ArrayList<ArrayList<Object>> ();
		for(int i=0;i<trainingSet.size();i++) {
			cloneSet.add(trainingSet.get(i));
		 }
		super.buildModel();//builds the ENN model, which is necessary to get the cluster numbers. 
		numClusters=super.getModelSize();//gets the cluster numbers for the data set.
		//resets the training set back to the original set.
		trainingSet=cloneSet;
		this.makeInitialClusters(trainingSet, numClusters);
	}
	
	/**
	 * Initialize clusters
	 * @param trainSet the set to draw medoids from
	 * @param numberOfClusters the number of clusters to make
	 */
	public ArrayList<ArrayList<Object>> makeInitialClusters(ArrayList<ArrayList<Object>> trainSet, int numberOfClusters){
		ArrayList<ArrayList<Object>> clusterModel = new ArrayList<ArrayList<Object>>();
		ArrayList<Integer> initialClusterIndexList = new ArrayList<Integer>();
		for (int i = 0; i < numberOfClusters; i++) {
			Integer randIndex = r.nextInt();
			while (!initialClusterIndexList.contains(randIndex)) {
				randIndex = r.nextInt(numberOfClusters);
			}
			initialClusterIndexList.add(randIndex);
		}
		for (int i = 0; i < initialClusterIndexList.size(); i++) {
			clusterModel.add(trainSet.get(initialClusterIndexList.get(i).intValue()));
		}
		return clusterModel;
	}
}
