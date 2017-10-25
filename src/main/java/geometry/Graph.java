package geometry;

import java.util.List;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Arrays;
import java.util.Collections;

public class Graph {
    private ArrayList<Configuration> vertices;
    private ArrayList<ArrayList<Integer>> adjList;

    public Graph() {
        vertices = new ArrayList<>();
        adjList = new ArrayList<>(); 
    } 

    public int size() {
        return vertices.size();
    }

    public int addVertex(Configuration c) {
        vertices.add(new Configuration(c));
        adjList.add(new ArrayList<>());
        return vertices.size() - 1;
    }

    public void addEdge(int i, int j) {
        double dist = vertices.get(i).distanceTo(vertices.get(j));
        adjList.get(i).add(j);
        adjList.get(j).add(i);
    }

    public Configuration getVertex(int i) {
        return new Configuration(vertices.get(i));
    }

    public List<Integer> getNeighbors(int i) {
        return adjList.get(i);
    }

    public List<Integer> getKClosestVertices(int i, int k) {
        Configuration start = vertices.get(i);
        // max heap
        PriorityQueue<Edge> pq = new PriorityQueue<>((e1, e2) -> {
            if (e1.length< e2.length)
                return 1;
            else if (e1.length> e2.length)
                return -1;
            else return 0;
        });
        
        for (int j = 0; j < vertices.size(); j++) {
            if (j != i) {
                Configuration end = vertices.get(j);
                double dist = start.distanceTo(end);
                Edge e = new Edge(j, dist);

                if (pq.size() < k)
                    pq.add(e);
                else {
                    Edge maxMin = pq.peek();
                    if (maxMin.length > dist) {
                        pq.poll();
                        pq.add(e);
                    }
                }
            }
        }

        List<Integer> res = new ArrayList<>();
        for (Edge e: pq) {
            res.add(e.endPoint);
        }

        return res;
    }

    public List<Integer> shortestPath(int i, int j) {
        // prev list for backtracing the shortest path
        int[] prev = new int[vertices.size()];
        Arrays.fill(prev, -1);

        // priority queue for djikstra
        PriorityQueue<Vertex> pq = new PriorityQueue<>(vertices.size(), (v1, v2) -> {
            if (v1.dist > v2.dist)
                return 1;
            else if (v1.dist < v2.dist)
                return -1;
            return 0;
        });

        Vertex[] vWithDist = new Vertex[vertices.size()];

        // add all vertices to priority queue
        for (int idx = 0; idx < vertices.size(); idx++) {
            Vertex v;
            if (idx == i) {
                v = new Vertex(idx, 0.0);
            } else {
                v = new Vertex(idx, Double.POSITIVE_INFINITY);
            }
            vWithDist[idx] = v;
            pq.add(v);
        }

        while(!pq.isEmpty()) {
            Vertex u = pq.poll();

            // reach the end point
            if (u.idx == j)
                break;

            // consider every other neighbor
            ArrayList<Integer> neighbors = adjList.get(u.idx);
            for (int nIdx: neighbors) {

            }
        }

        // backtracing
        List<Integer> res = new ArrayList<>();
        int current = j;
        while (current != -1) {
            res.add(current);
            current = prev[current];        
        }
        Collections.reverse(res);
        return res;
    }

    private class Vertex {
        int idx;
        double dist;
        public Vertex(int idx, double dist) {
            this.idx = idx;
            this.dist = dist;
        }
    }

    private class Edge {
        int endPoint;
        double length;

        public Edge(int start, int end) {
            this.endPoint = end;
            this.length = vertices.get(start).distanceTo(vertices.get(end));
        }

        public Edge(int end, double dist) {
            this.endPoint = end;
            this.length = dist;
        }
    }
}
