package ml_knn_project;

import java.util.ArrayList;
import java.util.Random;


public class TestDriver {

	public static void main(String[] args) {
		Random r = new Random();
		String[] randomStrings = {"Test1", "Test2", "Test3", "Test4"};
		ArrayList<ArrayList<Object>> trainingSet = new ArrayList<ArrayList<Object>>();
		ArrayList<ArrayList<Object>> testSet = new ArrayList<ArrayList<Object>>();
		for (int i = 0; i < 1000; i++) {
			ArrayList<Object> row1 = new ArrayList<Object>();
			ArrayList<Object> row2 = new ArrayList<Object>();
			int jmax = 30;
			for (int j = 0; j < jmax; j++) {
				if (r.nextInt(44) == 0 || j == jmax-1) {
					
					String s1 = randomStrings[r.nextInt(randomStrings.length)];
					String s2 = randomStrings[r.nextInt(randomStrings.length)];
					row1.add(s1);
					row2.add(s2);
					
				} else {
				row1.add(r.nextInt(800));
				row2.add(r.nextInt(788));
				}
			}
			
			trainingSet.add(row1);
			testSet.add(row2);
		}
		
		/* public KNearestNeighbor(ArrayList<ArrayList<Object>> trainingSet, ArrayList<ArrayList<Object>> testSet, int k, ZeroOneLoss zeroOne, MeanSquaredError mse) */
		KNearestNeighbor knn = new KNearestNeighbor(trainingSet, testSet, 5, null, null);
		long startTime = System.nanoTime();
		for (int i = 0; i < testSet.size(); i++) {
			knn.classify(testSet.get(i), trainingSet);
		}
		long endTime = System.nanoTime();
		long duration = (endTime - startTime) * 1/1000000;
		System.out.printf("%n%nTook %d milliseconds to classify %d examples", duration, testSet.size());
		CNN cnn = new CNN(testSet, testSet, 1, null, null);
		startTime = System.nanoTime();
		cnn.buildModel(trainingSet);
		endTime = System.nanoTime();
		duration = (endTime - startTime) * 1/1000000;
		System.out.printf("%n%nTook %d milliseconds to build CNN model%n", duration);
		
		cnn.buildModel(trainingSet);
		startTime = System.nanoTime();
		for (int i = 0; i < testSet.size(); i++) {
			cnn.classify(testSet.get(i));
		}
		endTime = System.nanoTime();
		duration = (endTime - startTime) * 1/1000000;
	}

}
