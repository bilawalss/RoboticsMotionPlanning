package geometry;

import org.ejml.simple.SimpleMatrix;

public class Robot extends PolygonObject {
    public Robot (double[] points) {
        super(points);
    }

    public Robot rotate (double angle) {
        double sinAngle = Math.sin(angle);
        double cosAngle = Math.cos(angle);

        SimpleMatrix rotationMat = new SimpleMatrix(2, 2, true,
                cosAngle, -sinAngle,
                sinAngle, cosAngle);

        // rotate all points
        double[] points = new double[vectors.size() * 2];
        int j = 0;

        for (int i = 0; i < vectors.size(); i++) {
            SimpleMatrix newPoint = rotationMat.mult(vectors.get(i));
            points[j++] = newPoint.get(0, 0);
            points[j++] = newPoint.get(1, 0);
        } 

        return new Robot(points);
    }    

    public BoundingBox getBoundingBox(Configuration config) {
        Robot rotatedRobot = rotate(config.getAngle());
        return rotatedRobot.getBoundingBox();
    }

    public Double[] getPointArray(Configuration config) {
        // rotate the robot based on the angle in the configuration
        Robot rotatedRobot = rotate(config.getAngle());
        Double[] res = rotatedRobot.getPointArray();

        double cx = config.getX(), cy = config.getY();

        for (int i = 0; i < res.length; i++) {
            if (i % 2 == 0) res[i] += cx;
            else res[i] += cy;
        }        

        return res;
    }
}
