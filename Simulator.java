package prelim;

import prelim.Simulation.*;
import prelim.Graphs.*;
import prelim.AntColony.*;

public class Simulator {

    private static double currentTime;
    private static Event currentEvent;

    private static PEC pec;

    private static Algorithm algorithm;


    public static void main(String[] args) throws Exception {

        pec = new PEC();

        algorithm = new ACO();

        algorithm.readInputs(args);

        ACO.setGraph(new ArrayListGraph(ACO.n));

        algorithm.init(pec);

        algorithm.printParameters();

        //find event with lowest timeStamp (calculated inside pec.nextEvPEC method)
        currentEvent = pec.nextEvPEC();
        currentTime = currentEvent.getTimeStamp();
        System.out.println("start:" + currentTime);

        //simulation cycle
        while(currentTime <= algorithm.getSimulationTime())
        {
            currentEvent.simulateEvent(currentTime);
            currentEvent = pec.nextEvPEC();
            currentTime = currentEvent.getTimeStamp();
        }
    }
    
}
