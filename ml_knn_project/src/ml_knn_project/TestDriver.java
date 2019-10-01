package ml_knn_project;

import java.util.ArrayList;
import java.util.Random;


public class TestDriver {

	public static void main(String[] args) {
		Random r = new Random();
		
		ArrayList<ArrayList<Object>> trainingSet = new ArrayList<ArrayList<Object>>();
		ArrayList<ArrayList<Object>> testSet = new ArrayList<ArrayList<Object>>();
		for (int i = 0; i < 1000; i++) {
			ArrayList<Object> row1 = new ArrayList<Object>();
			ArrayList<Object> row2 = new ArrayList<Object>();
			for (int j = 0; j < 40; j++) {
				row1.add(r.nextInt(800));
				row2.add(r.nextInt(788));
			}
			trainingSet.add(row1);
			testSet.add(row2);
		}
		
		/* public KNearestNeighbor(ArrayList<ArrayList<Object>> trainingSet, ArrayList<ArrayList<Object>> testSet, int k, ZeroOneLoss zeroOne, MeanSquaredError mse) */
		KNearestNeighbor knn = new KNearestNeighbor(trainingSet, testSet, 5, null, null);
		knn.classify(testSet.get(8));

	}

}
