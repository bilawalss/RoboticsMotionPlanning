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

    public Vector add(Vector other) {
        double x = this.get(0) + other.get(0);
        double y = this.get(1) + other.get(1);
        return new Vector(x, y);
    }

    public SimpleMatrix getMatrix() {
        return new SimpleMatrix(mat);
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

    /**
     * Add the ith entry of the vector to each element in the ith row 
     * of the matrix. 
     */
    public static SimpleMatrix addVectorToMatrix(SimpleMatrix pointMat, Vector v) {
        SimpleMatrix res = new SimpleMatrix(2, pointMat.numCols());
        double vx = v.get(0);
        double vy = v.get(1);

        for (int j = 0; j < pointMat.numCols(); j++) {
            double px = pointMat.get(0, j);
            double py = pointMat.get(1, j);
            res.set(0, j, px - vx);
            res.set(1, j, py - vy);
        }     

        return res;
    }
}
