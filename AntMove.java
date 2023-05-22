package prelim;

import java.util.*;
public class AntMove extends Event{
    int target;
    boolean completedHamilton = false;
    boolean completedCycle = false;

    Ant ant;

    List<Double> getProbs(List<Integer> J, List<Double> probs)
    {
        System.out.println("determining probabilities:");
        double ci = 0;
        
        for (int i = 0; i < J.size(); i++) {
            System.out.println("currentAdjNode.phero:" + Simulator.G.getPheroOfEdge(ant.getPath().get(ant.getPath().size()-1), J.get(i)) + "; currentAdjNode.weight:" + Simulator.G.getWeightOfEdge(ant.getPath().get(ant.getPath().size()-1), J.get(i)));
            double cijk = (alfa + Simulator.G.getPheroOfEdge(ant.getPath().get(ant.getPath().size()-1), J.get(i)))/(beta + Simulator.G.getWeightOfEdge(ant.getPath().get(ant.getPath().size()-1), J.get(i)));
            probs.add(cijk);
            ci += cijk;
        }
        for (int i = 0; i < J.size(); i++) {
            probs.set(i, probs.get(i)/ci);
        }
        return probs;
    }

    boolean checkHamilton(){
        if (ant.getPath().size() == Simulator.G.getV()) {
            return true;
        }
        return false;
    }

    public AntMove(int id){
        System.out.println("Starting event AntMove creation:");

        //find ant with id in Simulator.ants (ants are ordered by id, so the .get method is enough)
        this.ant = ACO.getAnt(id);
        //J set not empty
        if(this.ant.getJ().size() != 0)
        {
            System.out.println("J set is not empty");
            List<Double> probs = new ArrayList<Double>();
            probs = getProbs(this.ant.getJ(), probs);
            System.out.println("J set:" + this.ant.getJ());
            System.out.println("move probabilities:" + probs);

            Random rand = new Random();

            // Obtain a decimal number between [0 - 1].
            double lotteryNum = rand.nextDouble();

            double currentProbBlock = 0;
            for (int i = 0; i < probs.size(); i++) {
                if (lotteryNum < probs.get(i) + currentProbBlock) {
                    this.target = this.ant.getJ().get(i);
                    break;
                }
                currentProbBlock += probs.get(i);
            }
        }
        //empty J set
        else
        {
            System.out.println("J set is empty");
            Random rand = new Random();

            // Obtain a number between [0 - Simulator.G.getAdjacenciesOf(ant.getPath().get(ant.getPath().size()-1)).size()-1].
            int targetIndex = rand.nextInt(Simulator.G.getNumberOfAdjacenciesOf(ant.getPath().get(ant.getPath().size()-1)));

            this.target = Simulator.G.getAdjacenciesOf(ant.getPath().get(ant.getPath().size()-1)).get(targetIndex).id;

            this.completedCycle = true;
        }

        System.out.println("target is: " + this.target);

        //code to determine timeStamp

        System.out.println("ant.getPath():" + ant.getPath());
        //find adjacency node with id==target in Simulator.G.adj 
        
        if(this.target == Simulator.n1)     // in this case, J set is not empty (because n1 is on J set even if already visited) but a cycle is completed because n1 is always the start node, meaning that visiting again completes a cycle
        {
            this.completedCycle = true;     
        }

        this.timeStamp = Simulator.currentTime + Simulator.expRandom(delta*Simulator.G.getWeightOfEdge(ant.getPath().get(ant.getPath().size()-1), this.target));
        System.out.println("Event timeStamp:" + this.timeStamp);
        System.out.println("Ended event creation");
        System.out.println();
    }

    @Override
    public void simulateEvent(){
        System.out.println("Started event simulation:");
        System.out.println("J set was:" + ant.getJ());

        Simulator.mevents += 1;

        if(completedCycle){
            completedCycle = false;
            for (int i = 0; i < ant.getPath().size(); i++) 
            {
                //find beginning of cycle
                if (ant.getPath().get(i).equals(target)) 
                {
                    //if enters here, possibly completed Hamiltonian cycle
                    if(i == 0){
                        System.out.println("returned to n1");
                        completedHamilton = checkHamilton();
                        if(completedHamilton)
                        {
                            int totalWeight = 0;

                            //calculatig total weight of path
                            int currentNodeId = Simulator.n1;
                            for (int j = 1; j < ant.getPath().size(); j++) 
                            {
                                //find adjacency node with id==ant.getPath().get(j) in Simulator.G.getAdjacenciesOf(currentNodeId) 
                                totalWeight += Simulator.G.getWeightOfEdge(currentNodeId, ant.getPath().get(j));
                                currentNodeId =  ant.getPath().get(j);
                            }
                            //do not forget to add last weght, from last node in path to n1 = target

                            //find adjacency node with id==target in Simulator.G.getAdjacenciesOf(ant.getPath().get(ant.getPath().size()-1)) 
                            totalWeight += Simulator.G.getWeightOfEdge(currentNodeId, this.target);;

                            if(totalWeight < Simulator.bestPathWeight)
                            {
                                Simulator.bestPathWeight = totalWeight;
                                Simulator.bestPath.removeAll(Simulator.bestPath);
                                Simulator.bestPath.addAll(ant.getPath());
                                Simulator.bestPath.add(target);
                            }

                            //now we must deploy pheromones, that is, increment pheromone level of adjacency nodes and add PheroEvap events
                            currentNodeId = Simulator.n1;

                            for (int j = 1; j < ant.getPath().size(); j++) 
                            {
                                //find adjacency node with id==ant.getPath().get(j) in Simulator.G.getAdjacenciesOf(currentNodeId) 

                                Simulator.G.setPheroOfEdge(currentNodeId, ant.getPath().get(j), gama/totalWeight);

                                //find adjacency node with indexes switched, that is, node with id==currentNodeId in Simulator.G.getAdjacenciesOf(currentAdjNode.id)
                                
                                Simulator.G.setPheroOfEdge(ant.getPath().get(j), currentNodeId, gama/totalWeight);
                                
                                pec.addEvPEC(new PheroEvap(currentNodeId, ant.getPath().get(j)));
                                currentNodeId = ant.getPath().get(j);
                            }
                        }
                    }
                    //remove cycle
                    System.out.println("target was: " + this.target);
                    System.out.println("i was " + i + " ant.getPath().size() was " + ant.getPath().size() + " ant.getPath() was" + ant.getPath());
                    int antPathSizeBeforeRemove = ant.getPath().size();
                    for (int j = i; j < antPathSizeBeforeRemove; j++) 
                    {
                        System.out.println("removed:" + ant.getPath().get(ant.getPath().size()-1));
                        ant.getPath().remove(ant.getPath().size()-1);                 //we are removing from end of cycle ant.getPath().size()-i times
                    }
                    System.out.println("removed cycle - new path:" + ant.getPath());
                    break;
                }
            }
        }

        ant.getPath().add(target);
        ant.updateJ();
        System.out.println("added:" + target + " to ant:" + ant.id + " with new J set:" + ant.J);
        System.out.println("Ended event simulation");
        System.out.println();

        System.out.println("Adding AntMove event:");
        pec.addEvPEC(new AntMove(ant.id));
    }
}