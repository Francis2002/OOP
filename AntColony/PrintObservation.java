package prelim.AntColony;

import prelim.Simulation.*;

    /**
     * Represents an event for printing observation information.
    */
public class PrintObservation extends Event{
    private int num;
    private ACO colony;

    /**
     * Creates a new instance of the PrintObservation event.
     *
     * @param colony     The ACO (Ant Colony Optimization) instance.
     * @param num        The observation number.
     * @param timeStamp  The timestamp of the observation.
     */
    public PrintObservation(ACO colony, int num, double timeStamp){
        this.timeStamp = timeStamp;
        this.colony = colony;
        this.num = num;
    }
    /**
         * Simulates the PrintObservation event.
         */
    @Override
    public void simulateEvent(){
        System.out.println("Observation " + num + ":");
        System.out.println("\t Present instant:\t\t" + timeStamp);
        System.out.println("\t Number of move events:\t\t" + colony.getMevents());
        System.out.println("\t Number of evaporation events:\t" + colony.getEevents());
        System.out.println("\t Top candidate cycles:");
        for (int i = 0; i < (colony.getHamiltons().size() > 5 ? 5:colony.getHamiltons().size()); i++) {
            System.out.println("\t\t\t\t\t" + colony.getHamiltons().get(i) + ":" + colony.getHamiltonCosts().get(i));
        }
        System.out.println("\t Best Hamiltonian cycle:\n\t\t\t\t\t" + colony.getBestPath() + ":" + colony.getBestPathWeight());
        System.out.println();
    }
}
