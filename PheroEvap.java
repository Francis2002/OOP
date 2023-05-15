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
        //decrement pheromone level of adjacency s->d and d->s

        //find adjacency node with id==d in nodeList.get(s) 
        Node adjNode = nodeList.get(s).get(0);      //default as first
        for (int k = 0; k < nodeList.size(); k++) {
            if (nodeList.get(s).get(k).id == d) {
                adjNode = nodeList.get(s).get(k);
                break;
            }
        }

        //if pheromone level becomes negative, set to zero
        adjNode.phero -= ro;
        if (adjNode.phero < 0) {
            adjNode.phero = 0;
        }

        //find adjacency node with id==s in nodeList.get(d) 
        adjNode = nodeList.get(d).get(0);      //default as first
        for (int k = 0; k < nodeList.size(); k++) {
            if (nodeList.get(d).get(k).id == s) {
                adjNode = nodeList.get(d).get(k);
                break;
            }
        }

        //if pheromone level becomes negative, set to zero
        adjNode.phero -= ro;
        if (adjNode.phero < 0) {
            adjNode.phero = 0;
        }
        else
        {
            pec.addEvPEC(new PheroEvap(s, d));          //only add one event beacuse adjacencies work in pairs, one event changes 2 adjacency nodes
        }

        System.out.println("Ended event simulation");
        System.out.println();
    }
}
