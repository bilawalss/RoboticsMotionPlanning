import geometry.*;
import org.ejml.simple.SimpleMatrix;

public class Configuration {

	private double centroidX, centroidY;

	public Configuration(double x, double y) {
		centroidX = x;
		centroidY = y;
	} 

	public double getCentroidX() {
		return centroidX;
	}

	public double getCentroidY() {
		return centroidY;
	}

/*
	public SimpleMatrix getCentroid(Robot robot) {
		int sumX = 0;
		int sumY = 0;
		int count = 0;
		for (SimpleMatrix s : robot.getPoints()) {
			sumX += s.get(0);
			sumY += s.get(1);
			count++;
		}	

		return new SimpleMatrix(2, 1, true, sumX/count, sumY/count);
	}
	*/
}