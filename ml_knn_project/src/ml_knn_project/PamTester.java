package ml_knn_project;

import java.util.ArrayList;
/**
 * This class reads in data sets previously built with PAM to test regression on their k values
 * @author rob
 *
 */


public class PamTester {
	ArrayList<ArrayList<Object>> model = new ArrayList<ArrayList<Object>>();
	ArrayList<ArrayList<Object>> testSet = new ArrayList<ArrayList<Object>>();;
	String curFile = "dummyName";
	static String[] modelFiles = {
			"1570698079788_PAM_Model_repaired_wine2.csv", // wine maybe?
			"1570698102675_PAM_Model_repaired_wine1.csv", // more wine?
			"1570698118192_PAM_Model_repaired_fire.csv", // forest fires
			"1570698225867_PAM_Model_repaired_computer.csv", // computer
			"1570693552136_PAM_Model_repaired_segmentation.csv", // segmentation
			"1570698326749_PAM_Model_repaired_car.csv", // car
			"1570698301597_PAM_Model_repaired_abalone.csv" // abalone
	};
	static String[] classificationFiles = {
			"1570693552136_PAM_Model_repaired_segmentation.csv", // segmentation
			"1570698326749_PAM_Model_repaired_car.csv", // car
			"1570698301597_PAM_Model_repaired_abalone.csv" // abalone
	};
	static String[] regressionFiles = {
	"1570698079788_PAM_Model_repaired_wine2.csv", // wine maybe?
	"1570698102675_PAM_Model_repaired_wine1.csv", // more wine?
	"1570698118192_PAM_Model_repaired_fire.csv", // forest fires
	"1570698225867_PAM_Model_repaired_computer.csv" // computer
	};
	
	CSVReader read;
	public PamTester() {
		for (String file : modelFiles) {
			curFile = file;
			for (int k = 30; k <= 40; k++) {
				read = new CSVReader(file, "in");
				runCrossFoldValidation(k);
			}
		}

	}
	
	public void runCrossFoldValidation(int k) {
		boolean classification = false;
		boolean regression = false;
		
		for (int i = 0; i < classificationFiles.length; i++) {
			if (classificationFiles[i].equals(curFile)) {
				classification = true;
			}
		}
		
		for (int i = 0; i < regressionFiles.length; i++) {
			if (regressionFiles[i].equals(curFile)) {
				regression = true;
			}
		}

		if (classification) {
			LossFunction pamz1 = new ZeroOneLoss(false, "PAM", curFile, String.format("K=%d", k));
			for (int i = 0; i < 10; i++) {
				buildTrainingSet();
				buildTestSet();
				knn(pamz1, model, testSet,String.format("%d %s", i, curFile), k, i);
			}
			pamz1.writeResults();
		}
		
		if (regression) {
			LossFunction pamMSE = new MeanSquaredError(false, "PAM", curFile, String.format("K=%d", k));
			for (int i = 0; i < 10; i++) {
				buildTrainingSet();
				buildTestSet();
				knn(pamMSE, model, testSet,String.format("%d %s", i, curFile), k, i);
			}
			pamMSE.writeResults();
		}

	}

	public void buildTrainingSet() {
		// purge the old model
		model.clear();
		model = buildSet("End PAM model");

	}
	
	public void buildTestSet() {
		testSet.clear();
		testSet = buildSet("End Test Set");
	}
	
	private void knn(LossFunction l1, ArrayList<ArrayList<Object>> trainingSet, ArrayList<ArrayList<Object>> testSet, String filename, int k, int foldNumber) {
		KNearestNeighbor knn = new KNearestNeighbor(trainingSet, testSet, k, null, null);
		knn.setFileName(filename);
		int correctCount = 0;
		int wrongCount = 0;
		//System.out.println(knn.getTrainingSetSize());
		for (int i = 0; i < testSet.size(); i++) {
			//double classificationResult = knn.regress(testSet.get(i));
			String classificationResult = knn.classify(testSet.get(i));
			//double actualClass = Double.parseDouble(testSet.get(i).get(testSet.get(i).size() - 1).toString());
			String actualClass = testSet.get(i).get(testSet.get(i).size() - 1).toString();
			l1.addResult(foldNumber, String.format("%s", classificationResult), String.format("%s",actualClass));
			if (classificationResult.equals(actualClass)) {
				correctCount++;
			} else {
				wrongCount++;
			}
		}
		System.out.printf("KNN score %d/%d (%f)%n", correctCount, correctCount+wrongCount, (double)(correctCount)/(correctCount+wrongCount));
	}

	public ArrayList<ArrayList<Object>> buildSet(String stopString) {
		ArrayList<ArrayList<Object>> set = new ArrayList<ArrayList<Object>>();
		String row = "filler";
		while (!row.equals(stopString)) {
			if (row.contains(",")) {
				String[] rowArr = row.split(",");
				ArrayList<Object> rowList = new ArrayList<Object>();
				for (int i = 0; i < rowArr.length; i++) {
					try {
						// value is numerical
						Double d = Double.parseDouble(rowArr[i]);
						rowList.add(d);
					} catch (NumberFormatException e) {
						// value is string
						rowList.add(rowArr[i]);
					}
				}
				set.add(rowList);
			}
			row = read.readLine();
		}
		return set;
	}
}
