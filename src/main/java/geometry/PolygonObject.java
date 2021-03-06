package geometry;

import java.util.List;
import java.util.ArrayList;
import org.ejml.simple.SimpleMatrix;

import static global.Constants.DEBUG;


/**
 * Used to create a polygon object. 
 */ 
public class PolygonObject {
    // store a list of 2x1 matrices representing coordinate vectors
    protected List<SimpleMatrix> vectors;

    /**
     * Create a PolygonObject from an array of points.
     * @param points an array of points, where the even indexes contain x-coordinates
     * and odd indexes contain y-coordinates
     */ 
    public PolygonObject (double[] points) {
        vectors = new ArrayList<>();
        
        // create list of vectors
        for (int i = 0; i < points.length; i+= 2) {
            SimpleMatrix p = new SimpleMatrix(2, 1, true, new double[] { points[i], points[i+1] });
            vectors.add(p);
        } 
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

        return res;
    }


    /**
     * Return the list of triangles that make this polygon object.
     */
    public List<PolygonObject> getTriangles () {
        List<PolygonObject> res = new ArrayList<>();

        Double[] pointArray = getPointArray();
        double startX = pointArray[0];
        double startY = pointArray[1];

        for (int i = 2; i < pointArray.length - 2; i += 2) {
            PolygonObject triangle = new PolygonObject(new double[] {
                startX, startY,
                pointArray[i], pointArray[i+1],
                pointArray[i+2], pointArray[i+3]
            });

            res.add(triangle);
        }

        return res;
    }
}
