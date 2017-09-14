package objects;

public class Point {
    private double x, y;

    public Point (double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point (Point other) {
        this.x = other.x;
        this.y = other.y;
    }

    public double getX () { return x; }
    public double getY () { return y; }
    public void setX (double x) { this.x = x; }
    public void setY (double y) { this.y = y; }
}
