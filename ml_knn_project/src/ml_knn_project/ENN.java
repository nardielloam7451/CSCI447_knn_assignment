import java.util.ArrayList;

public class ENN extends KNearestNeighbor{

    public void EditedNN(ArrayList<ArrayList<Object>> trainingSet) {

        // pass the dataset after KNN classifier has been trained on all the datapoints
        // Repeat for each datapoint
        int N = trainingSet.size();    // this includes the class column


        for (int i=0; i < N; i++) {
            //

            // if the class is incorrect,
            // the data point is removed/tagged
            if (!classify(trainingSet.get(i)).equals(trainingSet.get(i))) {
                trainingSet.remove(i);
            }

        }

    }


}
