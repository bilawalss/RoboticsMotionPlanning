package geometry;

import java.util.*;
import org.ejml.simple.SimpleMatrix;

import static global.Constants.DEBUG;

public class PolygonObject {
    // store a list of 2x1 matrices representing coordinate vectors
    protected List<SimpleMatrix> vectors;
    // the rectangular bounding box around this polygon 
    protected BoundingBox bbox;

    public PolygonObject (double[] points) {
        vectors = new ArrayList<>();
        
        // create list of vectors
        for (int i = 0; i < points.length; i+= 2) {
            SimpleMatrix p = new SimpleMatrix(2, 1, true, new double[] { points[i], points[i+1] });
            if (DEBUG) {
                System.out.println(p);
            }
            vectors.add(p);
        }

        // create bbox
        bbox = new BoundingBox(points);  
    }

    public BoundingBox getBoundingBox() {
        return bbox;
    }

    /**
     * Get point array for drawing purpose.
     **/
    public Double[] getPointArray () {
        Double[] res = new Double[vectors.size() * 2];

        for (int i = 0; i < res.length; i += 2) {
            int j = i / 2;
            res[i] = vectors.get(j).get(0, 0);
            res[i+1] = vectors.get(j).get(1, 0);
        }

        return res;
    }
}
