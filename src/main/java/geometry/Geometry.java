package geometry;

import java.util.*;
import org.ejml.simple.SimpleMatrix;

public class Geometry {
    // the coordinate of this object's centroid in global system.
    protected Vector centroid;
    // this will be an 2xn matrix, where n is the number of points.
    protected SimpleMatrix pointMat;
    protected BoundingBox bbox;

    public Geometry (SimpleMatrix pointMat, Vector centroid) {
        this.pointMat = new SimpleMatrix(pointMat);
        this.bbox = new BoundingBox(pointMat);
    }

    public boolean collidesWith (Geometry other) {
        return this.bbox.collidesWith(other.bbox);  
    }

    public Vector[] getPoints () {
        return Vector.getPoints(pointMat);
    }

    public SimpleMatrix getPointMat () {
        return new SimpleMatrix(pointMat);
    }
}
