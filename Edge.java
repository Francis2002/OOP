package prelim;

public class Edge {
    int id;
    int weight;

    public Edge(int id, int weight)
    {
        this.id = id;
        this.weight = weight;
    }

    public int getId(){
        return id;
    }

    public int getWeight(){
        return weight;
    }
}
