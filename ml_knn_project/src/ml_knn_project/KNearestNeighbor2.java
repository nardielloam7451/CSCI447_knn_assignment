//package ml_knn_project;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static java.util.stream.Collectors.*;
import static java.util.Map.Entry.*;

public class KNearestNeighbor2 {
    /* Global Variables */
    ArrayList<ArrayList<Object>> trainingSet; // The data set to build the model on, assume class/regression value in last row
    ArrayList<ArrayList<Object>> testSet;    // The data set to test the model on, assume class/regression value in last row
    int k;									  // The number of k-neighbors to use for classification/regression
    ZeroOneLoss zeroOne;					  // Loss function 1
    //MeanSquaredError mse;                    // Loss function 2
    Distance distanceFunction;
    String algorithmName = "KNN";
    // CSVReader writer = new CSVReader("Results.csv");
    String filename;
    boolean writeResultsOut = true;
    public KNearestNeighbor2(ArrayList<ArrayList<Object>> trainingSet, ArrayList<ArrayList<Object>> testSet, int k, ZeroOneLoss zeroOne) {
        this.trainingSet = trainingSet;
        this.testSet = testSet;
        this.k = k;
        this.zeroOne = zeroOne;
        //this.mse = mse;
        distanceFunction = new EuclidianDistance();
    }

    public KNearestNeighbor2(){}

    public String classify(ArrayList<Object> objectToClassify) {
        return classify(objectToClassify, trainingSet);
    }
    public String classify(ArrayList<Object> objectToClassify, ArrayList<ArrayList<Object>> knnModelSpace) {
        return classify(objectToClassify,knnModelSpace, k);
    }
    public String classify(ArrayList<Object> objectToClassify, ArrayList<ArrayList<Object>> knnModelSpace, int k) {
        // Find the k nearest neighbors to this object
        AbstractMap<Integer,Double> distanceMap = new HashMap<Integer,Double>();
        for (int i = 0; i < knnModelSpace.size(); i++) {
            double distance = distanceFunction.getDistance(objectToClassify, knnModelSpace.get(i));
            distanceMap.put(i, distance);
        }
        // sort the hash map
        // magic lambda wizardry, thanks https://www.baeldung.com/java-hashmap-sort
        AbstractMap<Integer,Double> sorted = distanceMap.entrySet().stream().sorted(comparingByValue()).collect(toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));

		/*System.out.println("Unsorted ------------");
		printMap(distanceMap);
		System.out.printf("%n%nSorted ---------%n");
		printMap(sorted);*/
        Object[] sortedKeys = sorted.keySet().toArray();
        AbstractMap<String, Integer> voteMap = new HashMap<String, Integer>();
        // Put the first k values into the voteMap
        for (int i = 0; i < k; i++) {
            int key = (int) sortedKeys[i];
            // Get class
            String attributeClass = knnModelSpace.get(key).get(knnModelSpace.get(key).size() - 1).toString();
            if (voteMap.get(attributeClass) != null) {
                int tally = voteMap.get(attributeClass);
                voteMap.put(attributeClass, tally+1);
            } else {
                voteMap.put(attributeClass, 1);
            }
        }
        // sort the vote map
        AbstractMap<String,Integer> sortedVoteMap = voteMap.entrySet().stream().sorted(comparingByValue()).collect(toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));
        int size = sortedVoteMap.keySet().toArray().length - 1; // Last element in the set is the one with most votes
        String classification = sortedVoteMap.keySet().toArray()[size].toString();
        //printVoteMap(sortedVoteMap);
        //System.out.println("Classification = " + classification + " Real class = " + objectToClassify.get(objectToClassify.size() - 1));
        //		if (writeResultsOut) {
        //			writer.writer(filename + "," + algorithmName, classification + "," + objectToClassify.get(objectToClassify.size() - 1).toString());
        //		}
        return classification;
    }



    public ArrayList<ArrayList<Object>> getNearestNeighbors(int numberOfNearestNeighborsToGet, ArrayList<ArrayList<Object>> knnModelSpace, ArrayList<Object> featureToTest) {

        ArrayList<ArrayList<Object>> nearestNeighbors = new ArrayList<ArrayList<Object>>();
        AbstractMap<Integer,Double> distanceMap = new HashMap<Integer,Double>();
        EuclidianDistance distanceFunction = new EuclidianDistance();


        for (int i = 0; i < knnModelSpace.size(); i++) {
            double distance = distanceFunction.getDistance(featureToTest, knnModelSpace.get(i));
            distanceMap.put(i, distance);
        }
        // sort the hash map
        // magic lambda wizardry, thanks https://www.baeldung.com/java-hashmap-sort
        AbstractMap<Integer,Double> sorted = distanceMap.entrySet().stream().sorted(comparingByValue()).collect(toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));
        Object[] sortedKeys = sorted.keySet().toArray();


        for (int i = 0; i < numberOfNearestNeighborsToGet; i++) {
            ArrayList<Object> closeNeighbor = knnModelSpace.get((int)sortedKeys[i]);
            nearestNeighbors.add(closeNeighbor);
        }

        return nearestNeighbors;
    }

    public double regress (ArrayList<ArrayList<Object>> model, ArrayList<Object> featureToTest) {

        ArrayList<ArrayList<Object>> nearestNeighbors = getNearestNeighbors(k, model, featureToTest);
        //getNearestNeighbors(k, model, featureToTest);

        double total = 0;
        double avg = 0;
        int lastCol = nearestNeighbors.get(0).size()-1;


        for (int r = 0; r < k; r++) {


            Object feature = nearestNeighbors.get(r).get(lastCol);
            Double d = (Double) feature;
            total += d;

        }

        avg = total / k;
        return avg;


    }

    private void printVoteMap(AbstractMap<String, Integer> m) {
        Object[] keys = m.keySet().toArray();
        for (Object key : keys) {
            System.out.printf("Class: %s, value: %d%n", key.toString(), m.get(key));
        }
    }

    private void printMap(AbstractMap<Integer,Double> m) {
        Object[] keys = m.keySet().toArray();
        for (int i = 0; i < m.size(); i++) {
            System.out.printf("Key: %s Value: %s%n", keys[i].toString(), m.get(keys[i]));
        }
    }
    public void setFileName(String filename) {
        this.filename = filename;
    }


}