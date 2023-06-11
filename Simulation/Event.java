package prelim.Simulation;

public abstract class Event implements Comparable<Event>{

    protected PEC pec;

    protected double timeStamp;

    public abstract void simulateEvent();

    public double getTimeStamp() {
        return timeStamp;
    }

    @Override
    public int compareTo(Event event){
        if (this.getTimeStamp() < event.getTimeStamp()) {
            return -1;
        }
        if (this.getTimeStamp() > event.getTimeStamp()) {
            return 1;
        }
        return 0;
    }
    
}

