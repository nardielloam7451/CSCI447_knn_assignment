package ml_knn_project;

public class Timer {
	private long start;
	private long stop;
	private String message = "";
	public Timer() {
		start = System.nanoTime();
	}
	
	public Timer(String message) {
		this.message = message;
		start = System.nanoTime();
	}
	
	public void stop() {
		stop = System.nanoTime();
		if (!message.equals("")) {
			System.out.print(message + " ");
		}
		System.out.printf("took %f seconds or %f minutes%n", nanoSecondsToSeconds(), nanoSecondsToMinutes());
	}
	
	private double nanoSecondsToMinutes() {
		return nanoSecondsToSeconds()/60.0;
	}
	
	private double nanoSecondsToSeconds() {
		return (stop - start)/1000000000.0;
	}
}
