package prelim;

import java.util.*;

public class Simulator {
    public static double simulationTime = 30;
    public static double alfa = 1;
    public static double beta = 1;
    public static double gama = 1;
    public static double ro = 1;
    public static double miu = 1;
    public static double delta = 1;

    //define nest
    public static int n1 = 0;

    public static double currentTime;
    public static Event currentEvent;

    public static PEC pec;

    public static List<Ant> ants;

    public static Graph G;

    public static List<Integer> bestPath = new ArrayList<Integer>();
    public static int bestPathWeight = 1000000;         //default as infinity

    static Random random = new Random();

    public static double expRandom(double m)
    {
        double next = random.nextDouble();
        return -m*Math.log(1.0 - next);
    }

    public static void main(String[] args) throws Exception {


        G = new Graph();

        //head of list of adjacencies of a node with id i will be on index i of G.adj
        for (int i = 0; i < G.V; i++)
        G.adj.add(new ArrayList<Node>());

        // Add edges
        G.addEdge(0, 1, 5, 0.0);
        G.addEdge(0, 2, 3, 0.0);
        G.addEdge(0, 3, 20, 0.0);
        G.addEdge(1, 2, 3, 0.0);
        G.addEdge(1, 3, 2, 0.0);
        G.addEdge(2, 3, 5, 0.0);

        G.printGraph();

        ants = new ArrayList<Ant>();
        pec = new PEC();

        //add ants and AntMove events starting on node n1
        for (int i = 0; i < 4; i++) {
            ants.add(new Ant(i));
            pec.addEvPEC(new AntMove(i));
        }

        //find AntMove event with lowest timeStamp (calculated inside pec.nextEvPEC method)
        currentEvent = pec.nextEvPEC();
        currentTime = currentEvent.timeStamp;
        System.out.println("start:" + currentTime);

        //simulation cycle
        while(currentTime < simulationTime)
        {
            currentEvent.simulateEvent();
            currentEvent = pec.nextEvPEC();
            currentTime = currentEvent.timeStamp;
            System.out.println("CurrentTime:" + currentTime + "; bestPathWeight:" + bestPathWeight + "; bestPath:" + bestPath + "\n");
        }
    }
}
