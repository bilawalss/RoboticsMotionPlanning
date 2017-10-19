package planning;

import java.util.List;
import java.util.ArrayList;

import geometry.Configuration;

import org.ejml.simple.SimpleMatrix;


/** Local planner for connecting two configuration points. */
public class LocalPlanner {
    /**
     * Get a collision-free path from a source configuration to a destined configuration in
     * a given environment and null if there is no such path. 
     * @param env environment (workspace)
     * @param start starting configuration
     * @param end ending configuration
     * @param delta step size
     * @return a List containing a set of configurations representing a collision-free path or 
     * null if no such path exists
     */
    public List<Configuration> getPath(Environment env, Configuration start, Configuration end, 
            double delta) {
        List<Configuration> configs = new ArrayList<>();

        // add the start configuration
        configs.add(new Configuration(start));
        
        Configuration current = start;

        // compute the step vector
        SimpleMatrix diff = end.toVector().minus(start.toVector());
        int numSteps = (int)Math.floor(diff.normF() / delta);
        SimpleMatrix step = diff.divide(numSteps);

        // go from start to end configuration step by step
        for (int i = 0; i < numSteps; i++) {
            current = getNextConfiguration(current, step);
            // check if there is collision
            if (env.checkCollision(current))
                return null;
            configs.add(current);
        }

        // add the end configuration
        if (!current.toVector().isIdentical(end.toVector(), 0.001))
            configs.add(new Configuration(end));

        return configs;
    }


    /* Get the next configuration within a specific step from the current one. */
    private Configuration getNextConfiguration(Configuration current, SimpleMatrix step) {
        SimpleMatrix nextVector = current.toVector().plus(step);
        return new Configuration(nextVector.get(0, 0), nextVector.get(1, 0), nextVector.get(2, 0));
    }
}
