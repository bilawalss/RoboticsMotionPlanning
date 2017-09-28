from geometry import *;

public class Environment {
	private Robot robot;
	private List<Polygon> obstacles;

	public Environment(Robot robot, List<Polygon> obstacles) {
		this.robot = robot;
		this.obstacles = obstacles;
	}

	public static boolean collisionCheck() {
		for (Polygon obstacle : obstacles) {
			BoundingBox obstaclebbox = obstacle.getBoundingBox();
			BoundingBox robotbbox = robot.getBoundingBox();
			if (robotbbox.getMinX() > obstalcebbox.getMaxX() || obstaclebbox.getMinX() > robotbbox.getMaxX())
	            return false;

	        if (robotbbox.getMinY() > obstaclebbox.getMaxY() || robotbbox.getMinY() > obstaclebbox.getMaxY()) 
	           return false;
		}

		return true;
	}
}