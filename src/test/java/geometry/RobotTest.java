package geometry;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.ejml.simple.SimpleMatrix;


public class RobotTest {
    Robot robot;

    // check if a list of points contains a given point.
    boolean hasPoint(Vector[] points, double x, double y) {
        for (Vector v: points) {
            if (v.get(0) == x && v.get(1) == y)
               return true; 
        }
        return false;
    }

    @BeforeEach
    void init() {
        double[] array = new double[] { -1, 2, 3, 1, 1, 2, -1, -1 };

        SimpleMatrix points = new SimpleMatrix(2, 4, true, array);
        robot = new Robot(points);
    }


    @Test
    void testConstructor() {
        // check centroid
        Vector centroid = robot.getCentroid(); 
        assertEquals(centroid.get(0), 1.25, 0.001);
        assertEquals(centroid.get(1), 0.25, 0.001);

        // check if points are relative to centroid
        double[] points = new double[] { -2.25, 0.75, 1.75, -0.25,
                                         0.75, 1.75, -1.25, -1.25 }; 
        SimpleMatrix expected = new SimpleMatrix(2, 4, true, points);
        assertTrue(robot.getPointMat().isIdentical(expected, 0.001));
    }

    @Test
    void testMove() {
        robot.move(1, 2);

        // check centroid
        Vector centroid = robot.getCentroid(); 
        assertEquals(centroid.get(0), 2.25, 0.001);
        assertEquals(centroid.get(1), 2.25, 0.001);
    }

    @Test
    void testRotate() {
        robot.rotate(Math.PI / 2.0);

        // check centroid
        Vector centroid = robot.getCentroid(); 
        assertEquals(centroid.get(0), -0.25, 0.001);
        assertEquals(centroid.get(1), 1.25, 0.001);
    }
}
