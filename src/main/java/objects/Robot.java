package objects;

public class Robot {
    /*
     * x and z are opposite and y is on the right of the line xz.
     * xz will always be the longer diameter.
     */
    private Point x, y, z;

    public Robot(Point x, Point y, Point z) {
        this.x = new Point(x);
    }

    public void rotate (double angle) {
        // rotate around the main radius clockwise
    }

    public void move (double dx, double dy) {

    }

    public double getAngle() {
        // zxy angle
    }

    public double getMainRadius() {
        // from center to x
    }
}
