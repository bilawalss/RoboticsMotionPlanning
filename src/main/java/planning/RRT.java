package planning;

import geometry.Graph;
import geometry.Configuration;

import java.util.List;


public class RRT extends MotionPlanner {

    public RRT(Environment env, Sampler sampler, LocalPlanner localPlanner) {
        this.env = env;
        this.sampler = sampler;
        this.localPlanner = localPlanner;
    }


    public Graph build(Configuration q, int n) {
        Graph g = new Graph();     
        g.addVertex(q);
        for (int i = 1; i <= n; i++) {
            // random a free configuration
            Configuration qRand;
            while (true) {
                qRand = sampler.getSamplePoint(env.getWorldWidth(), env.getWorldHeight());
                if (!env.checkCollision(qRand)) {
                    break;
                }
            }
            extend(g, qRand);
        }
        return g;
    }


    public boolean merge(Graph g1, Graph g2, int l) {
        for (int i = 1; i <= l; i++) {
            Configuration qRand = sampler.getSamplePoint(env.getWorldWidth(), env.getWorldHeight());
            Configuration qNew1 = extend(g1, qRand);
            if (qNew1 != null) {
                Configuration qNew2 = extend(g2, qNew1);
                if (qNew2 != null && qNew2.equals(qNew1)) {
                    return true;
                }
            }
            // swap 
            Graph tmp = g1;
            g1 = g2;
            g2 = tmp;
        }

        return false; 
    }


    private Configuration extend(Graph g, Configuration q) {
        List<Integer> nearest = g.getKClosestVertices(q, 1, -1);
        Configuration qNear = g.getVertex(nearest.get(0));
        Configuration qNew = localPlanner.getNextConfiguration(qNear, q, 10.0);

        if (!env.checkCollision(qNew)) {
            int newIdx = g.addVertex(qNew);
            g.addEdge(newIdx, nearest.get(0));
            return qNew;
        }

        return null;
    }
}
