import geometry.Configuration;

public class MotionPlanner {
    // singleton object of the class
    private static MotionPlanner instance;

    private Environment env;
    private Sampler sampler;
    private Configuration currentConfig;

    private MotionPlanner() {
    }

    public void setEnvironment(Environment env) {
        this.env = env;
    }

    public void setSampler(Sampler sampler) {
        this.sampler = sampler;
    }

    public static MotionPlanner getInstance() {
        if (instance == null)
            instance = new MotionPlanner();
        return instance;
    }

    public void sampling () {
        currentConfig = sampler.getSamplePoint(env.getWorldWidth(), env.getWorldHeight());
    }

    public boolean checkCollision() {
        return env.checkCollision(currentConfig);
    }

    public Double[] getRobotPointArray() {
        return env.getRobotPointArray(currentConfig); 
    }
}
