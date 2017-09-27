package geometry;

import org.ejml.simple.SimpleMatrix;

public class Robot extends Geometry {
    
    public Robot (SimpleMatrix pointMat, Vector centroid) {
        super(pointMat, centroid);
    }

    public void move (double dx, double dy) {
        for (int j = 0; j < pointMat.numCols(); j++) {
            double x = pointMat.get(0, j);
            double y = pointMat.get(1, j);

            // translation
            pointMat.set(0, j, x + dx);
            pointMat.set(1, j, y + dy);
        } 
    }

    public void rotate (double angle) {
        double sinAngle = Math.sin(angle);
        double cosAngle = Math.cos(angle);

        SimpleMatrix rotationMat = new SimpleMatrix(2, 2, true,
                cosAngle, -sinAngle,
                sinAngle, cosAngle);

        System.out.println(pointMat);

        pointMat = rotationMat.mult(pointMat);
    }    
}
