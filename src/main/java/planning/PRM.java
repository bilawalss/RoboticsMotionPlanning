package planning;

import geometry.Graph;
import geometry.Configuration;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import java.io.PrintWriter;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class PRM extends MotionPlanner {

	List<Configuration> res;
	String s = "";

    public PRM(Environment env, Sampler sampler, LocalPlanner localPlanner) {
        
        
    	this.env = env;
   		this.sampler = sampler;
    	this.localPlanner = localPlanner;

    	/*
        Date d = new Date();
		String s = d.toString();
		System.out.println("Date: ");
		*/

		
    }

    public Graph buildRoadMap(int worldWidth, int worldHeight, int samplePoints, int closestVertices) {
    	
		

    	int size = 0;
        Graph g = new Graph();
        while (size < samplePoints) {
        	
    		long startTime = System.nanoTime();
        	Configuration c = sampler.getSamplePoint(worldWidth, worldHeight);
        	if (!env.checkCollision(c)) {
            	size++;
            	g.addVertex(c);
 			}

 			if (size == 0) {

     			long endTime = System.nanoTime();
       			long duration = (endTime - startTime);
        		s += "Time to Sample point: "+ duration +"\n";
            }
            
        }

        

        boolean done  = true;
        // initialize configurations
        for (int i = 0; i < size; i++) {
        	List<Integer> neighbors = null;
            // Closest vertices to the 5 closest ones.
            if (i == 0) {
            	long startTime = System.nanoTime();
           		neighbors = g.getKClosestVertices(i, closestVertices);
           		long endTime = System.nanoTime();
           		long duration = endTime - startTime;
           		s += "Time for K nearest neighbors: "+ duration +"\n";
           	}
           	if (i != 0) {
           		neighbors = g.getKClosestVertices(i, closestVertices);
           	}

            // if the local planner does not have any obtacles between the object, add the
            // object to the graph
            
            for (int neighbor : neighbors) {
            	long startTime = System.nanoTime();
                if (localPlanner.getPath(env, g.getVertex(i), g.getVertex(neighbor), 10) != null) {
                	if (done) {
                		long endTime = System.nanoTime();
                		long duration = endTime - startTime;
                		s += "Time for LocalPlanner: "+ duration +"\n";
                		done = false;

                	}
                    g.addEdge(i, neighbor);
                    
                }
            }
        }  

        

        return g;
    }

    public Graph pathPlanning(Graph g, Configuration start, Configuration end, int closestVertices) {

    	long startTime = System.nanoTime();
        if (env.checkCollision(start)) return null;
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        s += "Time to check collision for each sample point: "+ duration +"\n";
        if (env.checkCollision(end)) return null;

         // add this to the graph
        startTime = System.nanoTime();
        int startPos = g.addVertex(start);
        int endPos = g.addVertex(end);
    
        for (int i = 0; i < 2; i++) {
            int pos = 0;
            if (i == 0) pos = startPos;
            if (i == 1) pos = endPos;
             List<Integer> neighbors = g.getKClosestVertices(pos, closestVertices);
             for (int neighbor : neighbors) {
                if (localPlanner.getPath(env, g.getVertex(pos), g.getVertex(neighbor), 10) != null) {
                    g.addEdge(pos, neighbor);
                    
                }
            }
        }   

        endTime = System.nanoTime();
        duration = endTime - startTime;
        s += "Time to add start and end nodes to the roadmap: "+ duration +"\n";    


        startTime = System.nanoTime();
        List<Integer> shortestPath = g.shortestPath(startPos, endPos);
        res = new ArrayList<>();

        endTime = System.nanoTime();
        duration = endTime - startTime;
        s += "Time to find shortest path: "+ duration +"\n";

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

    public String getTimes() {
    	return s;
    }
}