package geometry;

import org.ejml.simple.SimpleMatrix;

public class Configuration {
    private double x, y;
    private double angle;

	public Configuration(double x, double y, double angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
	} 

    public Configuration(double x, double y) {
        this(x, y, 0.0);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getAngle() {
        return angle;
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
