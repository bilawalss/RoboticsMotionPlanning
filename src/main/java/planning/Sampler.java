package planning;

import java.util.Random;

import geometry.Configuration;


/** Used for sampling configuration points. */
public class Sampler {

    /** Returns a configuration point within the given dimensions. */
    public Configuration getSamplePoint(double worldWidth, double worldHeight) {
        Random rand = new Random();
        double randX = rand.nextDouble() * worldWidth;
        double randY = rand.nextDouble() * worldHeight;
        // the angle is from -PI to PI
        double randAngle = (rand.nextDouble() * 2.0 - 1.0) * Math.PI; 
        return new Configuration(randX, randY, randAngle);
    }
}
