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

        long startTime = System.nanoTime();
        Graph srcRRT = build(start, n);
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        System.out.println("Build srcRRT: " + duration);

        startTime = System.nanoTime();
        Graph tarRRT = build(end, n);
        endTime = System.nanoTime();
        duration = System.nanoTime();
        System.out.println("Build tarRRT: " + duration);

        // merge 2 RRTs
        startTime = System.nanoTime();
        PairRes<Integer, Integer> p = merge(srcRRT, tarRRT, l);
        endTime = System.nanoTime();
        duration = endTime - startTime;
        System.out.println("Merge two graphs: " + duration);
        
        List<Configuration> path = new ArrayList<>();

        // add path in the src tree
        for (int index: srcRRT.shortestPath(0, p.first)) {
            path.add(srcRRT.getVertex(index));
        }

        // add path in the goal tree
        for (int index: tarRRT.shortestPath(p.second, 0)) {
            path.add(tarRRT.getVertex(index));
        }

        return path;
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


    public PairRes<Integer, Integer> merge(Graph g1, Graph g2, int l) {
        Graph first = g1, second = g2;

        for (int i = 1; i <= l; i++) {
            Configuration qRand = sampler.getSamplePoint(env.getWorldWidth(), env.getWorldHeight());
            int qNewIdx1 = extend(g1, qRand);
            if (qNewIdx1 != -1) {
                int qNewIdx2 = extend(g2, g1.getVertex(qNewIdx1));
                if (qNewIdx2 != -1 && g1.getVertex(qNewIdx1).equals(g2.getVertex(qNewIdx2))) {
                    if (g1 == first) {
                        return new PairRes<>(qNewIdx1, qNewIdx2);
                    } else {
                        return new PairRes<>(qNewIdx2, qNewIdx1);
                    }
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
