package prelim;

import java.util.*;

public class Ant {
    int id;
    List<Integer> J = new ArrayList<Integer>();
    List<Integer> path = new ArrayList<Integer>();

    public Ant(int id)
    {
        this.id = id;
        //adding to J set id of nodes ajacent to n1
        for (int i = 0; i < ACO.G.getNumberOfAdjacenciesOf(ACO.n1); i++) {
            J.add(ACO.G.getAdjacenciesOf(ACO.n1).get(i).id);
        }
        this.path.add(ACO.n1);
    }

    public List<Integer> getJ(){
        return J;
    }

    public List<Integer> getPath(){
        return path;
    }

    public void updateJ(){
        //reset J
        J.removeAll(J);
        //add id of nodes adjacent to last node in path
        for (int i = 0; i < ACO.G.getNumberOfAdjacenciesOf(path.get(path.size()-1)); i++) {
            //only add nodes that are not in path
            if(!this.path.contains(ACO.G.getAdjacenciesOf(path.get(path.size()-1)).get(i).id))
            {
                J.add(ACO.G.getAdjacenciesOf(path.get(path.size()-1)).get(i).id);
            }
            //if there is a chance of completing Hamiltonian cycle, then add n1 to possible targets if n1 is adjacent to last node in path
            if (ACO.G.getAdjacenciesOf(path.get(path.size()-1)).get(i).id == ACO.n1 && this.path.size() == ACO.G.getV()) 
            {
                J.add(ACO.n1);
            }
        }
    }
}
