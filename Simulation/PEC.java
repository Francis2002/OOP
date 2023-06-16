package prelim.Simulation;

import java.util.*;
/**
 * The PEC (Pending Event Container) class represents a container for events in a simulation.
 * It maintains a sorted list of events based on their priority.
 */
public class PEC {

    private static PEC instance;

    private PEC(){

    }
    /**
     * Verifies if the PEC is already created, and if not it instantiates it 
     */
    public static PEC getInstance(){
        if (instance == null) {
            instance = new PEC();
        }
        return instance;
    }

    private List<Event> evArr = new ArrayList<Event>();
    /**
     * Adds an event to the PEC in the correct position based on its priority.
     * @param ev The event to be added.
     */
    public void addEvPEC(Event ev){
        //find correct place of new event on the already ordered evArr using binarySearch
        int index = binarySearch(evArr, 0, evArr.size() - 1, ev);
        evArr.add(index, ev);
    }
     /**
     * Retrieves and removes the next event from the PEC.
     * @return The next event.
     */
    public Event nextEvPEC(){
        return evArr.remove(0);
    }

    private int binarySearch(List<Event> arr, int l, int r, Event x)
    {
        if (r>=l)
        {
            int mid = l + (r - l)/2;
  
            // If the element is present at the
            // middle itself
            if (arr.get(mid).compareTo(x) == 0)
               return mid;
  
            // If element is smaller than mid, then
            // it can only be present in left subarray
            if (arr.get(mid).compareTo(x) > 0)
               return binarySearch(arr, l, mid-1, x);
  
            // Else the element can only be present
            // in right subarray
            return binarySearch(arr, mid+1, r, x);
        }
  
        // We reach here when final index is discovered
        // return left value, becuase right has already crossed left
        return l;
    }
}
