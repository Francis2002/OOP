package prelim;

public abstract class Algorithm {

    public static double simulationTime;

    public abstract void init(PEC pec);

    public abstract void printParameters();

    public abstract void readInputs(String[] args);

    public abstract double getSimulationTime();
}
