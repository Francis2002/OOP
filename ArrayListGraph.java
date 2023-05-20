package prelim;

import java.util.*;
public class ArrayListGraph extends Graph{

    ArrayList<ArrayList<Node>> adj;

    public ArrayListGraph(int v){
        // Create the ArrayListGraph
        this.V = v;
        this.adj = new ArrayList<ArrayList<Node>>(v);

        //head of list of adjacencies of a node with id i will be on index i of Simulator.G.adj
        for (int i = 0; i < V; i++)
            adj.add(new ArrayList<Node>());
    }

        // Add edge(source, destination, weight, pheromone level)
    public void addEdge(int s, int d, int w) {
        adj.get(s).add(new Node(d, w));
        adj.get(d).add(new Node(s, w));
    }

    // Print the Graph
    public void printGraph() {
        System.out.print("\twith graph:");
        for (int i = 0; i < V; i++) {
            System.out.print("\n\t\t");
            int k = 0;
            for (int j = 0; j < V; j++) {
                if(k < adj.get(i).size()){
                    if(adj.get(i).get(k).id == j)
                    {
                        System.out.print(adj.get(i).get(k).weight + " ");
                        k++;
                    }
                    else
                    {
                        System.out.print("0 ");
                    }
                }
                else
                {
                    System.out.print("0 ");
                }
            }
        }
        System.out.println("\n");
    }

    public void validateWeights(){
        for (int i = 0; i < adj.size(); i++) {
            for (int j = 0; j < adj.get(i).size(); j++) {
                if (adj.get(i).get(j).weight <= 0) {
                    System.out.println("Invalid inputs: All weights should be non-zero positive integers");
                    return;
                }
            }
        }
    }

    public int getSizeOf(int nodeIndex){
        return adj.get(nodeIndex).size();
    }

    public List<Node> getAdjList(int index){
        return adj.get(index);
    }

    public void fillWithRandomAdjacencies(int maxWeight){
        Random random = new Random();
        int weight = 0;
        for (int i = 0; i < V; i++) {
            for (int j = i+1; j < V; j++) {
                weight = random.nextInt(maxWeight + 1);
                if (weight != 0) {
                    addEdge(i, j, weight);
                }
            }
        }
        if(!checksDiracsTheorem()){
            for (int i = 0; i < V; i++) {
                adj.get(i).removeAll(adj.get(i));
            }
            fillWithRandomAdjacencies(maxWeight);
        }
    }

}
