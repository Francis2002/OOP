package prelim.AntColony;

import prelim.Simulation.*;
public class PheroEvap extends Event {
    private int s;
    private int d;
    private ACO colony;

    public PheroEvap(int s, int d, double currentTime, ACO colony, PEC pec){
        System.out.println("Adding PheroEvap event:");
        this.s = s;
        this.d = d;
        this.colony = colony;
        this.timeStamp = currentTime + colony.expRandom(colony.getParam("miu"));
        this.pec = pec;
        System.out.println("Ended event creation; Event timeStamp:" + this.timeStamp);
        System.out.println();
    }

    @Override
    public void simulateEvent(){
        System.out.println("Started PheroEvap event simulation:");

        colony.incrementEevents();
        //decrement pheromone level of adjacency s->d and d->s

        //if pheromone level becomes negative, set to zero
        colony.setPheroOfEdge(s, d, colony.getPheroOfEdge(s, d) - colony.getParam("ro"));
        if (colony.getPheroOfEdge(s, d) < 0) {
            colony.setPheroOfEdge(s, d, 0);
        }

        //find adjacency node with id==s in ACO.G.getAdjList(d) 
        colony.setPheroOfEdge(d, s, colony.getPheroOfEdge(d, s) - colony.getParam("ro"));
        if (colony.getPheroOfEdge(d, s) < 0) {
            colony.setPheroOfEdge(d, s, 0);
        }
        else 
        {
            pec.addEvPEC(new PheroEvap(s, d, timeStamp, colony, pec));          //only add one event beacuse adjacencies work in pairs, one event changes 2 adjacency nodes
        }

        System.out.println("Ended event simulation");
        System.out.println();
    }
}
