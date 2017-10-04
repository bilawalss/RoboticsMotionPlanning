import geometry.*;

import java.util.ArrayList;
import java.util.List;

import org.ejml.simple.SimpleMatrix;

public class Environment {
	private Robot robot;
	private List<PolygonObject> obstacles;
    Sampler sampler = new Sampler(600, 400);

	public Environment(Robot robot, List<PolygonObject> obstacles) {
		this.robot = robot;
		this.obstacles = obstacles;
	}

    public static boolean pairCollision (PolygonObject p1, PolygonObject p2, Configuration c) {
        BoundingBox b1 = p1.getBoundingBox();
        BoundingBox b2 = p2.getBoundingBox();

        // one on the left of the other
        if (b1.getMinX() > b2.getMaxX() + c.getX() || b2.getMinX() + c.getX() > b1.getMaxX())
            return false;

        // one on top of the other
        if (b1.getMinY() > b2.getMaxY() + c.getY() || b2.getMinY() + c.getY() > b1.getMaxY())
            return false;

        return true;
    }

    public Configuration getCollisionFreePoint() {
        boolean done = true;
        Configuration c = sampler.getSamplePoint();
        while (done) { 
            c = sampler.getSamplePoint();
            int obstaclesCount = 0;
            for (PolygonObject obstacle : obstacles) {
                obstaclesCount++;
                if(pairCollision(obstacle, robot, c )) {
                    break;
                }
            }

            if (obstaclesCount == obstacles.size()) done = false;
        }
        return c;
    }
}
