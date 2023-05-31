package prelim;

public class PrintObservation extends Event{
    public int num;

    public PrintObservation(int num, double timeStamp){
        this.timeStamp = timeStamp;
        this.num = num;
    }

    @Override
    public void simulateEvent(double currentTime){
        System.out.println("Observation " + num + ":");
        System.out.println("\t Present instant:\t\t" + currentTime);
        System.out.println("\t Number of move events:\t\t" + ACO.mevents);
        System.out.println("\t Number of evaporation events:\t" + ACO.eevents);
        System.out.println("\t Best Hamiltonian cycle:\t" + ACO.bestPath + ":" + ACO.bestPathWeight);
        System.out.println();
    }
}
