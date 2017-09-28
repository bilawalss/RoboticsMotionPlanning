package geometry;

import java.util.*;
import org.ejml.simple.SimpleMatrix;

public class Polygon {
    // the coordinate of this object's centroid in global system.
    protected Vector centroid;
    // this will be an 2xn matrix, where n is the number of points.
    // these points' coordinates will be relative to the centroid.
    protected SimpleMatrix pointMat;
    protected BoundingBox bbox;

    public Polygon (SimpleMatrix pointMat) {
        centroid = getCentroidFromMatrix(pointMat);
        this.pointMat = Vector.addVectorToMatrix(pointMat, centroid);
        this.bbox = new BoundingBox(pointMat);
    }

    public Vector getCentroid () {
        return new Vector(centroid);
    }

    public Vector[] getPoints () {
        return Vector.getPoints(pointMat);
    }

    public SimpleMatrix getPointMat () {
        return new SimpleMatrix(pointMat);
    }

    private static Vector getCentroidFromMatrix (SimpleMatrix pointMat) {
        double cX = 0.0, cY = 0.0;

        for (int j = 0; j < pointMat.numCols(); j++) {
            double x = pointMat.get(0, j);
            double y = pointMat.get(1, j);

            cX += (x - cX) / (j + 1);
            cY += (y - cY) / (j + 1);
        }

        return new Vector(cX, cY);
    } 
}
