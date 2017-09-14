package objects;


import java.util.Arrays;

public class BaseObject {
    protected Point[] points;
    protected BoundingBox bbox;

    public BaseObject (Point[] points) {
        if (points == null || points.length == 0)
            return;

        // store the list of points for drawing real shape
        this.points = copyPointArray(points);

        bbox = new BoundingBox(points);
    }

    public boolean collidesWith (BaseObject other) {
        return bbox.collidesWith(other.bbox);
    }

    public Point[] getPoints () {
        return copyPointArray(this.points);
    }

    private Point[] copyPointArray (Point[] points) {
        Point[] res = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            res[i] = (points[i] == null) ? null : new Point(points[i]);
        }
        return res;
    }
}
