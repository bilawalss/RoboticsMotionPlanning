package planning;

import geometry.Graph;
import geometry.Configuration;

import java.util.List;
import java.util.ArrayList;


public class PRM extends MotionPlanner {
	private Graph roadMap;
    private int closestVertices;
    private int samplePoints;

    public PRM(Environment env, Sampler sampler, LocalPlanner localPlanner, int closestVertices, int samplePoints) {
        this.env = env;
        this.sampler = sampler;
        this.localPlanner = localPlanner;
        this.closestVertices = closestVertices;
        this.samplePoints = samplePoints;

        // build road map
        buildRoadMap();
    }


    public Graph getRoadMap () { return roadMap; }


    private void buildRoadMap() {
        int size = 0;
        roadMap = new Graph();

        while (size < samplePoints) {
            Configuration c = sampler.getSamplePoint(env.getWorldWidth(), env.getWorldHeight());
            if (!env.checkCollision(c)) {
                size++;
                roadMap.addVertex(c);
            }
        }

        // initialize configurations
        for (int i = 0; i < size; i++) {
            List<Integer> neighbors = roadMap.getKClosestVertices(i, closestVertices);

            // if the local planner does not have any obtacles between the object, add the
            // object to the graph
            for (int neighbor : neighbors) {
                if (localPlanner.getPath(env, roadMap.getVertex(i), roadMap.getVertex(neighbor)) != null) {
                    roadMap.addEdge(i, neighbor);
                }
            }
        }  
    }


    public List<Configuration> query(Configuration start, Configuration end) {
        if (env.checkCollision(start)) return null;
        if (env.checkCollision(end)) return null;

        // add this to the roadMap
        int startPos = roadMap.addVertex(start);
        int endPos = roadMap.addVertex(end);

        for (int i = 0; i < 2; i++) {
            int pos = 0;
            if (i == 0) pos = startPos;
            if (i == 1) pos = endPos;
            List<Integer> neighbors = roadMap.getKClosestVertices(pos, closestVertices);
            for (int neighbor : neighbors) {
                if (localPlanner.getPath(env, roadMap.getVertex(pos), roadMap.getVertex(neighbor)) != null) {
                    roadMap.addEdge(pos, neighbor);
                }
            }
        }   

        List<Integer> shortestPath = roadMap.shortestPath(startPos, endPos);
        List<Configuration> res = new ArrayList<>();

        for (int i = 0; i < shortestPath.size(); i++) {
            Configuration u = roadMap.getVertex(shortestPath.get(i));
            res.add(u);
        }

        return res;
    }
}
