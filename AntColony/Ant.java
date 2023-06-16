package prelim.AntColony;

import java.util.*;

import prelim.Graphs.*;
import prelim.Simulation.*;

/**
 * The Ant class represents an ant in the Ant Colony Optimization (ACO) algorithm.
 * It keeps track of the ant's id, path, graph, and colony it belongs to.
 * The ant is responsible for updating its set of possible targets (J set),
 * choosing the next target based on probabilities, and handling the completion of
 * a Hamiltonian cycle.
 */
public class Ant {
    int id;
    List<Integer> J = new ArrayList<Integer>();
    ArrayList<Integer> path = new ArrayList<Integer>();
    Graph G;
    ACO colony;
    int nest;

    /**
     * Constructs an Ant object with the specified id, graph, and colony.
     *
     * @param id     The id of the ant.
     * @param G      The graph on which the ant performs the search.
     * @param colony The ant colony to which the ant belongs.
     */
    public Ant(int id, Graph G, ACO colony)
    {
        this.id = id;
        this.G = G;
        this.colony = colony;
        this.nest = (int)colony.getParam("n1");
        //adding to J set id of nodes ajacent to n1
        for (Edge edge : G.getAdjacenciesOf(nest)) {
            J.add(edge.getId());
        }
        /*for (int i = 0; i < G.getNumberOfAdjacenciesOf(ACO.n1); i++) {
            J.add(G.getAdjacenciesOf(ACO.n1).get(i).getId());
        }*/
        this.path.add(nest);
    }

    /**
     * Gets the J set of the ant.
     *
     * @return The J set of the ant.
     */
    public List<Integer> getJ(){
        return J;
    }

    /**
     * Gets the path of the ant.
     *
     * @return The path of the ant.
     */
    public ArrayList<Integer> getPath(){
        return path;
    }

    /**
     * Updates the J set of the ant.
     * Resets the J set and adds the ids of nodes adjacent to the last node in the path,
     * excluding nodes that are already in the path.
     */
    public void updateJ(){
        //reset J
        J.removeAll(J);
        //add id of nodes adjacent to last node in path
        for (Edge edge : G.getAdjacenciesOf(getLastInPath())) {
            //only add nodes that are not in path
            if (!this.path.contains(edge.getId())) {
                J.add(edge.getId());
            }
            //if there is a chance of completing Hamiltonian cycle, then add n1 to possible targets if n1 is adjacent to last node in path
            if (edge.getId() == nest && this.path.size() == G.getV()) {
                J.add(nest);
            }
        }
    }

    /**
     * Gets the last node in the path of the ant.
     *
     * @return The last node in the path.
     */
    public int getLastInPath(){
        return path.get(path.size()-1);
    }

    /**
     * Increments the Mevents counter in the ant colony.
     * This method is used to track the number of events performed by the colony.
     */ 
    public void incrementMevents(){
        colony.incrementMevents();
    }
    /**
     * Retrieves the value of a parameter from the ant colony.
     *
     * @param param The parameter to retrieve.
     * @return The value of the specified parameter.
     */
    public double getParam(String param){
        return colony.getParam(param);
    }
    /**
     * Gets the weight of an edge between two nodes in the graph.
     *
     * @param s The source node.
     * @param d The destination node.
     * @return The weight of the edge between the source and destination nodes.
     */
    public int getWeightOfEdge(int s, int d){
        return G.getWeightOfEdge(s, d);
    }
    /**
     * Checks if the ant has completed a Hamiltonian cycle.
     *
     * @return True if the ant has completed a Hamiltonian cycle, false otherwise.
     */
    public boolean checkHamilton(){
        if (getPath().size() == G.getV()) {
            return true;
        }
        return false;
    }
    /**
     * Generates a random number from an exponential distribution.
     *
     * @param m The mean of the exponential distribution.
     * @return A random number from the exponential distribution.
     */
    public double expRandom(double m){
        return colony.expRandom(m);
    }
    /**
     * Calculates the probabilities of choosing each target in the J set.
     *
     * @param probs The list to store the probabilities.
     * @return The updated list of probabilities.
     */
    public List<Double> getProbs(List<Double> probs)
    {
        double ci = 0;
        
        for (int i = 0; i < J.size(); i++) {
            double cijk = (colony.getParam("alfa") + colony.getPheroOfEdge(getLastInPath(), J.get(i)))/(colony.getParam("beta") + G.getWeightOfEdge(getLastInPath(), J.get(i)));
            probs.add(cijk);
            ci += cijk;
        }
        for (int i = 0; i < J.size(); i++) {
            probs.set(i, probs.get(i)/ci);
        }
        return probs;
    }
    /**
     * Chooses the next target node based on probabilities.
     *
     * @return The id of the chosen target node.
     */
    public int getTarget(){
        //J set not empty
        if(getJ().size() != 0)
        {
            List<Double> probs = new ArrayList<Double>();
            probs = getProbs(probs);
            Random rand = new Random();

            // Obtain a decimal number between [0 - 1].
            double lotteryNum = rand.nextDouble();

            double currentProbBlock = 0;
            for (int i = 0; i < probs.size(); i++) {
                if (lotteryNum < probs.get(i) + currentProbBlock) {
                    return getJ().get(i);
                }
                currentProbBlock += probs.get(i);
            }
            return -1;
        }
        //empty J set
        else
        {
            Random rand = new Random();

            // Obtain a number between [0 - ACO.G.getAdjacenciesOf(ant.getPath().get(ant.getPath().size()-1)).size()-1].
            int targetIndex = rand.nextInt(G.getNumberOfAdjacenciesOf(getLastInPath()));

            return G.getAdjacenciesOf(getLastInPath()).get(targetIndex).getId();
        }
    }
    /**
     * Handles the completion of a Hamiltonian cycle by the ant.
     *
     * @param target    The target node where the cycle completes.
     * @param pec       The PEC (Pheromone Evaporation Controller) instance.
     * @param timeStamp The timestamp of the completion event.
     */
    public void handleCycleCompletion(int target, PEC pec, double timeStamp){
        boolean completedHamilton = false;
        for (int i = 0; i < getPath().size(); i++) 
        {
            //find beginning of cycle
            if (getPath().get(i).equals(target)) 
            {
                //if enters here, possibly completed Hamiltonian cycle
                if(i == 0){
                    completedHamilton = checkHamilton();
                    if(completedHamilton)
                    {
                        int totalWeight = 0;

                        //calculatig total weight of path
                        int currentNodeId = nest;
                        for (int j = 1; j < getPath().size(); j++) 
                        {
                            //find adjacency node with id==ant.getPath().get(j) in ACO.G.getAdjacenciesOf(currentNodeId) 
                            totalWeight += G.getWeightOfEdge(currentNodeId, getPath().get(j));
                            currentNodeId = getPath().get(j);
                        }
                        //do not forget to add last weight, from last node in path to n1 = target

                        //find adjacency node with id==target in ACO.G.getAdjacenciesOf(ant.getPath().get(ant.getPath().size()-1)) 
                        totalWeight += G.getWeightOfEdge(currentNodeId, target);

                        colony.addHamilton(getPath(), totalWeight);

                        if(totalWeight <= colony.getBestPathWeight())
                        {
                            colony.updateBestPathWeight(totalWeight);
                            colony.updateBestPath(getPath());
                        }

                        //now we must deploy pheromones, that is, increment pheromone level of adjacency nodes and add PheroEvap events
                        currentNodeId = nest;

                        int sumOfAllWeights = G.getSumOfAllWeights();
                        boolean pheroEvapEventAlreadyInPec = false;
                        for (int j = 1; j < getPath().size(); j++) 
                        {
                            if (colony.getPheroOfEdge(currentNodeId, getPath().get(j)) > 0) {
                                pheroEvapEventAlreadyInPec = true;
                            }
                            //find adjacency node with id==ant.getPath().get(j) in ACO.G.getAdjacenciesOf(currentNodeId) 

                            colony.setPheroOfEdge(currentNodeId, getPath().get(j), (sumOfAllWeights*colony.getParam("gama"))/totalWeight);

                            //find adjacency node with indexes switched, that is, node with id==currentNodeId in ACO.G.getAdjacenciesOf(currentAdjNode.id)
                            
                            colony.setPheroOfEdge(getPath().get(j), currentNodeId, (sumOfAllWeights*colony.getParam("gama"))/totalWeight);
                            
                            if (!pheroEvapEventAlreadyInPec) {
                                pec.addEvPEC(new PheroEvap(currentNodeId, getPath().get(j), timeStamp, colony, pec));
                            }
                            currentNodeId = getPath().get(j);
                            pheroEvapEventAlreadyInPec = false;
                        }
                    }
                }
                //remove cycle
                int antPathSizeBeforeRemove = getPath().size();
                for (int j = i; j < antPathSizeBeforeRemove; j++) 
                {
                    getPath().remove(getPath().size()-1);                 //we are removing from end of cycle ant.getPath().size()-i times
                }
                break;
            }
        }
    }

    /**
     * Adds a given id to the ant's path
     * @param id - id of the node to add to the path
     */
    public void addToPath(int id){
        path.add(id);
    }
}
