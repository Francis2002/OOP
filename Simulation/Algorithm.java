package prelim.Simulation;

public interface Algorithm {

    public abstract void init(PEC pec);

    public abstract void printParameters();

    public abstract void readInputs(String[] args);

    public abstract double getSimulationTime();
}
