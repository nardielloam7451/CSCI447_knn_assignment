package ml_knn_project;
//this class implements the k_means clustering algorithm, built off of a number given to us by ENN. 
import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;



public class K_means extends ENN{
//our k-means class, which inherits from ENN.	
	private int numClusters; //keeps track over the number of clusters in the array. 
	private Random rand= new Random();//creates a random number generator for selecting the centeroids initially.
	Distance euclid = new EuclidianDistance();
	
	
	public K_means(ArrayList<ArrayList<Object>> trainingSet, ArrayList<ArrayList<Object>> testSet, int k,
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
		this.makeClusters(trainingSet, numClusters);
	}
	
	public void regressClusters(int clusters) {
		//this function takes prepares the algorithm for a regression data set.  
		this.numClusters=clusters;//the clusters variable passed into the regress clusters uses the 
		this.makeClusters(trainingSet, numClusters);
	}
	
	public void makeClusters(ArrayList<ArrayList<Object>> trainSet, int numCluster){
		//the actual clustering algorithm, which takes in the training set and makes the clusters. 
		//stores the cluster map for the algorithm
		HashMap<ArrayList<Object>, Integer> clusterMap= new HashMap<ArrayList<Object>, Integer>();
		ArrayList<ArrayList<Object>>clusterSet = new ArrayList<ArrayList<Object>>();//stores the centeroids for the cluster, which will be returned when the algorithm is done. 
		ArrayList<Integer> clusterAssignment = new ArrayList<Integer>();
		int random;
		for(int j=0; j<numCluster;j++) {
			//Initializes the original centeroids to use in the base of the model.
			random = rand.nextInt(trainSet.size());
		for(int i=0;i<numCluster;i++){
			clusterSet.add(trainSet.get(random));
		}
		  int iter=0; //sets the iterable variable, which determines the number of times we run through the algorithm. 
		  while(iter<(5)) { //goes through the data set and creates the clusters.
			  for(int l=0;l<trainSet.size();l++) {//goes through and calculates the nearest neighbor between a specific point and a centeroid, then adds that to the specified cluster. 
				  int nearestCluster=clusterSet.indexOf(getNearestNeighbors(1, clusterSet, trainSet.get(l)));
				  clusterMap.put(trainSet.get(l), nearestCluster);
			  }
			  int clusterSize=0;
			  for(int m=0;m<clusterSet.size();m++) {//looks through and gets the number of clusterMaps for a given cluster. 
				  for(int k=0;k<trainSet.size();k++) {
					  int cluster=clusterMap.get(trainSet.get(k));
					  if(cluster==m) {
						  clusterSize++;
					  }
				  }
				  if(clusterSize==0) {//if the cluster does not have any data points, we break.  
					  break;
				  }
				  else {//if the cluster has data points, we take the average of the distance of those data points, and then make the point closest to that position the new centeroid. 
					 int count=0;
					
					  for(ArrayList<Object> key:clusterMap.keySet()){ 
						  ArrayList<Object> instance=key; 
						  if(clusterMap.get(instance)==m){ 
							  if(count<=5){//replaces the centeroid with a new value, and restarts the cycles
								  clusterSet.add(m, instance); clusterSet.remove(m+1); count++; 
							  } 
							  else { 
								  break;}
						  }
						  else { 
							  break; 
						  	} 
					  }
					 
				  }
			  }
			  iter++;
		}
		trainingSet=clusterSet;
	}
	
}
}