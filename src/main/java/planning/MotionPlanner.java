package planning;

import java.util.List;
import geometry.Configuration;

/** The motion planner for doing robotic simulation. */
public abstract class MotionPlanner {
    Environment env;
    Sampler sampler;
    LocalPlanner localPlanner;

    public Environment getEnvironment() {
        return env;
    }

    public LocalPlanner getLocalPlanner() {
        return localPlanner;
    }

    public abstract List<Configuration> query(Configuration start, Configuration end);
}
