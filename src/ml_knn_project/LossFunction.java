package ml_knn_project;

public interface LossFunction {
	public void addResult(int foldNumber, String guess, String actual);
	public void writeResults();
	
}
