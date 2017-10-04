package geometry;

import org.ejml.simple.SimpleMatrix;

public class Configuration {
    private double x, y;

	public Configuration(double x, double y) {
        this.x = x;
        this.y = y;
	} 

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double val) {
        x = val;
    }

    public void setY(double val) {
        y = val;
    }

    public Configuration move(double dx, double dy) {
        return new Configuration(x + dx, x + dy);
    }
}
