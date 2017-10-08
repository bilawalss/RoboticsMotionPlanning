package planning;

import geometry.*;

import java.util.ArrayList;
import java.util.List;

import org.ejml.simple.SimpleMatrix;


public class Environment {
    private Robot robot;
    private List<PolygonObject> obstacles;
    private double worldWidth, worldHeight;

    private List<PolygonObject> robotTriangles;
    private List<PolygonObject> obstacleTriangles;

    public Environment(Robot robot, List<PolygonObject> obstacles, double worldWidth,
            double worldHeight) {
        this.robot = robot;
        this.obstacles = obstacles;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
	}


    public double getWorldWidth () {
        return worldWidth;
    }

    public double getWorldHeight() {
        return worldHeight;
    }

    public Double[] getRobotPointArray (Configuration c) {
       return robot.getPointArray(c);     
    }

    private void triangleBuilder(Configuration c) {
        Double[] robotArray = robot.getPointArray(c);
        double startX = robotArray[0];
        double startY = robotArray[1];
        robotTriangles = new ArrayList<>();
        for (int i = 2; i < robotArray.length-2; i+=2) {

            robotTriangles.add(new PolygonObject(new double[] {startX, startY,
                                                               robotArray[i], robotArray[i+1], 
                                                               robotArray[i+2], robotArray[i+3]}));
        }
        obstacleTriangles = new ArrayList<>();

        for (PolygonObject obstacle : obstacles) {
            Double[] obstacleArray = obstacle.getPointArray();
            double obstacleStartX = obstacleArray[0];
            double obstacleStartY = obstacleArray[1];

            for (int i = 2; i < obstacleArray.length-2; i+=2) {
                obstacleTriangles.add(new PolygonObject(new double[] {obstacleStartX, obstacleStartY,
                                                               obstacleArray[i], obstacleArray[i+1], 
                                                               obstacleArray[i+2], obstacleArray[i+3]}));
                
            }
        }
    }

    public boolean checkCollision(Configuration c) {
        
        Double [] robotArray = robot.getPointArray(c);
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;
        for (int i = 0; i < robotArray.length/2; i+=2) {
            minX = Math.min(minX, robotArray[i]);
            maxX = Math.max(maxX, robotArray[i]);
            minY = Math.min(minY, robotArray[i+1]);
            maxY = Math.max(maxY, robotArray[i+1]);
        }

        if (minX <= 0 || maxX >= getWorldWidth() || minY <= 0 || maxY >= getWorldHeight()) 
            return true;


        triangleBuilder(c);
        for (PolygonObject robot : robotTriangles) {
            for (PolygonObject obstacle : obstacleTriangles) {
                if (isPolygonsIntersecting(robot, obstacle)) {
                    
                    return true;
                }
            }
        }

        return false;
    }
    
    private boolean isPolygonsIntersecting(PolygonObject a, PolygonObject b) {

        // for 2 polygons, a is robot, b is obstacle
        for (int x=0; x<2; x++) {

            // if x = 0, polygon is a
            PolygonObject polygon = (x==0) ? a : b;

            // repeat for all points of the polygon
            Double [] polygonArray = polygon.getPointArray();
            for (int i1=0; i1<polygonArray.length/2; i1+=2) {

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
                for (int i=0; i<aArray.length/2; i+=2) {
                // for (Double p : a.getPointArray()) {
                    double projected = normalX * aArray[i] + normalY * aArray[i+1];

                    if (projected < minA)
                        minA = projected;
                    if (projected > maxA)
                        maxA = projected;
                }

                double minB = Double.POSITIVE_INFINITY;
                double maxB = Double.NEGATIVE_INFINITY;

                Double [] bArray = b.getPointArray();
                for (int j=0; j<bArray.length/2; j+=2) {
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
