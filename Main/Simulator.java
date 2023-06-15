package prelim.Main;

import prelim.Simulation.*;
import prelim.Graphs.*;
import prelim.AntColony.*;

public class Simulator {

    private static double currentTime;
    private static Event currentEvent;

    private static PEC pec;

    private static Algorithm algorithm;

    public static void main(String[] args) throws Exception {

        pec = PEC.getInstance();

        algorithm = new ACO();

        //set graph strategy. Not all algorithms have graphs, so the method setGraph is not in the algorithm interface and thus we must access it in a static way.
        //since ACO is a Singleton, there are no problems doing this
        ACO.setGraph(new ArrayListGraph());        

        algorithm.readInputs(args);

        //put initial events in the pec and initialize variables
        algorithm.init(pec);

        algorithm.printParameters();

        //find event with lowest timeStamp (calculated inside pec.nextEvPEC method)
        currentEvent = pec.nextEvPEC();
        currentTime = currentEvent.getTimeStamp();
        System.out.println("start:" + currentTime);

        //simulation cycle
        while(currentTime <= algorithm.getSimulationTime())
        {
            currentEvent.simulateEvent();
            currentEvent = pec.nextEvPEC();
            currentTime = currentEvent.getTimeStamp();
        }
    }
    
}
