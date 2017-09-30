package geometry;

import org.ejml.simple.SimpleMatrix;

public class Robot extends PolygonObject {
    public Robot (double[][] points) {
        super(points);
    }

    public void move (double dx, double dy) {
        // move all points
        for (SimpleMatrix p: vectors) {
            p.set(0, 0, p.get(0, 0) + dx);
            p.set(1, 0, p.get(0, 1) + dy);
        }    
    }

    public void rotate (double angle) {
        double sinAngle = Math.sin(angle);
        double cosAngle = Math.cos(angle);

        SimpleMatrix rotationMat = new SimpleMatrix(2, 2, true,
                cosAngle, -sinAngle,
                sinAngle, cosAngle);

        // rotate all points
        for (int i = 0; i < vectors.size(); i++) {
            SimpleMatrix newPoint = rotationMat.mult(vectors.get(i));
            vectors.set(i, newPoint);
        } 
    }    
}
