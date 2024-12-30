package assets.model;

import java.util.ArrayList;

public class SimulationManager {
    ArrayList<Simulation> simulations = new ArrayList<>(0);
    ArrayList<Thread> simulationThreads = new ArrayList<>(0);;
    int maxSimulationsAtOnce = 8;

    // returns the thread that the simulation is running on
    public Thread addNewSimualtion(Simulation newSimulation){
        simulations.add(newSimulation);
        Thread thread = new Thread(newSimulation);
        simulationThreads.add(thread);
        return thread;
    }

    public Thread addAndStartSimulation(Simulation newSimulation){
        Thread thread = addNewSimualtion(newSimulation);
        thread.start();
        return thread;
    }

    // activates all inactive simulations
    public void startSimulations(){
        for(Thread thread : simulationThreads){
            if(!thread.isAlive()){
                thread.start();
            }
        }
    }

    // calls for all simulations to end
    public void closeAllSimulations(){
        for (Thread simthread : simulationThreads) {
            simthread.interrupt();
        }
    }

    // remove simulation by ID
    public void removeSimulation(Simulation simulation){
        int id = simulations.indexOf(simulation);
        simulations.remove(id);
        simulationThreads.get(id).interrupt();
        simulationThreads.remove(id);
    }
}
