package planning;

import java.util.List;
import java.util.ArrayList;

import geometry.Configuration;
import utils.PairRes;

import org.ejml.simple.SimpleMatrix;


/** Local planner for connecting two configuration points. */
public class LocalPlanner {
    // resolution distance
    private double delta;

    public LocalPlanner(double delta) {
        this.delta = delta;
    }

    /**
     * Get a collision-free path from a source configuration to a destined configuration in
     * a given environment and null if there is no such path. 
     * @param env environment (workspace)
     * @param start starting configuration
     * @param end ending configuration
     * @return a List containing a set of configurations representing a collision-free path or
     * null if no such path exists
     */
    public List<Configuration> getPath(Environment env, Configuration start, Configuration end) {
        List<Configuration> configs = new ArrayList<>();

        // add the start configuration
        configs.add(new Configuration(start));
        
        Configuration current = start;

        // compute the step vector
        PairRes<Integer, SimpleMatrix> numAndStep = computeStepVector(start, end);
        int numSteps = numAndStep.first;
        SimpleMatrix step = numAndStep.second;

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


    public Configuration getNextConfiguration(Configuration start, Configuration end) {
        SimpleMatrix step = computeStepVector(start, end).second;
        return getNextConfiguration(start, step);
    }


    /* Get the next configuration within a specific step from the current one. */
    private Configuration getNextConfiguration(Configuration current, SimpleMatrix step) {
        SimpleMatrix nextVector = current.toVector().plus(step);
        return new Configuration(nextVector.get(0, 0), nextVector.get(1, 0), nextVector.get(2, 0));
    }


    public PairRes<Integer, SimpleMatrix> computeStepVector(Configuration start, Configuration end) {
        SimpleMatrix diff = end.toVector().minus(start.toVector());
        int numSteps = (int)Math.ceil(diff.normF() / delta);
        SimpleMatrix step = diff.divide(numSteps);
        return new PairRes<>(numSteps, step);
    }
}
