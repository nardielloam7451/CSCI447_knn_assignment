package ml_knn_project;

import java.util.ArrayList;

public class CNN extends KNearestNeighbor{

	public CNN(ArrayList<ArrayList<Object>> trainingSet, ArrayList<ArrayList<Object>> testSet, int k,
			ZeroOneLoss zeroOne, MeanSquaredError mse) {
		super(trainingSet, testSet, k, zeroOne, mse);
		// TODO Auto-generated constructor stub
		algorithmName = "CNN";
	}

	public void buildModel() {
		buildModel(trainingSet);
	}

	public void buildModel(ArrayList<ArrayList<Object>> ourSet) {


		// one example from each class of the training set is added to KNN classifier
		// the next data point is tested with the classifier

		// if its incorrectly classified, add it to the classifier
		// if not, it is gonna be discarded

		ArrayList<ArrayList<Object>> newSet = new ArrayList<ArrayList<Object>>();


		int N = ourSet.size();    // this includes the class column
		int i = 0;
		ArrayList<Object> pointTested;
		ArrayList<Object> nearestPoint;

		boolean run = true;


		// add the first example
		pointTested = ourSet.get(0);
		newSet.add(pointTested);
		int newSetLength = 1;
		int count = 0; // DELETE
		while (run == true) {
			for (int q = 1; q < ourSet.size(); q++) {
				// Test the points one at a time
				pointTested = ourSet.get(q);
				boolean testInList = true;
				
				// iterate through the points and stop when an element is not in the new set to see if it should be added
				while(testInList && q < ourSet.size() - 1) {
					if(!newSet.contains(pointTested)) {
						testInList = false;
					} else {
						q++;
						pointTested = ourSet.get(q);
					}
				}
				String pointTestedClass = pointTested.get(pointTested.size() - 1).toString(); // DELETE
				//System.out.printf("pointTested class = %s%n", pointTestedClass);

				// get the nearest data point
				double minDistance = Double.MAX_VALUE;
				int minIndex = -1;
				Distance euclidianDistance = new EuclidianDistance();
				for (int p = 0; p < newSet.size(); p++) {
					double distance = euclidianDistance.getDistance(newSet.get(p), pointTested);
					if (distance < minDistance) {
						minDistance = distance;
						minIndex = p;
					}
				}
				nearestPoint = newSet.get(minIndex);

				// check if that nearest data point is classified incorrectly
				if (!nearestPoint.get(nearestPoint.size() - 1).toString().equals(pointTested.get(pointTested.size() - 1).toString())) {
					newSet.add(pointTested);
				} 

				// handle when it reaches the end of the set
			}
			// Stop running when the size of the new set does not change
			if (newSetLength == newSet.size()) {
				run = false;
			} else {
				newSetLength = newSet.size();
				count++;
			}
		}
		trainingSet = newSet;
		System.out.printf("Ran %d trials through the training set to build condensed nearest neighbor set (size: %d elements)%n", count, newSet.size());
		// check if the class is incorrect
		// remove/tag that data point

	}
}