package prelim.AntColony;

import prelim.Simulation.*;

/**
* Represents an event where an ant moves to a target node.
*/
public class AntMove extends Event{
    private int target;
    private boolean completedCycle = false;

    Ant ant;

    /**
     * Creates a new instance of the AntMove event.
     *
     * @param ant          The ant associated with this event.
     * @param id           The ID of the ant.
     * @param currentTime  The current time of the simulation.
     * @param pec          The PEC (Pheromone Evaporation Controller) instance.
    */
    public AntMove(Ant ant, int id, double currentTime, PEC pec){

        //find ant with id in ACO.ants (ants are ordered by id, so the .get method is enough)
        this.ant = ant;

        this.pec = pec;
        
        this.target = ant.getTarget();

        if (this.ant.getPath().contains(this.target)) {
            this.completedCycle = true;
        }


        //code to determine timeStamp

        //find adjacency node with id==target in ACO.G.adj 
        
        if(this.target == ant.nest)     // in this case, J set is not empty (because n1 is on J set even if already visited) but a cycle is completed because n1 is always the start node, meaning that visiting again completes a cycle
        {
            this.completedCycle = true;     
        }

        this.timeStamp = currentTime + ant.expRandom(ant.getParam("delta")*ant.getWeightOfEdge(ant.getLastInPath(), this.target));
    }

    /**
     * Simulates the AntMove event.
    */
    @Override
    public void simulateEvent(){

        ant.incrementMevents();

        if(this.completedCycle){
            this.completedCycle = false;
            ant.handleCycleCompletion(target, pec, timeStamp);
        }

        ant.addToPath(target);
        ant.updateJ();

        pec.addEvPEC(new AntMove(ant, ant.id, timeStamp, pec));
    }
}