package prelim;

import java.util.*;
public class Graph {

    // Create the graph
    int V = 4;
    ArrayList<ArrayList<Node>> adj = new ArrayList<ArrayList<Node>>(V);

        // Add edge(source, destination, weight, pheromone level)
    public void addEdge(int s, int d, int w, double f) {
        adj.get(s).add(new Node(d, w, f));
        adj.get(d).add(new Node(s, w, f));
    }

    // Print the graph
    public void printGraph() {
        for (int i = 0; i < adj.size(); i++) {
        System.out.println("\nVertex " + i + ":");
        for (int j = 0; j < adj.get(i).size(); j++) {
            System.out.print(" -> " + adj.get(i).get(j).id);
        }
        System.out.println();
        }
    }
}
