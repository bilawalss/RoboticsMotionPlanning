package planning;

import java.util.List;
import java.util.ArrayList;

import geometry.Configuration;


public class LocalPlanner {
    /**
     * Get a collision-free path from a source configuration to a destined configuration in
     * a given environment and null if there is no such path. 
     * @param env environment (workspace)
     * @param start starting configuration
     * @param end ending configuration
     * @param stepSize the maximum distance between two configurations on the calculated path
     * @return a List containing a set of configurations representing a collision-free path or 
     * null if no such path exists
     */
    public List<Configuration> getPath(Environment env, Configuration start, Configuration end, 
            double stepSize) {
        List<Configuration> configs = new ArrayList<>();

        // add the start configuration
        configs.add(new Configuration(start));
        
        Configuration current = start;
        // check collision incrementally until collision is found or the distance is less than 
        // step size.
        while (Configuration.distance(current, end) > stepSize) {
            // get the next configuration
            current = getNextConfiguration(current, end, stepSize);

            // if the new configuration is not collision-free, return null
            if (env.checkCollision(current))
                return null;
            
            configs.add(current);
        }

        // add the end configuration
        configs.add(new Configuration(end));

        return configs;
    }

    private Configuration getNextConfiguration(Configuration current, Configuration end, 
            double stepSize) {
        double cx = current.getX(), cy = current.getY();
        double ex = end.getX(), ey = end.getY();

        // distance between two configurations 
        double d = Configuration.distance(current, end);

        // distance factor from current to next configuration
        double f = stepSize / d;

        double nx = f * ex + (1-f) * cx;
        double ny = f * ey + (1-f) * cy;

        return new Configuration(nx, ny, current.getAngle());
    }
}
