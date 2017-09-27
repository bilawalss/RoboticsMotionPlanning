package geometry;

import org.ejml.simple.SimpleMatrix;

/**
 * A vector class that wraps around SimpleMatrix
 */ 
public class Vector {
    SimpleMatrix mat;

    public Vector(double x, double y) {
        mat = new SimpleMatrix(2, 1);
        set(0, x);
        set(1, y);
    }

    public Vector(Vector v) {
        this(v.get(0), v.get(1));
    }

    public double get(int i) {
        return mat.get(i, 0); 
    }

    public void set(int i, double val) {
        mat.set(i, 0, val);
    }

    /**
     * Get a list of vectors from a 2xn point matrix.
     */
    public static Vector[] getPoints(SimpleMatrix pointMat) {
        Vector[] res = new Vector[pointMat.numCols()];
        
        for (int j = 0; j < pointMat.numCols(); j++) {
            res[j] = new Vector(pointMat.get(0, j), pointMat.get(1, j));
        }

        return res;
    }
}
