package prelim;

public abstract class Event {

    double alfa = Simulator.alfa;
    double beta = Simulator.beta;
    double gama = Simulator.gama;
    double ro = Simulator.ro;
    double miu = Simulator.miu;
    double delta = Simulator.delta;
    double timeStamp;

    PEC pec = Simulator.pec;

    public abstract void simulateEvent();
    
}

