package prelim.Graphs;

import java.util.*;
/**
 * The Graph class represents an abstract graph.
 */
public abstract class Graph {

    protected int V;
    /**
     * Adds an edge to the graph.
     *
     * @param s The source vertex of the edge.
     * @param d The destination vertex of the edge.
     * @param w The weight of the edge.
     */
    public abstract void addEdge(int s, int d, int w);
    /**
     * Prints the graph.
     */
    public abstract void printGraph();
    /**
     * Validates the weights of the graph.
     */
    public abstract void validateWeights();
    /**
     * Retrieves the number of adjacencies of a given node.
     *
     * @param nodeIndex The index of the node.
     * @return The number of adjacencies.
     */
    public abstract int getNumberOfAdjacenciesOf(int nodeIndex);
    /**
     * Retrieves the list of edges adjacent to a given node.
     *
     * @param index The index of the node.
     * @return The list of adjacent edges.
     */
    public abstract List<Edge> getAdjacenciesOf(int index);
    /**
     * Fills the graph with random adjacencies,which its weight must not exceed the maxWeight.
     *
     * @param maxWeight The maximum weight of the adjacencies.
     */
    public abstract void fillWithRandomAdjacencies(int maxWeight);
    /**
     * Retrieves the weight of an edge between two vertices.
     *
     * @param s The source vertex of the edge.
     * @param d The destination vertex of the edge.
     * @return The weight of the edge.
     */
    public abstract int getWeightOfEdge(int s, int d);
    /**
     * Retrieves the sum of all edge weights in the graph.
     *
     * @return The sum of all edge weights.
     */
    public abstract int getSumOfAllWeights();
    /**
     * Checks if the graph satisfies Dirac's theorem.
     *
     * @return True if the graph satisfies Dirac's theorem, false otherwise.
     */
    public boolean checksDiracsTheorem()
    {
        for (int i = 0; i < V; i++) {
            if (getNumberOfAdjacenciesOf(i) < Math.round(((double)V)/2)) {
                return false;
            }
        }
        return true;
    }
    /**
     * Retrieves the number of vertices in the graph.
     *
     * @return The number of vertices.
     */
    public int getV(){
        return V;
    }
     /**
     * Sets the number of vertices in the graph.
     *
     * @param v The number of vertices.
     */
    public void setV(int v){
        this.V = v;
    }
}
