package prelim.Graphs;

import java.util.*;
public abstract class Graph {

    protected int V;

    public abstract void addEdge(int s, int d, int w);

    public abstract void printGraph();

    public abstract void validateWeights();

    public abstract int getNumberOfAdjacenciesOf(int nodeIndex);

    public abstract List<Edge> getAdjacenciesOf(int index);

    public abstract void fillWithRandomAdjacencies(int maxWeight);

    public abstract int getWeightOfEdge(int s, int d);

    public abstract int getSumOfAllWeights();

    public boolean checksDiracsTheorem()
    {
        for (int i = 0; i < V; i++) {
            if (getNumberOfAdjacenciesOf(i) < Math.round(((double)V)/2)) {
                return false;
            }
        }
        return true;
    }

    public int getV(){
        return V;
    }

    public void setV(int v){
        this.V = v;
    }
}
