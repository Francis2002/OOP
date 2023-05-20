package prelim;

import java.util.*;
public abstract class Graph {

    int V;

    public abstract void addEdge(int s, int d, int w);

    public abstract void printGraph();

    public abstract void validateWeights();

    public abstract int getSizeOf(int nodeIndex);

    public abstract List<Node> getAdjList(int index);

    public abstract void fillWithRandomAdjacencies(int maxWeight);

    public boolean checksDiracsTheorem()
    {
        for (int i = 0; i < V; i++) {
            if (getSizeOf(i) < Math.round(((double)V)/2)) {
                return false;
            }
        }
        return true;
    }
}
