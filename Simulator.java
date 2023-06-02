package prelim;


public class Simulator {

    public static double currentTime;
    public static Event currentEvent;

    public static PEC pec;

    public static Algorithm algorithm;


    public static void main(String[] args) throws Exception {

        pec = new PEC();

        algorithm = new ACO();

        algorithm.readInputs(args);

        ACO.setGraph(new ArrayListGraph(ACO.n));

        algorithm.init(pec);

        algorithm.printParameters();

        //find event with lowest timeStamp (calculated inside pec.nextEvPEC method)
        currentEvent = pec.nextEvPEC();
        currentTime = currentEvent.timeStamp;
        System.out.println("start:" + currentTime);

        //simulation cycle
        while(currentTime <= algorithm.getSimulationTime())
        {
            currentEvent.simulateEvent(currentTime);
            currentEvent = pec.nextEvPEC();
            currentTime = currentEvent.timeStamp;
        }
    }
    
}
