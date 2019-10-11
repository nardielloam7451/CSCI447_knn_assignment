//package ml_knn_project;
////this class implements the k_means clustering algorithm, built off of a number given to us by ENN. 
//import java.util.ArrayList;
//import java.util.Random;
//
//
//public class K_means extends ENN{
//	//our k-means class, which inherits from ENN.	
//	private int numClusters; //keeps track over the number of clusters in the array. 
//	private Random rand= new Random();//creates a random number generator for selecting the centeroids initially. 
//
//
//	public K_means(ArrayList<ArrayList<Object>> trainingSet, ArrayList<ArrayList<Object>> testSet, int k,
//			ZeroOneLoss zeroOne, MeanSquaredError mse, double validationSetFraction) {
//		super(trainingSet, testSet, k, zeroOne, mse, validationSetFraction);
//		this.numClusters=0;//sets the original number of clusters to 0
//		// TODO Auto-Generated constructor for the k-means class
//	}
//
//	public void classClusters() {
//		//the function that actually makes the cluster for our classification data sets. 
//		ArrayList<ArrayList<Object>> cloneSet = new ArrayList<ArrayList<Object>> ();
//		for(int i=0;i<trainingSet.size();i++) {
//			cloneSet.add(trainingSet.get(i));
//		}
//		super.buildENNModel();//builds the ENN model, which is necessary to get the cluster numbers. 
//		numClusters=super.getModelSize();//gets the cluster numbers for the data set.
//		//resets the training set back to the original set.
//		trainingSet=cloneSet;
//		this.makeClusters(trainingSet, numClusters);
//	}
//
//	public void regressClusters(int clusters) {
//		//this function takes prepares the algorithm for a regression data set.  
//		this.numClusters=clusters;//the clusters variable passed into the regress clusters uses the 
//		this.makeClusters(trainingSet, numClusters);
//	}
//
//	public void makeClusters(ArrayList<ArrayList<Object>> trainSet, int clusters){
//		//the actual clustering algorithm, which takes in the training set and makes the clusters. 
//		ArrayList<ArrayList<ArrayList<Object>>> clusterMap=new ArrayList<ArrayList<ArrayList<Object>>>(trainingSet.size());//stores the cluster map for the algorithm
//		ArrayList<ArrayList<Object>>clusterSet = new ArrayList<ArrayList<Object>>(clusters);//stores the centeroids for the cluster, which will be returned when the algorithm is done. 
//		//		int[] clusterAssignment = new int[trainSet.size()];
//		//		int random;
//		//		for(int j=0; j<clusterSet.size();j++) {
//		//			//Initializes the original centeroids to use in the base of the model.
//		//			random = rand.nextInt(trainSet.size());
//		//			clusterSet.add(trainSet.get(random));
//		//		}
//		//ArrayList<ArrayList<Object>> clusterModel = new ArrayList<ArrayList<Object>>();
//		ArrayList<Integer> initialClusterIndexList = new ArrayList<Integer>();
//		for (int i = 0; i < clusters; i++) {
//			Integer randIndex = rand.nextInt();
//			while (!initialClusterIndexList.contains(randIndex)) {
//				randIndex = rand.nextInt(clusters);
//			}
//			initialClusterIndexList.add(randIndex);
//		}
//		for (int i = 0; i < initialClusterIndexList.size(); i++) {
//			clusterSet.add(trainSet.get(initialClusterIndexList.get(i).intValue()));
//		}
//
//		int iter=0; //sets the iterable variable, which determines the number of times we run through the algorithm. 
//		while(iter<=(200)) { //goes through the data set and creates the clusters.
//			double minimumDistance = Double.MAX_VALUE;
//			for(int i =0;i<clusterSet.size();i++) {
//				clusterMap.add(super.getNearestNeighbors(clusterSet.size(), trainSet, clusterSet.get(i)));//puts the cluster into the 3-d array 
//				for(int l=0;l<clusterMap.size();l++){ 
//					for(int k=0;k<trainSet.size();k++){//searches through the distances, finds the specific 
//						for(int m=0;m<trainSet.size();m++) { 
//							double currentDistance= new Double(clusterMap.get(l).get(k).get(m).toString()); 
//							if(currentDistance <=minimumDistance) {//does the assignment of the point to a cluster.
//								minimumDistance = currentDistance; clusterAssignment[k]=i; } 
//						} 
//					} 
//				} 
//			}
//			int clusterSize=0; 
//			for(int a=0;a<clusterSet.size();a++) {//changes out the original centeroids for new centeroids 
//				for(int b=0;b<clusterAssignment.length;b++) { 
//					if(clusterAssignment[b]==a) {
//						clusterSize++; } 
//				} 
//				if(clusterSize==0) { 
//					break; 
//				} 
//				else {
//					clusterSet.add(a, trainSet.get(rand.nextInt(trainSet.size())));
//					clusterSet.remove(clusterSet.size()-1);
//				}		 
//			} 
//			iter++; 
//		}
//		trainSet=clusterSet;
//	}
//
//}