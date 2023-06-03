package prelim.Simulation;

public abstract class Algorithm {

    protected static double simulationTime;

    public abstract void init(PEC pec);

    public abstract void printParameters();

    public abstract void readInputs(String[] args);

    public abstract double getSimulationTime();
}
