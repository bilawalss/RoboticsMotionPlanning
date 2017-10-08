package geometry;

import org.ejml.simple.SimpleMatrix;


public class Configuration {
    private double x, y;
    private double angle;

    public Configuration (double x, double y, double angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    } 

    public Configuration (double x, double y) {
        this(x, y, 0.0);
    }

    public Configuration (Configuration other) {
        this(other.x, other.y, other.angle);
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

    public static double distance(Configuration c1, Configuration c2) {
        double x1 = c1.getX(), y1 = c1.getY();
        double x2 = c2.getX(), y2 = c2.getY();

        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}
