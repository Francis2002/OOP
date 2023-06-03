package prelim.AntColony;

import java.util.*;
public class Ant {
    int id;
    List<Integer> J = new ArrayList<Integer>();
    ArrayList<Integer> path = new ArrayList<Integer>();

    public Ant(int id)
    {
        this.id = id;
        //adding to J set id of nodes ajacent to n1
        for (int i = 0; i < ACO.G.getNumberOfAdjacenciesOf(ACO.n1); i++) {
            J.add(ACO.G.getAdjacenciesOf(ACO.n1).get(i).getId());
        }
        this.path.add(ACO.n1);
    }

    public List<Integer> getJ(){
        return J;
    }

    public ArrayList<Integer> getPath(){
        return path;
    }

    public void updateJ(){
        //reset J
        J.removeAll(J);
        //add id of nodes adjacent to last node in path
        for (int i = 0; i < ACO.G.getNumberOfAdjacenciesOf(getLastInPath()); i++) {
            //only add nodes that are not in path
            if(!this.path.contains(ACO.G.getAdjacenciesOf(getLastInPath()).get(i).getId()))
            {
                J.add(ACO.G.getAdjacenciesOf(getLastInPath()).get(i).getId());
            }
            //if there is a chance of completing Hamiltonian cycle, then add n1 to possible targets if n1 is adjacent to last node in path
            if (ACO.G.getAdjacenciesOf(getLastInPath()).get(i).getId() == ACO.n1 && this.path.size() == ACO.G.getV()) 
            {
                J.add(ACO.n1);
            }
        }
    }

    public int getLastInPath(){
        return path.get(path.size()-1);
    }
}
