package geometry;

import java.util.*;
import org.ejml.simple.SimpleMatrix;

public class PolygonObject {
    protected List<SimpleMatrix> vectors;
    protected BoundingBox bbox;

    public PolygonObject (double[][] points) {
        vectors = new ArrayList<>();
        
        // create list of vectors
        for (int i = 0; i < points.length; i++) {
            SimpleMatrix p = new SimpleMatrix(2, 1, true, points[i]);
            vectors.add(p);
        }

        // create bbox
        bbox = new BoundingBox(points);  
    }

    public BoundingBox getBoundingBox() {
        return bbox;
    }

    public SimpleMatrix[] getPoints () {
        SimpleMatrix[] res = new SimpleMatrix[vectors.size()];

        for (int i = 0; i < res.length; i++) {
            res[i] = new SimpleMatrix(vectors.get(i));
        }

        return res;
    }
}
