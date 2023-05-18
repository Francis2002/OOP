package prelim;

import java.util.*;
public abstract class Event {

    double alfa = Simulator.alfa;
    double beta = Simulator.beta;
    double gama = Simulator.gama;
    double ro = Simulator.ro;
    double miu = Simulator.miu;
    double delta = Simulator.delta;
    double timeStamp;

    PEC pec = Simulator.pec;

    //reference to adjacency list to avoid writing Simulator.G.adj, which difficults understaing of code
    ArrayList<ArrayList<Node>> nodeList = Simulator.G.adj;

    public abstract void simulateEvent();
    
}

