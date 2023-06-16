package prelim.Graphs;
/**
 * The Edge class represents an edge in a graph.
 */
public class Edge {
    private int id;
    private int weight;
    /**
     * Constructs an Edge object with the specified ID and weight.
     *
     * @param id     The ID of the edge.
     * @param weight The weight of the edge.
     */
    public Edge(int id, int weight)
    {
        this.id = id;
        this.weight = weight;
    }
    /**
     * Retrieves the ID of the edge.
     *
     * @return The ID of the edge.
     */
    public int getId(){
        return id;
    }
    /**
     * Retrieves the weight of the edge.
     *
     * @return The weight of the edge.
     */
    public int getWeight(){
        return weight;
    }
}
