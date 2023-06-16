package prelim.Simulation;

public interface Algorithm {

    /**
     * Initializes the pec with the first events and the algorithm attributes
     * @param pec
     */
    public abstract void init(PEC pec);

    /**
     * Prints the parametrs received from the command line
     */
    public abstract void printParameters();

    /**
     * Retrieves the algorithm parameters from the command line
     * @param args
     */
    public abstract void readInputs(String[] args);

    /**
     * 
     * @return algorithm total simulation time
     */
    public abstract double getSimulationTime();
}
