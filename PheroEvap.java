package prelim;

public class PheroEvap extends Event {
    int s;
    int d;

    public PheroEvap(int s, int d, double currentTime){
        System.out.println("Adding PheroEvap event:");
        this.s = s;
        this.d = d;
        this.timeStamp = currentTime + ACO.expRandom(ACO.miu);
        System.out.println("Ended event creation; Event timeStamp:" + this.timeStamp);
        System.out.println();
    }

    @Override
    public void simulateEvent(double currentTime){
        System.out.println("Started PheroEvap event simulation:");

        ACO.eevents += 1;
        //decrement pheromone level of adjacency s->d and d->s

        //if pheromone level becomes negative, set to zero
        ACO.G.setPheroOfEdge(s, d, ACO.G.getPheroOfEdge(s, d) - ACO.ro);
        if (ACO.G.getPheroOfEdge(s, d) < 0) {
            ACO.G.setPheroOfEdge(s, d, 0);
        }

        //find adjacency node with id==s in ACO.G.getAdjList(d) 
        ACO.G.setPheroOfEdge(d, s, ACO.G.getPheroOfEdge(d, s) - ACO.ro);
        if (ACO.G.getPheroOfEdge(d, s) < 0) {
            ACO.G.setPheroOfEdge(d, s, 0);
        }
        else 
        {
            pec.addEvPEC(new PheroEvap(s, d, currentTime));          //only add one event beacuse adjacencies work in pairs, one event changes 2 adjacency nodes
        }

        System.out.println("Ended event simulation");
        System.out.println();
    }
}
