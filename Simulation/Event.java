package prelim.Simulation;
/**
 * The Event class represents an abstract event in a simulation.
 * It provides a timestamp and a reference to the Pending Event Container (PEC) it belongs to.
 */
public abstract class Event implements Comparable<Event>{

    protected PEC pec;

    protected double timeStamp;
    /**
     * Simulates the event.
     */
    public abstract void simulateEvent();
     /**
     * Retrieves the timestamp of the event.
     * @return The timestamp.
     */
    public double getTimeStamp() {
        return timeStamp;
    }
    /**
     * Compares this event with another event based on their timestamps.
     * @param event The event to compare with.
     * @return -1 if this event's timestamp is less than the other event's timestamp,
     *         1 if this event's timestamp is greater than the other event's timestamp,
     *         0 if the timestamps are equal.
     */
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

