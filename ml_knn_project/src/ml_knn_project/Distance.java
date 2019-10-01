package ml_knn_project;

import java.util.ArrayList;

public interface Distance {
	
	public double getDistance(ArrayList<Double> targetVector, ArrayList<Double> featureVector);

}
