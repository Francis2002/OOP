package prelim.Graphs;

import java.util.*;
public class ArrayListGraph extends Graph{

    private ArrayList<ArrayList<Edge>> adj;
    /**
    * The ArrayListGraph class represents a graph using an Matrix of adjacency implemented with an ArrayList.
    */
    public ArrayListGraph()
    {
        // Create the ArrayListGraph
        this.adj = new ArrayList<ArrayList<Edge>>();
    }

    /**
     * Adds an edge to the graph with the specified source, destination, and weight.
     *
     * @param s The source node index.
     * @param d The destination node index.
     * @param w The weight of the edge.
     */
    public void addEdge(int s, int d, int w) 
    {
        while (adj.size() - 1 < s) {
            adj.add(new ArrayList<Edge>());
        }
        while (adj.size() - 1 < d) {
            adj.add(new ArrayList<Edge>());
        }
        adj.get(s).add(new Edge(d, w));
        adj.get(d).add(new Edge(s, w));
    }

    /**
     * Prints the graph.
     */
    public void printGraph() {
        System.out.print("\twith graph:");
        for (int i = 0; i < V; i++) {
            System.out.print("\n\t\t");
            int k = 0;
            for (int j = 0; j < V; j++) {
                if(k < adj.get(i).size()){
                    if(adj.get(i).get(k).getId() == j)
                    {
                        System.out.print(adj.get(i).get(k).getWeight() + " ");
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

    /**
     * Validates the weights of the graph.
     * Checks if all weights are non-zero positive integers.
     */
    public void validateWeights(){
        for (int i = 0; i < adj.size(); i++) {
            for (int j = 0; j < adj.get(i).size(); j++) {
                if (adj.get(i).get(j).getWeight() <= 0) {
                    System.out.println("Invalid inputs: All weights should be non-zero positive integers");
                    return;
                }
            }
        }
    }
    /**
     * Returns the number of adjacencies of the specified node.
     *
     * @param nodeIndex The index of the node.
     * @return The number of adjacencies of the node.
     */
    public int getNumberOfAdjacenciesOf(int nodeIndex){
        return adj.get(nodeIndex).size();
    }
    /**
     * Returns a list of edges representing the adjacencies of the specified node.
     *
     * @param index The index of the node.
     * @return The list of edges representing the adjacencies of the node.
     */
    public List<Edge> getAdjacenciesOf(int index){
        return adj.get(index);
    }
    /**
     * Fills the graph with random adjacencies with weights up to the specified maximum weight.
     *
     * @param maxWeight The maximum weight of the adjacencies.
     */
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
        printGraph();
        if(!checksDiracsTheorem()){
            for (int i = 0; i < V; i++) {
                adj.get(i).removeAll(adj.get(i));
            }
            fillWithRandomAdjacencies(maxWeight);
        }
    }
     /**
     * Returns the weight of the edge between the specified source and destination nodes.
     *
     * @param s The source node index.
     * @param d The destination node index.
     * @return The weight of the edge.
     */
    public int getWeightOfEdge(int s, int d){
        Edge currentAdjNode = getAdjacenciesOf(s).get(0); //default as first

        //find adjacency node with id==J.get(i) in G.getAdjacenciesOf(ant.getPath().get(ant.getPath().size()-1)) 
        for (int j = 0; j < getNumberOfAdjacenciesOf(s); j++) {
            if (getAdjacenciesOf(s).get(j).getId() == d) {
                currentAdjNode = getAdjacenciesOf(s).get(j);
                break;
            }
        }
        return currentAdjNode.getWeight();
    }
    /**
     * Calculates and returns the sum of weights of all edges in the graph.
     *
     * @return The sum of weights of all edges.
     */
    @Override
    public int getSumOfAllWeights(){
        int cnt = 0;
        for (int i = 0; i < V; i++) {
            for (int j = i+1; j < V; j++) {
                cnt += getWeightOfEdge(i, j);
            }
        }
        return cnt;
    }
}
