package geometry;

import org.ejml.simple.SimpleMatrix;

public class Robot extends Polygon {
    
    public Robot (SimpleMatrix pointMat) {
        super(pointMat);
    }

    public void move (double dx, double dy) {
        // move the centroid
        centroid.set(0, centroid.get(0) + dx);
        centroid.set(1, centroid.get(1) + dy);   
    }

    public void rotate (double angle) {
        double sinAngle = Math.sin(angle);
        double cosAngle = Math.cos(angle);

        SimpleMatrix rotationMat = new SimpleMatrix(2, 2, true,
                cosAngle, -sinAngle,
                sinAngle, cosAngle);

        pointMat = rotationMat.mult(pointMat);

        // recompute the centroid
        SimpleMatrix newCentroid = rotationMat.mult(centroid.getMatrix());
        centroid.set(0, newCentroid.get(0, 0));
        centroid.set(1, newCentroid.get(1, 0));

        // recompute the bounding box
        bbox = new BoundingBox(pointMat);
    }    
}
