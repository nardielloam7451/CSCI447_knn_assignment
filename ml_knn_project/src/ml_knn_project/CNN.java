import java.util.ArrayList;

public class CNN extends KNearestNeighbor{

    public void CondensedNN(ArrayList<ArrayList<Object>> ourSet) {


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


        //for (int i=0; i < N-1; i++) {
        while (run == true) {

            // add the first example
            pointTested = ourSet.get(0);
            newSet.add(pointTested);

            // get the nearest data point
            double minDistance = Double.MAX_VALUE;
            int minIndex = -1;
            int p;
            Distance euclidianDistance = new EuclidianDistance();
            for (p = 0; p < newSet.size(); p++) {
                double distance = euclidianDistance.getDistance(newSet.get(p), pointTested);
                if (distance < minDistance) {
                    minDistance = distance;
                    minIndex = p;
                }
            }
            nearestPoint = ourSet.get(p);

            // check if that nearest data point is classified incorrectly
            if (!classify(nearestPoint).equals(nearestPoint)) {
                newSet.add(nearestPoint);
                pointTested = nearestPoint;
            }

            // handle when it reaches the end of the set

            if (ourSet.indexOf(nearestPoint) == N - 1) {
                run = false;
            }


        }

        // check if the class is incorrect
        // remove/tag that data point

    }
}
