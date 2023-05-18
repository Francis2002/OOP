package prelim;

import java.util.*;
public class AntMove extends Event{
    int target;
    boolean completedHamilton = false;
    boolean completedCycle = false;

    Ant ant;

    int maxDecimalDigits(List<Double> list)
    {
        int max = 0;
        for (int i = 0; i < list.size(); i++) {
            String text = Double.toString(Math.abs(list.get(i)));
            int integerPlaces = text.indexOf('.');
            int decimalPlaces = text.length() - integerPlaces - 1;
            if (decimalPlaces > max) {
                max = decimalPlaces;
            }
        }
        return max;
    }

    List<Double> getProbs(List<Integer> J, List<Double> probs)
    {
        System.out.println("determining probabilities:");
        double ci = 0;
        
        Node currentAdjNode = nodeList.get(ant.path.get(ant.path.size()-1)).get(0); //default as first
        for (int i = 0; i < J.size(); i++) {
            //find adjacency node with id==J.get(i) in nodeList.get(ant.path.get(ant.path.size()-1)) 
            for (int j = 0; j < nodeList.get(ant.path.get(ant.path.size()-1)).size(); j++) {
                if (nodeList.get(ant.path.get(ant.path.size()-1)).get(j).id == J.get(i)) {
                    currentAdjNode = nodeList.get(ant.path.get(ant.path.size()-1)).get(j);
                    break;
                }
            }

            System.out.println("currentAdjNode.phero:" + currentAdjNode.phero + "; currentAdjNode.weight:" + currentAdjNode.weight);
            double cijk = (alfa + currentAdjNode.phero)/(beta + currentAdjNode.weight);
            probs.add(cijk);
            ci += cijk;
        }
        for (int i = 0; i < J.size(); i++) {
            probs.set(i, probs.get(i)/ci);
        }
        return probs;
    }

    boolean checkHamilton(){
        if (ant.path.size() == Simulator.G.V) {
            return true;
        }
        return false;
    }

    public AntMove(int id){
        System.out.println("Starting event AntMove creation:");
        //find ant with id in Simulator.ants 
        for (int i = 0; i < Simulator.ants.size(); i++) {
            if (Simulator.ants.get(i).id == id) {
                this.ant = Simulator.ants.get(i);
                break;
            }
        }
        //J set not empty
        if(this.ant.J.size() != 0)
        {
            System.out.println("J set is not empty");
            List<Double> probs = new ArrayList<Double>();
            probs = getProbs(this.ant.J, probs);
            System.out.println("J set:" + this.ant.J);
            System.out.println("move probabilities:" + probs);

            int maxDecimalDigits = maxDecimalDigits(probs);

            double ci = 0;
            for (int i = 0; i < probs.size(); i++) {
                ci += probs.get(i);
            }

            int mult = (int)Math.pow(10, maxDecimalDigits);

            Random rand = new Random();

            // Obtain a number between [0 - ci*10^maxDecimalDigits-1].
            int lotteryNum = rand.nextInt((int)(ci*mult));

            int currentProbBlock = 0;
            for (int i = 0; i < probs.size(); i++) {
                if (lotteryNum < probs.get(i)*mult + currentProbBlock) {
                    this.target = this.ant.J.get(i);
                    break;
                }
                currentProbBlock += probs.get(i)*mult;
            }
        }
        //empty J set
        else
        {
            System.out.println("J set is empty");
            Random rand = new Random();

            // Obtain a number between [0 - nodeList.get(ant.path.get(ant.path.size()-1)).size()-1].
            int targetIndex = rand.nextInt(nodeList.get(ant.path.get(ant.path.size()-1)).size());

            this.target = nodeList.get(ant.path.get(ant.path.size()-1)).get(targetIndex).id;

            this.completedCycle = true;
        }

        //code to determine timeStamp

        System.out.println("ant.path:" + ant.path);
        //find adjacency node with id==target in nodeList 
        Node targetAdjNode = nodeList.get(ant.path.get(ant.path.size()-1)).get(0);      //default as first
        for (int j = 0; j < nodeList.get(ant.path.get(ant.path.size()-1)).size(); j++) {
            if (nodeList.get(ant.path.get(ant.path.size()-1)).get(j).id == this.target) {
                targetAdjNode = nodeList.get(ant.path.get(ant.path.size()-1)).get(j);
                break;
            }
        }
        if(this.target == Simulator.n1)     // in this case, J set is not empty (because n1 is on J set even if already visited) but a cycle is completed because n1 is always the start node, meaning that visiting again completes a cycle
        {
            this.completedCycle = true;     
        }

        this.timeStamp = Simulator.currentTime + Simulator.expRandom(delta*targetAdjNode.weight);
        System.out.println("Event timeStamp:" + this.timeStamp);
        System.out.println("Ended event creation");
        System.out.println();
    }

    @Override
    public void simulateEvent(){
        System.out.println("Started event simulation:");
        if(completedCycle){
            completedCycle = false;
            for (int i = 0; i < ant.path.size(); i++) 
            {
                //find beginning of cycle
                if (ant.path.get(i).equals(target)) 
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
                            for (int j = 1; j < ant.path.size(); j++) 
                            {
                                //find adjacency node with id==ant.path.get(j) in nodeList.get(currentNodeId) 
                                Node currentAdjNode = nodeList.get(currentNodeId).get(0);      //default as first
                                for (int k = 0; k < nodeList.get(currentNodeId).size(); k++) {
                                    if (nodeList.get(currentNodeId).get(k).id == ant.path.get(j)) {
                                        currentAdjNode = nodeList.get(currentNodeId).get(k);
                                        break;
                                    }
                                }
                                totalWeight += currentAdjNode.weight;
                                currentNodeId = currentAdjNode.id;
                            }
                            //do not forget to add last weght, from last node in path to n1 = target

                            //find adjacency node with id==target in nodeList.get(ant.path.get(ant.path.size()-1)) 
                            Node currentAdjNode = nodeList.get(currentNodeId).get(0);      //default as first
                            for (int k = 0; k < nodeList.get(ant.path.get(ant.path.size()-1)).size(); k++) {
                                if (nodeList.get(ant.path.get(ant.path.size()-1)).get(k).id == target) {
                                    currentAdjNode = nodeList.get(ant.path.get(ant.path.size()-1)).get(k);
                                    break;
                                }
                            }

                            totalWeight += currentAdjNode.weight;

                            if(totalWeight < Simulator.bestPathWeight)
                            {
                                Simulator.bestPathWeight = totalWeight;
                                Simulator.bestPath.removeAll(Simulator.bestPath);
                                Simulator.bestPath.addAll(ant.path);
                                Simulator.bestPath.add(target);
                            }

                            //now we must deploy pheromones, that is, increment pheromone level of adjacency nodes and add PheroEvap events
                            currentNodeId = Simulator.n1;

                            for (int j = 1; j < ant.path.size(); j++) 
                            {
                                //find adjacency node with id==ant.path.get(j) in nodeList.get(currentNodeId) 
                                currentAdjNode = nodeList.get(currentNodeId).get(0);      //default as first
                                for (int k = 0; k < nodeList.get(currentNodeId).size(); k++) {
                                    if (nodeList.get(currentNodeId).get(k).id == ant.path.get(j)) {
                                        currentAdjNode = nodeList.get(currentNodeId).get(k);
                                        break;
                                    }
                                }

                                currentAdjNode.phero = gama/totalWeight;

                                //find adjacency node with indexes switched, that is, node with id==currentNodeId in nodeList.get(currentAdjNode.id)
                                Node auxCurrentAdjNode = nodeList.get(currentAdjNode.id).get(0);      //default as first
                                for (int k = 0; k < nodeList.get(currentAdjNode.id).size(); k++) {
                                    if (nodeList.get(currentAdjNode.id).get(k).id == currentNodeId) {
                                        auxCurrentAdjNode = nodeList.get(currentAdjNode.id).get(k);
                                        break;
                                    }
                                }
                                auxCurrentAdjNode.phero = gama/totalWeight;
                                
                                pec.addEvPEC(new PheroEvap(currentNodeId, currentAdjNode.id));
                                currentNodeId = currentAdjNode.id;
                            }
                        }
                    }
                    //remove cycle
                    System.out.println("i was " + i + " ant.path.size() was " + ant.path.size() + " ant.path was" + ant.path);
                    int antPathSizeBeforeRemove = ant.path.size();
                    for (int j = i; j < antPathSizeBeforeRemove; j++) 
                    {
                        System.out.println("removed:" + ant.path.get(ant.path.size()-1));
                        ant.path.remove(ant.path.size()-1);                 //we are removing from end of cycle ant.path.size()-i times
                    }
                    System.out.println("removed cycle - new path:" + ant.path);
                    break;
                }
            }
        }

        ant.path.add(target);
        ant.updateJ();
        System.out.println("added:" + target + " to ant:" + ant.id + " with new J set:" + ant.J);
        System.out.println("Ended event simulation");
        System.out.println();

        System.out.println("Adding AntMove event:");
        pec.addEvPEC(new AntMove(ant.id));
    }
}