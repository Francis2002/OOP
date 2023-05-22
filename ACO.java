package prelim;

import java.util.*;
public class ACO extends Algorithm {

    public static List<Ant> ants;

    public ACO(){
        ACO.ants = new ArrayList<Ant>();
    }

    public static Ant getAnt(int index){
        return ants.get(index);
    }

    @Override
    public void init(){

        //add ants and AntMove events starting on node n1
        for (int i = 0; i < Simulator.niu; i++) {
            ants.add(new Ant(i));
            Simulator.pec.addEvPEC(new AntMove(i));
        }
        Simulator.pec.addEvPEC(new PrintObservation(1, Simulator.simulationTime/20));
    }

    @Override
    public void printParameters(){
        System.out.println("Input parameters:");
        System.out.println("\t" + Simulator.G.V + "\t: number of nodes in the graph");
        System.out.println("\t" + Simulator.n1 + "\t: the nest node");
        System.out.println("\t" + Simulator.alfa + "\t: alpha, ant move event");
        System.out.println("\t" + Simulator.beta + "\t: beta, ant move event");
        System.out.println("\t" + Simulator.delta + "\t: delta, ant move event");
        System.out.println("\t" + Simulator.miu + "\t: eta, pheromone evaporation event");
        System.out.println("\t" + Simulator.ro + "\t: rho, pheromone evaporation event");
        System.out.println("\t" + Simulator.gama + "\t: pheromone level");
        System.out.println("\t" + Simulator.niu + "\t: ant colony size");
        System.out.println("\t" + Simulator.simulationTime + "\t: final instant");
        System.out.println();
    }
}
