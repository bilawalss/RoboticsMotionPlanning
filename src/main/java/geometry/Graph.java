package geometry;

import java.util.List;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Arrays;
import java.util.Collections;


/**
 * Used to make a graph for connecting configurations.
 */
public class Graph {
    public List<Vertex> vertices;

    public Graph() {
        vertices = new ArrayList<>();
    } 

    public int size() {
        return vertices.size();
    }

    public int addVertex(Configuration c) {
        int idx = vertices.size();
        Vertex v = new Vertex(idx, c);
        vertices.add(v);
        return idx;
    }

    public void addEdge(int i, int j) {
        vertices.get(i).addNeighbor(j);
        vertices.get(j).addNeighbor(i); 
    }

    public Configuration getVertex(int i) {
        return new Configuration(vertices.get(i).config);
    }

    public List<Integer> getNeighbors(int i) {
        return vertices.get(i).getNeighbors();
    }

    public List<Integer> getKClosestVertices(int i, int k) {
        Vertex start = vertices.get(i);

        // max heap
        PriorityQueue<Pair> pq = new PriorityQueue<>(k, (p1, p2) -> {
            if (p1.dist < p2.dist)
                return 1;
            else if (p1.dist > p2.dist)
                return -1;
            return 0;
        });

        // run through the list of all vertices, calculate their distances to
        // the start vertex, and put them on the heap (the heap only contains
        // the k vertices with minimum distances to the start vertex) 
        for (int j = 0; j < vertices.size(); j++) {
            if (j != i) {
                Vertex end = vertices.get(j);
                double dist = start.config.distanceTo(end.config);
                Pair p = new Pair(end, dist);

                if (pq.size() < k)
                    pq.add(p);
                else {
                    Pair maxMin = pq.peek();
                    if (maxMin.dist > dist) {
                        pq.poll();
                        pq.add(p);
                    }
                }
            }
        }

        // get the indexes of the k nearest vertices
        List<Integer> res = new ArrayList<>();
        for (Pair p: pq) {
            res.add(p.v.idx);
        }

        return res;
    }


    public List<Integer> shortestPath(int source, int target) {
        // min heap
        PriorityQueue<Pair> pq = new PriorityQueue<>(vertices.size(), (p1, p2) -> {
            if (p1.dist < p2.dist)
                return -1;
            else if (p1.dist > p2.dist)
                return 1;
            else return 0;
        });

        int[] prev = new int[vertices.size()];
        Arrays.fill(prev, -1);

        // map each vertex's index to the corresponding pair
        Pair[] pairs = new Pair[vertices.size()];

        // Dijkstra for finding the shortest path
        for (int i = 0; i < vertices.size(); i++) {
            Pair p = new Pair(vertices.get(i), Double.POSITIVE_INFINITY);
            if (i == source)
                p.dist = 0.0;
            pairs[i] = p;
            pq.add(p);
        }

        while (!pq.isEmpty()) {
            Pair p = pq.poll();
            Vertex v = p.v;
            double dist = p.dist;

            // find the target
            if (v.idx == target)
                break;

            // update all neighbors' distances
            for (int nIdx: v.neighbors) {
                Pair nP = pairs[nIdx];
                double edgeLength = v.config.distanceTo(nP.v.config);
                // update the heap if get smaller distance
                if (nP.dist > dist + edgeLength) {
                    pq.remove(nP);
                    nP.dist = dist + edgeLength;
                    pq.add(nP);
                    prev[nIdx] = v.idx;
                }
            }
        }
        
        // backtracing to get the shortest path
        List<Integer> res = new ArrayList<>();

        int current = target;
        while (current != -1) {
            res.add(current);
            current = prev[current];         
        }

        Collections.reverse(res);
        return res;
    }


    // (vertex, distance to start) pair
    private class Pair {
        Vertex v;
        double dist;
        Pair(Vertex v, double dist) {
            this.v = v;
            this.dist = dist;
        }
    }


    private class Vertex {
        // the index of this vertex in the list of vertices 
        int idx;
        // the indexes of this vertex's neighbors
        List<Integer> neighbors;
        // the configuration at this vertex
        Configuration config;
        
        Vertex(int idx, Configuration c) {
            this.idx = idx;
            this.neighbors = new ArrayList<>();
            this.config = new Configuration(c);
        }

        void addNeighbor(int neighborIdx) {
            neighbors.add(neighborIdx); 
        }

        List<Integer> getNeighbors() {
            return new ArrayList<>(neighbors);
        }
    }
}
