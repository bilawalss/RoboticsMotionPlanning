package planning;

import geometry.Graph;
import geometry.Configuration;

import java.util.List;
import java.util.ArrayList;


public class PRM extends MotionPlanner {

	List<Configuration> res;

    public PRM(Environment env, Sampler sampler, LocalPlanner localPlanner) {
        this.env = env;
        this.sampler = sampler;
        this.localPlanner = localPlanner;
    }

    public Graph buildRoadMap(int worldWidth, int worldHeight, int samplePoints) {
    	long startTime = System.nanoTime();
		

    	int size = 0;
        Graph g = new Graph();
        for (int i = 0; i < samplePoints; i++) {
            Configuration c = sampler.getSamplePoint(worldWidth, worldHeight);
            if (!env.checkCollision(c)) {
                size++;
                g.addVertex(c);
     
            }
            
        }

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("Time to Sample points: "+ duration/1000000);

        startTime = System.nanoTime();
        // initialize configurations
        for (int i = 0; i < size; i++) {
            // Closest vertices to the 5 closest ones.
            List<Integer> neighbors = g.getKClosestVertices(i, 5);

            // if the local planner does not have any obtacles between the object, add the
            // object to the graph
            for (int neighbor : neighbors) {
                if (localPlanner.getPath(env, g.getVertex(i), g.getVertex(neighbor), 10) != null) {
                    g.addEdge(i, neighbor);
                    
                }
            }
        }  

        endTime = System.nanoTime();  
        duration = (endTime - startTime);
        System.out.println("Time to use Local Planner to finish RoadMap: "+ duration/1000000);

        return g;
    }

    public Graph pathPlanning(Graph g, Configuration start, Configuration end) {

        if (env.checkCollision(start)) return null;
        if (env.checkCollision(end)) return null;

         // add this to the graph
        long startTime = System.nanoTime();
        int startPos = g.addVertex(start);
        int endPos = g.addVertex(end);
    
        for (int i = 0; i < 2; i++) {
            int pos = 0;
            if (i == 0) pos = startPos;
            if (i == 1) pos = endPos;
             List<Integer> neighbors = g.getKClosestVertices(pos, 10);
             for (int neighbor : neighbors) {
                if (localPlanner.getPath(env, g.getVertex(pos), g.getVertex(neighbor), 10) != null) {
                    g.addEdge(pos, neighbor);
                    
                }
            }
        }   

        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        System.out.println("Time to add Start and End Nodes to the RoadMap "+ duration/1000000);     


        startTime = System.nanoTime();
        List<Integer> shortestPath = g.shortestPath(startPos, endPos);
        res = new ArrayList<>();

        endTime = System.nanoTime();
        duration = endTime - startTime;
        System.out.println("Time to find shortest path between start and end positions: "+ duration/1000000);

        System.out.println("Graph size: "+g.size());
        for (int i = 0; i < shortestPath.size(); i++) {
            Configuration u = g.getVertex(shortestPath.get(i));
            System.out.println("Shortest Path: "+i+" "+shortestPath.get(i));
        	res.add(u);
        }  



        return g;
    }    

    public List<Configuration> getSolution() {
    	return res;
    }
}