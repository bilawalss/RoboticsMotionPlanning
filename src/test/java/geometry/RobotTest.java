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
        double[] array = new double[] { -1, 2, 1, -2, 2, 3, -2, -2 };

        SimpleMatrix points = new SimpleMatrix(2, 4, true, array);
        robot = new Robot(points, new Vector(1, 1));
    }


    @Test
    void testMove() {
        robot.move(1, 2);

        Vector[] points = robot.getPoints();
        SimpleMatrix pointMat = robot.getPointMat();

        double[] array = new double[] { 0, 3, 2, -1, 4, 5, 0, 0 };
        SimpleMatrix expectedMat = new SimpleMatrix(2, 4, true, array);

        assertTrue(pointMat.isIdentical(expectedMat, 0.001));
    }


    @Test
    void testRotate() {
        robot.rotate(Math.PI / 2); 
    }
}
