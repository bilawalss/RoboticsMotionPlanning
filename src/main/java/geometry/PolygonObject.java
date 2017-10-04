package geometry;

import java.util.*;
import org.ejml.simple.SimpleMatrix;

import static global.Constants.DEBUG;

/**
 * This class is used to create a polygon object. 
 */ 
public class PolygonObject {
    // store a list of 2x1 matrices representing coordinate vectors
    protected List<SimpleMatrix> vectors;
    // the rectangular bounding box around this polygon 
    protected BoundingBox bbox;

    /**
     * Create a PolygonObject from an array of points.
     * @param points an array of points, where the even indexes contain x-coordinates
     *  and odd indexes contain y-coordinates
     */ 
    public PolygonObject (double[] points) {
        vectors = new ArrayList<>();
        
        // create list of vectors
        for (int i = 0; i < points.length; i+= 2) {
            SimpleMatrix p = new SimpleMatrix(2, 1, true, new double[] { points[i], points[i+1] });
            vectors.add(p);
        }

        // create bbox
        bbox = new BoundingBox(points);  
    }

    public BoundingBox getBoundingBox() {
        return bbox;
    }

    /**
     * Get world coordinates of all the points.
     */
    public Double[] getPointArray () {
        Double[] res = new Double[vectors.size() * 2];

        for (int i = 0; i < res.length; i += 2) {
            int j = i / 2;
            res[i] = vectors.get(j).get(0, 0);
            res[i+1] = vectors.get(j).get(1, 0);
        }

        return res;
    }

    /**
     * Get screen coordinates of all the points for drawing.
     * @param screenHeight the height of the screen
     */
    public Double[] getScreenPointArray (double screenHeight) {
        Double[] res = getPointArray();

        for (int i = 1; i < res.length; i += 2) {
            res[i] = screenHeight - res[i];
        }
    }
}
