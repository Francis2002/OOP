package prelim.AntColony;

import prelim.Simulation.*;
import prelim.Graphs.*;

import java.util.*;
public class ACO implements Algorithm {

    private static double simulationTime;

    private static List<Ant> ants;

    static Graph G;

    private static Map<Edge, Double> phero;

    private static int mevents;
    private static int eevents;
    
    static List<Integer> bestPath = new ArrayList<Integer>();
    static ArrayList<ArrayList<Integer>> hamiltons = new ArrayList<ArrayList<Integer>>();
    static ArrayList<Integer> hamiltonCosts = new ArrayList<Integer>();
    static int bestPathWeight = 1000000;         //default as infinity

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
    static int n1;

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
            pec.addEvPEC(new AntMove(i, 0, pec));
        }
        for (int i = 0; i < 20; i++) {
            pec.addEvPEC(new PrintObservation(i + 1, (simulationTime/20)*(i + 1)));
        }
    }

    @Override
    public void printParameters(){
        System.out.println("Input parameters:");
        System.out.println("\t" + G.getV() + "\t: number of nodes in the graph");
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
            if (G.getAdjacenciesOf(s).get(j).getId() == d) {
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
            if (G.getAdjacenciesOf(s).get(j).getId() == d) {
                currentAdjNode = G.getAdjacenciesOf(s).get(j);
                break;
            }
        }
        
        phero.put(currentAdjNode, level);
    }

    public static void incrementMevents(){
        mevents+=1;
    }

    public static void incrementEevents(){
        eevents+=1;
    }

    public static int getMevents(){
        return mevents;
    }

    public static int getEevents(){
        return eevents;
    }

    public static void updateBestPath(List<Integer> path){
        bestPath.removeAll(bestPath);
        bestPath.addAll(path);
    }

    public static void addHamilton(ArrayList<Integer> path, int cost){
        System.out.println("Received Path:" + path);
        if(!duplicateHamilton(path)){
            System.out.println("Adding new hamilton");
            //needs to be done like this - if added directly, the reference is copied and not the content, meaning that a change to the ant path woukd change the hamiltons list
            ArrayList<Integer> toAdd = new ArrayList<Integer>();
            toAdd.addAll(path);
            System.out.println("hamiltonsCosts: " + hamiltonCosts.size());
            int index = binarySearch(hamiltonCosts, 0, hamiltonCosts.size() - 1, cost);
            System.out.println("index: " + index);
            if (index > 4) {
                return;
            }
            hamiltons.add(index, toAdd);
            hamiltonCosts.add(index, cost);
            if (hamiltons.size() > 5) {
                hamiltons.subList(5, hamiltons.size() - 1).clear();
                hamiltonCosts.subList(5, hamiltonCosts.size() - 1).clear();
            }
            System.out.println("New hamilton list: " + hamiltons);
        }
    }

    public static void updateBestPathWeight(int newWeight){
        bestPathWeight = newWeight;
    }

    public static int getBestPathWeight(){
        return bestPathWeight;
    }

    private static boolean duplicateHamilton(ArrayList<Integer> path){
        System.out.println("Path: " + path);
        System.out.println("Hamiltons: " + hamiltons);
        int flag = 0;
        //iterate over all hamiltons
        for (int i = 0; i < hamiltons.size(); i++) {
            System.out.println("Current Hamilton: " + hamiltons.get(i));
            flag = 0;
            //check if all elements match
            for (int j = 0; j < hamiltons.get(i).size(); j++) {
                if(!hamiltons.get(i).get(j).equals(path.get(j))){
                    //indicate that elements do not match
                    flag = 1;
                    break;
                }
            }
            //inner loop was not broken => path is duplicate
            if(flag == 0){
                return true;
            }
        }
        //inner loop never broken for all hamiltons => path not yet in list => not duplicate
        return false;
    }

    private static int binarySearch(List<Integer> arr, int l, int r, int x)
    {
        if (r>=l)
        {
            int mid = l + (r - l)/2;
  
            // If the element is present at the
            // middle itself
            if (arr.get(mid).equals(x))
               return mid;
  
            // If element is smaller than mid, then
            // it can only be present in left subarray
            if (arr.get(mid).intValue() > x)
               return binarySearch(arr, l, mid-1, x);
  
            // Else the element can only be present
            // in right subarray
            return binarySearch(arr, mid+1, r, x);
        }
  
        // We reach here when final index is discovered
        // return left value, becuase right has already crossed left
        return l;
    }
}
