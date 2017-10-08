package planning;

import java.util.Random;

import geometry.Configuration;

public class Sampler {
    public Configuration getSamplePoint(double worldWidth, double worldHeight) {
        Random rand = new Random();
        double randX = rand.nextDouble() * worldWidth;
        double randY = rand.nextDouble() * worldHeight;
        double randAngle = rand.nextDouble() * 2 * Math.PI;
        return new Configuration(randX, randY, randAngle);
    }
}
