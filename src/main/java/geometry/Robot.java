package geometry;

import org.ejml.simple.SimpleMatrix;

public class Robot extends PolygonObject {
    public Robot (double[] points) {
        super(points);
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

    public Double[] getPointArray(Configuration config) {
        Double[] res = getPointArray();
        double cx = config.getX(), cy = config.getY();

        for (int i = 0; i < res.length; i++) {
            if (i % 2 == 0) res[i] += cx;
            else res[i] += cy;
        }        

        return res;
    }
}
