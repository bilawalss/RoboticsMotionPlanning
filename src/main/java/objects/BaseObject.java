package objects;


import java.util.Arrays;

public class BaseObject {
    private double[] points;
    private BoundingBox bbox;

    public BaseObject (double... points) {
        // keep the points for drawing original shape
        this.points = Arrays.copyOf(points, points.length);
        bbox = new BoundingBox(points);
    }

    public boolean collidesWith (BaseObject other) {
        return bbox.collidesWith(other.bbox);
    }

    public double[] getPoints () {
        return Arrays.copyOf(points, points.length);
    }
}
