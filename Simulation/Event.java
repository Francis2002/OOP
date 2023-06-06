package prelim.Simulation;

public abstract class Event {

    protected PEC pec;

    protected double timeStamp;

    public abstract void simulateEvent();

    public double getTimeStamp() {
        return timeStamp;
    }
    
}

