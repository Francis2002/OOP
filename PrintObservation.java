package prelim;

public class PrintObservation extends Event{
    public int num;

    public PrintObservation(int num, double timeStamp){
        this.timeStamp = timeStamp;
        this.num = num;
    }

    @Override
    public void simulateEvent(){
        System.out.println("Observation " + num + ":");
        System.out.println("\t Present instant:\t\t" + Simulator.currentTime);
        System.out.println("\t Number of move events:\t\t" + Simulator.mevents);
        System.out.println("\t Number of evaporation events:\t" + Simulator.eevents);
        System.out.println("\t Best Hamiltonian cycle:\t" + Simulator.bestPath + ":" + Simulator.bestPathWeight);
        System.out.println();
        Simulator.pec.addEvPEC(new PrintObservation(num + 1, (Simulator.simulationTime/20)*(num + 1)));
    }
}
