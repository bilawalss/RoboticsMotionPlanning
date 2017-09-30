package geometry;

import java.util.List;
import org.ejml.simple.SimpleMatrix;

public class BoundingBox {
    protected double minX, maxX;
    protected double minY, maxY;

    public BoundingBox (double minX, double maxX, double minY, double maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    public BoundingBox (double[] points) {
        minX = points[0];
        maxX = points[0];
        minY = points[1];
        maxX = points[1];

        for (int i = 2; i < points.length; i+= 2) {
            minX = Math.min(minX, points[i]);
            maxX = Math.max(maxX, points[i]);
            minY = Math.min(minY, points[i+1]);
            maxY = Math.max(maxY, points[i+1]);
        }
    }

    public double getMinX() {
        return this.minX;
    }

    public double getMaxX() {
        return this.maxX;
    }

    public double getMinY() {
        return this.minY;
    }

    public double getMaxY() {
        return this.maxY;
    }
}
