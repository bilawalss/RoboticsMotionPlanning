package planning;

import geometry.*;

import java.util.ArrayList;
import java.util.List;

import org.ejml.simple.SimpleMatrix;


public class Environment {
    private Robot robot;
    private List<PolygonObject> obstacles;
    private double worldWidth, worldHeight;

    public Environment(Robot robot, List<PolygonObject> obstacles, double worldWidth,
            double worldHeight) {
        this.robot = robot;
        this.obstacles = obstacles;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
	}

    private boolean pairCollision (PolygonObject obstacle, Configuration c) {
        BoundingBox b1 = obstacle.getBoundingBox();
        BoundingBox b2 = robot.getBoundingBox(c);

        // one on the left of the other
        if (b1.getMinX() > b2.getMaxX() + c.getX() || b2.getMinX() + c.getX() > b1.getMaxX())
            return false;

        // one on top of the other
        if (b1.getMinY() > b2.getMaxY() + c.getY() || b2.getMinY() + c.getY() > b1.getMaxY())
            return false;

        return true;
    }

    public boolean checkCollision (Configuration c) {
        for (PolygonObject o: obstacles) {
            if (pairCollision(o, c)) {
                return true;
            }
        }
        return false;
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
}
