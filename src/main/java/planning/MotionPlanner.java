package planning;

import geometry.Configuration;


/** The motion planner for doing robotic simulation. */
public class MotionPlanner {
    // singleton object of the class
    private static MotionPlanner instance;

    private Environment env;
    private Sampler sampler;
    private LocalPlanner localPlanner;
    
    private MotionPlanner() {
    }

    /** Set the environment. */
    public void setEnvironment(Environment env) {
        this.env = env;
    }

    /** Set the sampler. */
    public void setSampler(Sampler sampler) {
        this.sampler = sampler;
    }

    /** Set the local planner. */
    public void setLocalPlanner(LocalPlanner localPlanner) {
        this.localPlanner = localPlanner;
    }

    /** Returns the singleton object. */
    public static MotionPlanner getInstance() {
        if (instance == null)
            instance = new MotionPlanner();
        return instance;
    }
}
