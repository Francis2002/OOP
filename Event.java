package prelim;

public abstract class Event {

    double timeStamp;

    PEC pec = Simulator.pec;

    public abstract void simulateEvent(double currentTime);
    
}

