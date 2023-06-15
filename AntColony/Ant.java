package prelim.AntColony;

import java.util.*;

import prelim.Graphs.*;
import prelim.Simulation.*;
public class Ant {
    int id;
    List<Integer> J = new ArrayList<Integer>();
    ArrayList<Integer> path = new ArrayList<Integer>();
    Graph G;
    ACO colony;
    int nest;

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

    public List<Integer> getJ(){
        return J;
    }

    public ArrayList<Integer> getPath(){
        return path;
    }

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
        /*//add id of nodes adjacent to last node in path
        for (int i = 0; i < ACO.G.getNumberOfAdjacenciesOf(getLastInPath()); i++) {
            //only add nodes that are not in path
            if(!this.path.contains(ACO.G.getAdjacenciesOf(getLastInPath()).get(i).getId()))
            {
                J.add(ACO.G.getAdjacenciesOf(getLastInPath()).get(i).getId());
            }
            //if there is a chance of completing Hamiltonian cycle, then add n1 to possible targets if n1 is adjacent to last node in path
            if (ACO.G.getAdjacenciesOf(getLastInPath()).get(i).getId() == ACO.n1 && this.path.size() == ACO.G.getV()) 
            {
                J.add(ACO.n1);
            }
        }*/
    }

    public int getLastInPath(){
        return path.get(path.size()-1);
    }

    public void incrementMevents(){
        colony.incrementMevents();
    }

    public double getParam(String param){
        return colony.getParam(param);
    }

    public int getWeightOfEdge(int s, int d){
        return G.getWeightOfEdge(s, d);
    }

    public boolean checkHamilton(){
        if (getPath().size() == G.getV()) {
            return true;
        }
        return false;
    }

    public double expRandom(double m){
        return colony.expRandom(m);
    }

    public List<Double> getProbs(List<Double> probs)
    {
        System.out.println("determining probabilities:");
        double ci = 0;
        
        for (int i = 0; i < J.size(); i++) {
            System.out.println("currentAdjNode.phero:" + colony.getPheroOfEdge(getLastInPath(), J.get(i)) + "; currentAdjNode.weight:" + G.getWeightOfEdge(getLastInPath(), J.get(i)));
            double cijk = (colony.getParam("alfa") + colony.getPheroOfEdge(getLastInPath(), J.get(i)))/(colony.getParam("beta") + G.getWeightOfEdge(getLastInPath(), J.get(i)));
            probs.add(cijk);
            ci += cijk;
        }
        for (int i = 0; i < J.size(); i++) {
            probs.set(i, probs.get(i)/ci);
        }
        return probs;
    }

    public int getTarget(){
        //J set not empty
        if(getJ().size() != 0)
        {
            System.out.println("J set is not empty");
            List<Double> probs = new ArrayList<Double>();
            probs = getProbs(probs);
            System.out.println("J set:" + getJ());
            System.out.println("move probabilities:" + probs);

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
            System.out.println("J set is empty");
            Random rand = new Random();

            // Obtain a number between [0 - ACO.G.getAdjacenciesOf(ant.getPath().get(ant.getPath().size()-1)).size()-1].
            int targetIndex = rand.nextInt(G.getNumberOfAdjacenciesOf(getLastInPath()));

            return G.getAdjacenciesOf(getLastInPath()).get(targetIndex).getId();
        }
    }

    public void handleCycleCompletion(int target, PEC pec, double timeStamp){
        System.out.println("handleCycleCompletion");
        boolean completedHamilton = false;
        for (int i = 0; i < getPath().size(); i++) 
        {
            //find beginning of cycle
            if (getPath().get(i).equals(target)) 
            {
                //if enters here, possibly completed Hamiltonian cycle
                if(i == 0){
                    System.out.println("returned to n1");
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
                        System.out.println("sum of all weights: " + sumOfAllWeights);
                        boolean pheroEvapEventAlreadyInPec = false;
                        for (int j = 1; j < getPath().size(); j++) 
                        {
                            if (colony.getPheroOfEdge(currentNodeId, getPath().get(j)) > 0) {
                                pheroEvapEventAlreadyInPec = true;
                                System.out.println("evap already in pec");
                            }
                            //find adjacency node with id==ant.getPath().get(j) in ACO.G.getAdjacenciesOf(currentNodeId) 
                            System.out.println("gama :" + colony.getParam("gama"));

                            colony.setPheroOfEdge(currentNodeId, getPath().get(j), (sumOfAllWeights*colony.getParam("gama"))/totalWeight);

                            System.out.println("phero level: " + colony.getPheroOfEdge(currentNodeId, getPath().get(j)));
                            //find adjacency node with indexes switched, that is, node with id==currentNodeId in ACO.G.getAdjacenciesOf(currentAdjNode.id)
                            
                            colony.setPheroOfEdge(getPath().get(j), currentNodeId, (sumOfAllWeights*colony.getParam("gama"))/totalWeight);
                            
                            System.out.println("phero level: " + colony.getPheroOfEdge(getPath().get(j), currentNodeId));

                            if (!pheroEvapEventAlreadyInPec) {
                                pec.addEvPEC(new PheroEvap(currentNodeId, getPath().get(j), timeStamp, colony, pec));
                            }
                            currentNodeId = getPath().get(j);
                            pheroEvapEventAlreadyInPec = false;
                        }
                    }
                }
                //remove cycle
                System.out.println("target was: " + target);
                System.out.println("i was " + i + " ant.getPath().size() was " + getPath().size() + " ant.getPath() was" + getPath());
                int antPathSizeBeforeRemove = getPath().size();
                for (int j = i; j < antPathSizeBeforeRemove; j++) 
                {
                    System.out.println("removed:" + getLastInPath());
                    getPath().remove(getPath().size()-1);                 //we are removing from end of cycle ant.getPath().size()-i times
                }
                System.out.println("removed cycle - new path:" + getPath());
                break;
            }
        }
    }
}
