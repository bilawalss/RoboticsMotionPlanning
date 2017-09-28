import geometry.*;
import java.util.ArrayList;
import java.util.List;

public class Environment {
	private Robot robot;
	private List<Polygon> obstacles;

	public Environment(Robot robot, List<Polygon> obstacles) {
		this.robot = robot;
		this.obstacles = obstacles;
	}

	public boolean collisionCheck() {
		for (Polygon obstacle : obstacles) {
			BoundingBox obstaclebbox = obstacle.getBoundingBox();
			BoundingBox robotbbox = robot.getBoundingBox();

			Vector obstaclePolygon = obstacle.getCentroid();
			Vector robotPolygon = robot.getCentroid();
			if (robotbbox.getMinX()+robotPolygon.get(0) > obstaclebbox.getMaxX()+obstaclePolygon.get(0) 
				|| obstaclebbox.getMinX()+obstaclePolygon.get(0) > robotbbox.getMaxX()+robotPolygon.get(0)) 
	            return false;

	        if (robotbbox.getMinY()+robotPolygon.get(1) > obstaclebbox.getMaxY()+obstaclePolygon.get(1) 
	        	|| robotbbox.getMinY()+robotPolygon.get(1) > obstaclebbox.getMaxY()+obstaclePolygon.get(1)) 
	           return false;
		}

		return true;
	}
}