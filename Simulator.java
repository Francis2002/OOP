package prelim;

import java.util.*;

public class Simulator {
    public static int n;
    public static int a;
    public static double simulationTime;
    public static double alfa;
    public static double beta;
    public static double gama;
    public static double ro;
    public static double miu;
    public static double delta;
    public static int niu;

    //define nest
    public static int n1;

    public static double currentTime;
    public static Event currentEvent;

    public static int mevents;
    public static int eevents;

    public static PEC pec;

    public static Algorithm algorithm;

    public static Graph G;

    public static List<Integer> bestPath = new ArrayList<Integer>();
    public static int bestPathWeight = 1000000;         //default as infinity

    static Random random = new Random();

    public static double expRandom(double m)
    {
        double next = random.nextDouble();
        return -m*Math.log(1.0 - next);
    }

    public static void readInputs(String[] args){
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

    public static void main(String[] args) throws Exception {

        readInputs(args);

        pec = new PEC();

        G = new ArrayListGraph(n);

        G.fillWithRandomAdjacencies(a);

        G.validateWeights();

        algorithm = new ACO();

        algorithm.init();

        algorithm.printParameters();

        G.printGraph();

        //find event with lowest timeStamp (calculated inside pec.nextEvPEC method)
        currentEvent = pec.nextEvPEC();
        currentTime = currentEvent.timeStamp;
        System.out.println("start:" + currentTime);

        //simulation cycle
        while(currentTime <= simulationTime)
        {
            currentEvent.simulateEvent();
            currentEvent = pec.nextEvPEC();
            System.out.println("CurrentTime:" + currentTime + "; bestPathWeight:" + bestPathWeight + "; bestPath:" + bestPath + "\n");
            currentTime = currentEvent.timeStamp;
        }

        G.printGraph();
    }
}
