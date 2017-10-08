package planning;

import geometry.Configuration;

public class MotionPlanner {
    // singleton object of the class
    private static MotionPlanner instance;

    private Environment env;
    private Sampler sampler;
    private LocalPlanner localPlanner;
    
    private MotionPlanner() {
    }

    public void setEnvironment(Environment env) {
        this.env = env;
    }

    public void setSampler(Sampler sampler) {
        this.sampler = sampler;
    }

    public void setLocalPlanner(LocalPlanner localPlanner) {
        this.localPlanner = localPlanner;
    }

    public static MotionPlanner getInstance() {
        if (instance == null)
            instance = new MotionPlanner();
        return instance;
    }
}
