package ml_knn_project;

import java.util.ArrayList;

public class EuclidianDistance implements Distance {

	@Override
	public double getDistance(ArrayList<Object> targetVector, ArrayList<Object> featureVector) {
		double distance = 0.0;
		// Looking at size - 1 because last element is the class
		for (int i = 0; i < targetVector.size() - 1; i++) {
			// Parse values as double or string
			double firstVal = Double.MAX_VALUE;
			double secondVal = 0;
			try {
				firstVal = Double.parseDouble(targetVector.get(i).toString());
				secondVal = Double.parseDouble(featureVector.get(i).toString());
			} catch (Exception e) {
				// We are dealing with strings rather than double values
				if (targetVector.get(i).toString().equals(featureVector.get(i).toString())) {
					firstVal = 0; // Same strings, same distance apart
				} else {
					firstVal = 1.0; // Different strings, different distances
				}
			}
			distance += Math.pow(firstVal - secondVal, 2);
		}
		return Math.sqrt(distance);
	}


}
