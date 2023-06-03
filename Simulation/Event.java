package prelim.Simulation;

public abstract class Event {

    protected PEC pec;

    protected double timeStamp;

    public abstract void simulateEvent(double currentTime);

    public double getTimeStamp() {
        return timeStamp;
    }
    
}

