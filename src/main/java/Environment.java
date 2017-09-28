import geometry.*;

import java.util.ArrayList;
import java.util.List;

import org.ejml.simple.SimpleMatrix;

public class Environment {
	private Robot robot;
	private List<Polygon> obstacles;

	public Environment(Robot robot, List<Polygon> obstacles) {
		this.robot = robot;
		this.obstacles = obstacles;
	}

    public static boolean pairCollision (Polygon p1, Polygon p2) {
        BoundingBox b1 = p1.getBoundingBox();
        BoundingBox b2 = p2.getBoundingBox();

        // one on the left of the other
        if (b1.getMinX() > b2.getMaxX() || b2.getMinX() > b1.getMaxX())
            return false;

        // one on top of the other
        if (b1.getMinY() > b2.getMaxY() || b2.getMinY() > b1.getMaxY())
            return false;

        return true;
    }

    public static Polygon getWorldPolygon(Polygon p) {
        Vector centroid = p.getCentroid();
        SimpleMatrix pointMat = p.getPointMat();
        SimpleMatrix worldMat = Vector.addVectorToMatrix(pointMat, centroid.negate());
        return new Polygon(worldMat);
    }
}
