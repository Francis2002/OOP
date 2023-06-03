package prelim.AntColony;

import prelim.Simulation.*;
public class PrintObservation extends Event{
    private int num;

    public PrintObservation(int num, double timeStamp){
        this.timeStamp = timeStamp;
        this.num = num;
    }

    @Override
    public void simulateEvent(double currentTime){
        System.out.println("Observation " + num + ":");
        System.out.println("\t Present instant:\t\t" + currentTime);
        System.out.println("\t Number of move events:\t\t" + ACO.getMevents());
        System.out.println("\t Number of evaporation events:\t" + ACO.getEevents());
        System.out.println("\t Top candidate cycles:");
        for (int i = 0; i < (ACO.hamiltons.size() > 5 ? 5:ACO.hamiltons.size()); i++) {
            System.out.println("\t\t\t\t\t" + ACO.hamiltons.get(i) + ":" + ACO.hamiltonCosts.get(i));
        }
        System.out.println("\t Best Hamiltonian cycle:\n\t\t\t\t\t" + ACO.bestPath + ":" + ACO.bestPathWeight);
        System.out.println();
    }
}
