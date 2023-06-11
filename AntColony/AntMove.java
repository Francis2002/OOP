package prelim.AntColony;

import prelim.Simulation.*;
public class AntMove extends Event{
    private int target;
    private boolean completedCycle = false;

    Ant ant;

    public AntMove(Ant ant, int id, double currentTime, PEC pec){
        System.out.println("Starting event AntMove creation:");

        //find ant with id in ACO.ants (ants are ordered by id, so the .get method is enough)
        this.ant = ant;

        this.pec = pec;
        
        this.target = ant.getTarget();

        if (this.ant.getPath().contains(this.target)) {
            this.completedCycle = true;
        }

        System.out.println("target is: " + this.target);

        //code to determine timeStamp

        System.out.println("ant.getPath():" + ant.getPath());
        //find adjacency node with id==target in ACO.G.adj 
        
        if(this.target == ant.nest)     // in this case, J set is not empty (because n1 is on J set even if already visited) but a cycle is completed because n1 is always the start node, meaning that visiting again completes a cycle
        {
            this.completedCycle = true;     
        }

        this.timeStamp = currentTime + ant.expRandom(ant.getParam("delta")*ant.getWeightOfEdge(ant.getLastInPath(), this.target));
        System.out.println("Event timeStamp:" + this.timeStamp);
        System.out.println("Ended event creation");
        System.out.println();
    }

    @Override
    public void simulateEvent(){
        System.out.println("Started event simulation:");
        System.out.println("J set was:" + ant.getJ());

        ant.incrementMevents();

        if(this.completedCycle){
            this.completedCycle = false;
            ant.handleCycleCompletion(target, pec, timeStamp);
        }

        ant.getPath().add(target);
        ant.updateJ();
        System.out.println("added:" + target + " to ant:" + ant.id + " with new J set:" + ant.J);
        System.out.println("Ended event simulation");
        System.out.println();

        System.out.println("Adding AntMove event:");
        pec.addEvPEC(new AntMove(ant, ant.id, timeStamp, pec));
    }
}