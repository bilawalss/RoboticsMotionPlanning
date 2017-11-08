package planning;

import geometry.Graph;
import geometry.Configuration;
import utils.PairRes;

import java.util.List;
import java.util.ArrayList;


public class RRT extends MotionPlanner {

    public RRT(Environment env, Sampler sampler, LocalPlanner localPlanner) {
        this.env = env;
        this.sampler = sampler;
        this.localPlanner = localPlanner;
    }


    public List<Configuration> query (Configuration start, Configuration end, int n, int l) {
        List<Configuration> res = new ArrayList<>();

        Graph g1 = build(start, n);
        Graph g2 = build(start, n);
        return null;
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


    public List<PairRes<Graph, Integer>> merge(Graph g1, Graph g2, int l) {
        for (int i = 1; i <= l; i++) {
            Configuration qRand = sampler.getSamplePoint(env.getWorldWidth(), env.getWorldHeight());
            int qNewIdx1 = extend(g1, qRand);
            if (qNewIdx1 != -1) {
                int qNewIdx2 = extend(g2, g1.getVertex(qNewIdx1));
                if (qNewIdx2 != -1 && g1.getVertex(qNewIdx1).equals(g2.getVertex(qNewIdx2))) {
                    PairRes<Graph, Integer> p1 = new PairRes<>(g1, qNewIdx1);
                    PairRes<Graph, Integer> p2 = new PairRes<>(g2, qNewIdx2);

                    List<PairRes<Graph, Integer>> res = new ArrayList<>();
                    res.add(p1);
                    res.add(p2);

                    return res;
                }
            }
            // swap 
            Graph tmp = g1;
            g1 = g2;
            g2 = tmp;
        }

        return null;
    }


    private int extend(Graph g, Configuration q) {
        List<Integer> nearest = g.getKClosestVertices(q, 1, -1);
        Configuration qNear = g.getVertex(nearest.get(0));
        Configuration qNew = localPlanner.getNextConfiguration(qNear, q, 10.0);

        if (!env.checkCollision(qNew)) {
            int newIdx = g.addVertex(qNew);
            g.addEdge(newIdx, nearest.get(0));
            return newIdx;
        }

        return -1;
    }
}
