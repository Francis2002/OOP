package prelim.Simulation;

import java.util.*;

public class PEC {

    private static List<Event> evArr = new ArrayList<Event>();

    public void addEvPEC(Event ev){
        //find correct place of new event on the already ordered evArr using binarySearch
        int index = binarySearch(evArr, 0, evArr.size() - 1, ev);
        evArr.add(index, ev);
    }

    public Event nextEvPEC(){
        return evArr.remove(0);
    }

    int binarySearch(List<Event> arr, int l, int r, Event x)
    {
        if (r>=l)
        {
            int mid = l + (r - l)/2;
  
            // If the element is present at the
            // middle itself
            if (arr.get(mid).timeStamp == x.timeStamp)
               return mid;
  
            // If element is smaller than mid, then
            // it can only be present in left subarray
            if (arr.get(mid).timeStamp > x.timeStamp)
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
