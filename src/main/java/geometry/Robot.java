package geometry;

import org.ejml.simple.SimpleMatrix;


/** Used for create robot. */
public class Robot extends PolygonObject {

    /** Creates a robot from an array of points. */
    public Robot (double[] points) {
        super(points);
    }

    /**
     * Returns a new robot that is translated from the current robot.
     * @param dx the change in x coordinate
     * @param dy the change in y coordinate
     * @return a new robot that is translated from the current robot
     */
    public Robot translate (double dx, double dy) {
        double[] points = new double[vectors.size() * 2];

        // translate all points
        int j = 0;
        for (int i = 0; i < vectors.size(); i++) {
            SimpleMatrix p = vectors.get(i);
            points[j++] = p.get(0, 0) + dx;
            points[j++] = p.get(1, 0) + dy;
        }
        
        return new Robot(points);
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


    public Robot getWorldRobot (Configuration config) {
        return this.rotate(config.getAngle()).translate(config.getX(), config.getY());
    }


    public Double[] getPointArray(Configuration config) {
        // rotate and translate the robot based on the configuration
        Robot finalRobot = getWorldRobot(config);
        return finalRobot.getPointArray();
    }
}
