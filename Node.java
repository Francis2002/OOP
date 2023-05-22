package prelim;

public class Node {
    int id;
    int weight;
    double phero = 0.0;

    public Node(int id, int weight)
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

    public double getPhero(){
        return phero;
    }

    public void setPhero(double level)
    {
        phero = level;
    }
}
