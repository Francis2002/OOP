package prelim.Graphs;

import java.util.*;
public class ArrayListGraph extends Graph{

    private ArrayList<ArrayList<Edge>> adj;

    public ArrayListGraph(){
        // Create the ArrayListGraph
        this.adj = new ArrayList<ArrayList<Edge>>();
    }

        // Add edge(source, destination, weight, pheromone level)
    public void addEdge(int s, int d, int w) {
        System.out.println("size: " + adj.size());
        while (adj.size() - 1 < s) {
            System.out.println("adding head in s");
            adj.add(new ArrayList<Edge>());
        }
        while (adj.size() - 1 < d) {
            System.out.println("adding head in d");
            adj.add(new ArrayList<Edge>());
        }
        adj.get(s).add(new Edge(d, w));
        adj.get(d).add(new Edge(s, w));
    }

    // Print the Graph
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
     * @param 
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

    public int getNumberOfAdjacenciesOf(int nodeIndex){
        return adj.get(nodeIndex).size();
    }

    public List<Edge> getAdjacenciesOf(int index){
        return adj.get(index);
    }

    public void fillWithRandomAdjacencies(int maxWeight){
        Random random = new Random();
        int weight = 0;
        System.out.println("V: " + V);
        for (int i = 0; i < V; i++) {
            for (int j = i+1; j < V; j++) {
                weight = random.nextInt(maxWeight + 1);
                if (weight != 0) {
                    addEdge(i, j, weight);
                }
            }
        }
        System.out.println("printing graph");
        printGraph();
        if(!checksDiracsTheorem()){
            for (int i = 0; i < V; i++) {
                adj.get(i).removeAll(adj.get(i));
            }
            fillWithRandomAdjacencies(maxWeight);
        }
    }

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
