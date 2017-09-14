package objects;

public class MovableObject extends BaseObject implements Movable {
    public MovableObject (Point[] points) {
        super(points);
    }

    public void move (double dx, double dy) {
        // move every point
        for (Point p : points) {
            p.setX(p.getX() + dx);
            p.setY(p.getY() + dy);
        }

        // recompute the bounding box
        bbox = new BoundingBox(points);
    }

    public void rotate(double angle) {

        double xSum;
        double ySum;

        
    }
}
