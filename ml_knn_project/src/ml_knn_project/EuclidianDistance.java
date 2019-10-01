package ml_knn_project;

import java.util.ArrayList;

public class EuclidianDistance implements Distance {

	@Override
	public double getDistance(ArrayList<Double> targetVector, ArrayList<Double> featureVector) {
		double distance = 0.0;
		for (int i = 0; i < targetVector.size(); i++) {
			distance += Math.pow(targetVector.get(i) - featureVector.get(i), 2);
		}
		return Math.sqrt(distance);
	}


}
