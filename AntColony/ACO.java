package prelim.AntColony;

import prelim.Simulation.*;
import prelim.Graphs.*;

import java.util.*;
import java.io.*;
/**
 * This class implements an Ant Colony Optimization algorithm to solve the Travelling Salesman Problem.
 * This class implements the Algorithm interface found in the Simulator package.
 */
public class ACO implements Algorithm {

    private double simulationTime;

    private List<Ant> ants;

    private static Graph G;

    private Map<Edge, Double> phero;

    private int mevents;
    private int eevents;
    
    private List<Integer> bestPath = new ArrayList<Integer>();
    private ArrayList<ArrayList<Integer>> hamiltons = new ArrayList<ArrayList<Integer>>();
    private ArrayList<Integer> hamiltonCosts = new ArrayList<Integer>();
    private int bestPathWeight = 100000000;         //default as infinity

    private Map<String, Double> params;

    Random random = new Random();

    public double expRandom(double m)
    {
        double next = random.nextDouble();
        return -m*Math.log(1.0 - next);
    }

    public ACO(){
        ants = new ArrayList<Ant>();
        phero = new HashMap<Edge, Double>();
        params = new HashMap<String, Double>();
    }

    public Ant getAnt(int index){
        return ants.get(index);
    }

    @Override
    public void init(PEC pec){

        for (int i = 0; i < G.getV(); i++) {
            for (int j = 0; j < G.getNumberOfAdjacenciesOf(i); j++) {
                System.out.println(G.getAdjacenciesOf(i).get(j) + "i: " + i + "j: " + j);
                phero.put(G.getAdjacenciesOf(i).get(j), 0.0);
            }
        }

        //add ants and AntMove events starting on node n1
        for (int i = 0; i < getParam("niu"); i++) {
            ants.add(new Ant(i, G, this));
            pec.addEvPEC(new AntMove(ants.get(i), i, 0, pec));
        }
        for (int i = 0; i < 20; i++) {
            pec.addEvPEC(new PrintObservation(this, i + 1, (simulationTime/20)*(i + 1)));
        }
    }

    @Override
    public void printParameters(){
        System.out.println("Input parameters:");
        System.out.println("\t" + G.getV() + "\t: number of nodes in the graph");
        System.out.println("\t" + getParam("n1") + "\t: the nest node");
        System.out.println("\t" + getParam("alfa") + "\t: alpha, ant move event");
        System.out.println("\t" + getParam("beta") + "\t: beta, ant move event");
        System.out.println("\t" + getParam("delta") + "\t: delta, ant move event");
        System.out.println("\t" + getParam("miu") + "\t: eta, pheromone evaporation event");
        System.out.println("\t" + getParam("ro") + "\t: rho, pheromone evaporation event");
        System.out.println("\t" + getParam("gama") + "\t: pheromone level");
        System.out.println("\t" + getParam("niu") + "\t: ant colony size");
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
        if(!args[0].equals("-r") && !args[0].equals("-f") && !args[0].equals("-h")){
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
            params.put("invocation", 0.0);
            params.put("n", Double.parseDouble(args[1]));
            params.put("a", Double.parseDouble(args[2]));
            params.put("n1", Double.parseDouble(args[3]));
            params.put("alfa", Double.parseDouble(args[4]));
            params.put("beta", Double.parseDouble(args[5]));
            params.put("delta", Double.parseDouble(args[6]));
            params.put("miu", Double.parseDouble(args[7]));
            params.put("ro", Double.parseDouble(args[8]));
            params.put("gama", Double.parseDouble(args[9]));
            params.put("niu", Double.parseDouble(args[10]));
            simulationTime = Double.parseDouble(args[11]);

            G.setV(Integer.parseInt(args[1]));

            G.fillWithRandomAdjacencies(Integer.parseInt(args[2]));
        }
        else if(args[0].equals("-f")){
            System.out.println("TODO: Read from file");
            String filePath = args[1]; 
            System.out.println(filePath);
        
            try {
                BufferedReader reader = new BufferedReader(new FileReader(filePath));
                
                String firstLine = reader.readLine();
                String[] specificArrayValues = firstLine.split(" ");
                int matrixSize = Integer.parseInt(specificArrayValues[0]);
                double n_param = Double.parseDouble(specificArrayValues[0]);
                double[] specificArray = new double[specificArrayValues.length - 1];
                
                for (int i = 1; i < specificArrayValues.length; i++) {
                    specificArray[i - 1] = Double.parseDouble(specificArrayValues[i]);
                }
                
                double[][] matrix = new double[matrixSize][matrixSize];
                
                String line;
                
                for (int i = 0; i < matrixSize; i++) {
                    line = reader.readLine();
                    String[] rowValues = line.split(" ");
                    
                    for (int j = 0; j < matrixSize; j++) {
                        matrix[i][j] = Double.parseDouble(rowValues[j]);
                    }
                }
                G.setV(matrixSize);
                for (int i = 0; i < matrixSize; i++) {
                    for (int j = i; j < matrixSize; j++) {
                        if (matrix[i][j] > 0.0) {
                            G.addEdge(i,j,(int)matrix[i][j]);
                        }
                    }
                }
                params.put("invocation", 1.0);
                params.put("n", n_param);
                params.put("n1", specificArray[0]);
                params.put("alfa", specificArray[1]);
                params.put("beta", specificArray[2]);
                params.put("delta", specificArray[3]);
                params.put("miu", specificArray[4]);
                params.put("ro", specificArray[5]);
                params.put("gama", specificArray[6]);
                params.put("niu", specificArray[7]);
                simulationTime = specificArray[8];
                
                System.out.println("Specific Array: " + Arrays.toString(specificArray));
                
                System.out.println("Matrix:");
                for (double[] row : matrix) {
                    System.out.println(Arrays.toString(row));
                }
                
                reader.close();
                G.printGraph();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (args[0].equals("-h")) {
            System.out.println("System invocation:");
            System.out.println("java -jar project.jar -r n a n1 alfa beta delta eta ro gama niu tau");
            System.out.println("\tn: number of nodes in the graph");
            System.out.println("\ta: maximum edge weight");
            System.out.println("\tn1: the nest node");
            System.out.println("\talpha: parameter concerning ant move event");
            System.out.println("\tbeta: parameter concerning ant move event");
            System.out.println("\tdelta: ant move time stamp exponential distribution mean");
            System.out.println("\teta, pheromone evaporation time stamp exponential distribution mean");
            System.out.println("\trho, pheromone evaporation level decrement");
            System.out.println("\tgama: parameter concerning pheromone evaporation event");
            System.out.println("\tniu: ant colony size");
            System.out.println("\ttau: final instant");
            System.out.println("java -jar project.jar -f <path-to-file>/filename.txt");
            System.out.println("\tfilename.txt: text file with specified parameters and graph");
            System.exit(1);
        }

        G.validateWeights();
    }

    @Override
    public double getSimulationTime(){
        return simulationTime;
    }

    public static void setGraph(Graph graph){
        G = graph;
    }

    public double getPheroOfEdge(int s, int d){
        Edge currentAdjNode = G.getAdjacenciesOf(s).get(0); //default as first

        for (Edge edge : G.getAdjacenciesOf(s)) {
            if (edge.getId() == d) {
                currentAdjNode = edge;
                break;
            }
        }
        return phero.get(currentAdjNode);
    }

    public void setPheroOfEdge(int s, int d, double level){
        Edge currentAdjNode = G.getAdjacenciesOf(s).get(0); //default as first

        for (Edge edge : G.getAdjacenciesOf(s)) {
            if (edge.getId() == d) {
                currentAdjNode = edge;
                break;
            }
        }
        
        phero.put(currentAdjNode, level);
    }

    public void incrementMevents(){
        mevents+=1;
    }

    public void incrementEevents(){
        eevents+=1;
    }

    public int getMevents(){
        return mevents;
    }

    public int getEevents(){
        return eevents;
    }

    public void updateBestPath(List<Integer> path){
        bestPath.removeAll(bestPath);
        bestPath.addAll(path);
    }

    public void addHamilton(ArrayList<Integer> path, int cost){
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

    public void updateBestPathWeight(int newWeight){
        bestPathWeight = newWeight;
    }

    public int getBestPathWeight(){
        return bestPathWeight;
    }

    public ArrayList<ArrayList<Integer>> getHamiltons(){
        return hamiltons;
    }

    public ArrayList<Integer> getHamiltonCosts(){
        return hamiltonCosts;
    }

    public List<Integer> getBestPath(){
        return bestPath;
    }

    private boolean duplicateHamilton(ArrayList<Integer> path){
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

    public double getParam(String param){
        return params.get(param);
    }

    private int binarySearch(List<Integer> arr, int l, int r, int x)
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
