package assets;

import assets.model.ConsoleMapPrinter;
import assets.model.MapConfig;
import assets.model.Simulation;
import assets.model.SimulationManager;

public class World {

    public static void main(String[] args) {
        System.out.println("Program started");

        MapConfig config = new MapConfig(15,
                                        15,
                                        0.2,
                                        5,
                                        3,
                                        2,
                                        8,
                                        8,
                                        5,
                                        5,
                                        3);
        SimulationManager simulationManager = new SimulationManager();
        ConsoleMapPrinter consoleMapPrinter = new ConsoleMapPrinter();

        Simulation sim1 = new Simulation(config, simulationManager);
        sim1.addObserver(consoleMapPrinter);
        simulationManager.addAndStartSimulation(sim1);

        Simulation sim2 = new Simulation(config, simulationManager);
        sim2.addObserver(consoleMapPrinter);
        simulationManager.addAndStartSimulation(sim2);

        System.out.println("Program finished");
    }

}
