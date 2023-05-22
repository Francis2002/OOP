package prelim;

public class PheroEvap extends Event {
    int s;
    int d;

    public PheroEvap(int s, int d){
        System.out.println("Adding PheroEvap event:");
        this.s = s;
        this.d = d;
        this.timeStamp = Simulator.currentTime + Simulator.expRandom(miu);
        System.out.println("Ended event creation; Event timeStamp:" + this.timeStamp);
        System.out.println();
    }

    @Override
    public void simulateEvent(){
        System.out.println("Started PheroEvap event simulation:");

        Simulator.eevents += 1;
        //decrement pheromone level of adjacency s->d and d->s

        //if pheromone level becomes negative, set to zero
        Simulator.G.setPheroOfEdge(s, d, Simulator.G.getPheroOfEdge(s, d) - ro);
        if (Simulator.G.getPheroOfEdge(s, d) < 0) {
            Simulator.G.setPheroOfEdge(s, d, 0);
        }

        //find adjacency node with id==s in Simulator.G.getAdjList(d) 
        Simulator.G.setPheroOfEdge(d, s, Simulator.G.getPheroOfEdge(d, s) - ro);
        if (Simulator.G.getPheroOfEdge(d, s) < 0) {
            Simulator.G.setPheroOfEdge(d, s, 0);
        }
        else 
        {
            pec.addEvPEC(new PheroEvap(s, d));          //only add one event beacuse adjacencies work in pairs, one event changes 2 adjacency nodes
        }

        System.out.println("Ended event simulation");
        System.out.println();
    }
}
