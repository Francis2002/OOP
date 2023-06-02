package prelim;

import java.util.*;
public class ACO extends Algorithm {

    public static List<Ant> ants;

    public static Graph G;

    private static Map<Edge, Double> phero;

    public static int mevents;
    public static int eevents;
    
    public static List<Integer> bestPath = new ArrayList<Integer>();
    public static int bestPathWeight = 1000000;         //default as infinity

    public static int n;
    public static int a;
    public static double alfa;
    public static double beta;
    public static double gama;
    public static double ro;
    public static double miu;
    public static double delta;
    public static int niu;

    //define nest
    protected static int n1;

    static Random random = new Random();

    public static double expRandom(double m)
    {
        double next = random.nextDouble();
        return -m*Math.log(1.0 - next);
    }

    public ACO(){
        ants = new ArrayList<Ant>();
        phero = new HashMap<Edge, Double>();
    }

    public static Ant getAnt(int index){
        return ants.get(index);
    }

    @Override
    public void init(PEC pec){

        //initialize graph

        G.fillWithRandomAdjacencies(a);

        G.validateWeights();

        for (int i = 0; i < G.getV(); i++) {
            for (int j = 0; j < G.getNumberOfAdjacenciesOf(i); j++) {
                System.out.println(G.getAdjacenciesOf(i).get(j) + "i: " + i + "j: " + j);
                phero.put(G.getAdjacenciesOf(i).get(j), 0.0);
            }
        }

        //add ants and AntMove events starting on node n1
        for (int i = 0; i < niu; i++) {
            ants.add(new Ant(i));
            pec.addEvPEC(new AntMove(i, 0));
        }
        for (int i = 0; i < 20; i++) {
            pec.addEvPEC(new PrintObservation(i + 1, (simulationTime/20)*(i + 1)));
        }
    }

    @Override
    public void printParameters(){
        System.out.println("Input parameters:");
        System.out.println("\t" + G.V + "\t: number of nodes in the graph");
        System.out.println("\t" + n1 + "\t: the nest node");
        System.out.println("\t" + alfa + "\t: alpha, ant move event");
        System.out.println("\t" + beta + "\t: beta, ant move event");
        System.out.println("\t" + delta + "\t: delta, ant move event");
        System.out.println("\t" + miu + "\t: eta, pheromone evaporation event");
        System.out.println("\t" + ro + "\t: rho, pheromone evaporation event");
        System.out.println("\t" + gama + "\t: pheromone level");
        System.out.println("\t" + niu + "\t: ant colony size");
        System.out.println("\t" + simulationTime + "\t: final instant");
        System.out.println();
        
        G.printGraph();
    }

    @Override
    public void readInputs(String[] args){
        if (args.length < 1) {
            System.out.println("Error: Too few input arguments");
            System.exit(1);
        }
        if(!args[0].equals("-r") && !args[0].equals("-f")){
            System.out.println("Error: Invalid invocation -- use -r or -f, run with -h for help");
            System.exit(1);
        }
        if (args[0].equals("-r")) {
            if (args.length != 12) {
                System.out.println("Error: Invalid invocation -- -r requires 11 parameters, run with -h for help");
                System.exit(1);
            }
            try{
                for (int i = 1; i < args.length; i++) {
                    if (Double.parseDouble(args[i]) <= 0 && i != 3) {
                        throw new NumberFormatException();
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid invocation -- all input parameters (except nest/n1) should be non-zero positive numbers, run with -h for help");
                System.exit(1);
            }
            n = Integer.parseInt(args[1]);
            a = Integer.parseInt(args[2]);
            n1 = Integer.parseInt(args[3]);
            alfa = Double.parseDouble(args[4]);
            beta = Double.parseDouble(args[5]);
            delta = Double.parseDouble(args[6]);
            miu = Double.parseDouble(args[7]);
            ro = Double.parseDouble(args[8]);
            gama = Double.parseDouble(args[9]);
            niu = Integer.parseInt(args[10]);
            simulationTime = Double.parseDouble(args[11]);
        }
        else if(args[0].equals("-f")){
            System.out.println("TODO: Read from file");
            System.exit(1);
        }
    }

    @Override
    public double getSimulationTime(){
        return simulationTime;
    }

    public static void setGraph(Graph graph){
        G = graph;
    }

    public static double getPheroOfEdge(int s, int d){
        Edge currentAdjNode = G.getAdjacenciesOf(s).get(0); //default as first

        //find adjacency node with id==J.get(i) in G.getAdjacenciesOf(ant.getPath().get(ant.getPath().size()-1)) 
        for (int j = 0; j < G.getNumberOfAdjacenciesOf(s); j++) {
            if (G.getAdjacenciesOf(s).get(j).id == d) {
                currentAdjNode = G.getAdjacenciesOf(s).get(j);
                break;
            }
        }
        return phero.get(currentAdjNode);
    }

    public static void setPheroOfEdge(int s, int d, double level){
        Edge currentAdjNode = G.getAdjacenciesOf(s).get(0); //default as first

        //find adjacency node with id==J.get(i) in G.getAdjacenciesOf(ant.getPath().get(ant.getPath().size()-1)) 
        for (int j = 0; j < G.getNumberOfAdjacenciesOf(s); j++) {
            if (G.getAdjacenciesOf(s).get(j).id == d) {
                currentAdjNode = G.getAdjacenciesOf(s).get(j);
                break;
            }
        }
        
        phero.put(currentAdjNode, level);
    }
}
