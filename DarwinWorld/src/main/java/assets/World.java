package assets;

import assets.model.records.MapSettings;
import assets.model.enums.MapType;
import assets.model.util.ConsoleMapPrinter;
import assets.model.records.SimulationConfig;

import javafx.application.Application;
import assets.model.application.ConfigurationApp;

public class World {

    public static void main(String[] args) {
        System.out.println("Program started");

        Application.launch(ConfigurationApp.class, args);
        /*
        MapSettings settings = new MapSettings(15,
                                                15,
                                                MapType.WATER,
                                                0.2
        );

        SimulationConfig config = new SimulationConfig(settings,
                                                        5,
                                                        3,
                                                        2,
                                                        8,
                                                        8,
                                                        5,
                                                        5,
                                                        3
        );

        SimulationManager simulationManager = new SimulationManager();
        ConsoleMapPrinter cmp = new ConsoleMapPrinter();

        Simulation sim1 = new Simulation(config, simulationManager, cmp);
        simulationManager.addAndStartSimulation(sim1);

        Simulation sim2 = new Simulation(config, simulationManager, cmp);
        simulationManager.addAndStartSimulation(sim2);

        System.out.println("Program finished");

        */
    }
}
