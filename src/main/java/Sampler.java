import java.util.*;
import geometry.*;

public class Sampler {

	public final int worldWidth, worldHeight;

	public Sampler(int width, int height) {
		worldWidth = width;
		worldHeight = height;
	}

	public Configuration getSamplePoint() {
			Random rand = new Random();
			double randX = rand.nextDouble() * worldWidth;
			double randY = rand.nextDouble() * worldHeight;
			return new Configuration(randX, randY);
	}
}