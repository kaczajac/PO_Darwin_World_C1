package assets;

import assets.model.MapConfig;
import assets.model.Simulation;

public class World {

    public static void main(String[] args) {
        System.out.println("Program started");

        MapConfig config = new MapConfig(15,
                                        15,
                                        0.2,
                                        3,
                                        2,
                                        8,
                                        8,
                                        5,
                                        5,
                                        3);
        Simulation simulation = new Simulation(config);
        simulation.run();

        System.out.println("Program finished");
    }

}
