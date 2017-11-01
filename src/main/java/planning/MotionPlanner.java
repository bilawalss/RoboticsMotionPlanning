package planning;


/** The motion planner for doing robotic simulation. */
public abstract class MotionPlanner {
    protected Environment env;
    protected Sampler sampler;
    protected LocalPlanner localPlanner;
}
