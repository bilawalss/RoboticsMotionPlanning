package geometry;

import java.util.List;
import org.ejml.simple.SimpleMatrix;

public class BoundingBox {
    double minX, maxX;
    double minY, maxY;

    public BoundingBox (double minX, double maxX, double minY, double maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    public BoundingBox (SimpleMatrix pointMat) {
        minX = Double.MAX_VALUE;
        maxX = Double.MIN_VALUE;
        minY = Double.MAX_VALUE;
        maxX = Double.MIN_VALUE;

        for (int j = 0; j < pointMat.numCols(); j++) {
            double x = pointMat.get(0, j);
            double y = pointMat.get(1, j);

            minX = Math.min(minX, x);
            maxX = Math.max(maxX, x);
            minY = Math.min(minY, y);
            maxY = Math.max(maxY, y);    
        }
    }

    public boolean collidesWith (BoundingBox other) {
        if (this.minX > other.maxX || other.minX > this.maxX)
            return false;

        if (this.minY > other.maxY || other.minY > this.maxY) 
           return false;

       return true; 
    }
}
