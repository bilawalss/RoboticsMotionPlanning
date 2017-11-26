package planning;

import geometry.*;
import static global.Constants.DEBUG;

import java.util.ArrayList;
import java.util.List;

import org.ejml.simple.SimpleMatrix;


/** 
 * Contains information about the workspace, which includes world dimensions, robot, 
 * and obstacles.
 */
public class Environment {
    // world dimensions
    private double worldWidth, worldHeight;
    // robot
    private Robot robot;
    // the list of obstacles as polygon objects
    private List<PolygonObject> obstacles;
    // the list of triangles that are extracted from the obstacles
    private List<PolygonObject> obstacleTriangles;


    public Environment(Robot robot, List<PolygonObject> obstacles, double worldWidth,
            double worldHeight) {
        this.robot = robot;
        this.obstacles = obstacles;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;

        // construct the list of triangles for obstacles
        obstacleTriangles = new ArrayList<>();
        for (PolygonObject o: obstacles) {
            List<PolygonObject> triangles = o.getTriangles();
            obstacleTriangles.addAll(triangles);    
        }
	}


    public double getWorldWidth () {
        return worldWidth;
    }


    public double getWorldHeight() {
        return worldHeight;
    }

    
    /** Returns the world coordinates of the robot given a configuration point. */
    public Double[] getRobotPointArray (Configuration c) {
       return robot.getPointArray(c);     
    }


    public List<PolygonObject> getObstacles () {
        return obstacles;
    }


    /** Checks if the robot collides with any obstacle given a configuration point. */
    public boolean checkCollision(Configuration c) {
        Double [] robotArray = robot.getPointArray(c);

        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < robotArray.length; i += 2) {
            minX = Math.min(minX, robotArray[i]);
            maxX = Math.max(maxX, robotArray[i]);
            minY = Math.min(minY, robotArray[i+1]);
            maxY = Math.max(maxY, robotArray[i+1]);
        }
        
        // check if the robot is outside the workspace 
        if (minX < 0 || maxX >= getWorldWidth() || minY < 0 || maxY >= getWorldHeight()) 
            return true;

        List<PolygonObject> robotTriangles = robot.getWorldRobot(c).getTriangles(); 

        // check if any triangle of the robot collides with a triangle of any obstacle
        for (PolygonObject robotT: robotTriangles) {
            for (PolygonObject obstacleT: obstacleTriangles) {
                if (doPolygonsIntersect(robotT, obstacleT))
                    return true;
            }    
        }

        return false;
    }
    

    /**
     * Check collision between 2 convex polygons using Separating Axis Theorem (
     * code adapted from https://stackoverflow.com/a/27309428)
     */
    private boolean doPolygonsIntersect(PolygonObject a, PolygonObject b) {
        // for 2 polygons, a is robot, b is obstacle
        for (int x = 0; x < 2; x++) {
            // if x = 0, polygon is a
            PolygonObject polygon = (x == 0) ? a : b;

            // repeat for all points of the polygon
            Double [] polygonArray = polygon.getPointArray();
            for (int i1 = 0; i1 < polygonArray.length; i1 += 2) {

                // get another point of the polygon
                int i2 = (i1 + 2) % polygon.getPointArray().length;

                Double p1X = polygonArray[i1];
                Double p1Y = polygonArray[i1+1];

                Double p2X = polygonArray[i2];
                Double p2Y = polygonArray[i2+1];

                // find the normal of these two points
                double normalX = p2Y - p1Y;
                double normalY = p1X - p2X;

                double minA = Double.POSITIVE_INFINITY;
                double maxA = Double.NEGATIVE_INFINITY;

                Double [] aArray = a.getPointArray();
                for (int i = 0; i < aArray.length; i += 2) {
                    double projected = normalX * aArray[i] + normalY * aArray[i+1];

                    if (projected < minA)
                        minA = projected;
                    if (projected > maxA)
                        maxA = projected;
                }

                double minB = Double.POSITIVE_INFINITY;
                double maxB = Double.NEGATIVE_INFINITY;

                Double [] bArray = b.getPointArray();
                for (int j = 0; j < bArray.length; j += 2) {
                    double projected = normalX * bArray[j] + normalY * bArray[j+1];

                    if (projected < minB)
                        minB = projected;
                    if (projected > maxB)
                        maxB = projected;
                }

                if (maxA < minB || maxB < minA)
                    return false;
            }
        }

        return true;
    }
}
