package assets;

import assets.model.MapConfig;
import assets.model.Simulation;
import assets.model.WorldMap;

public class World {

    public static void main(String[] args) {
        System.out.println("Program started");

        MapConfig config = new MapConfig(50,
                                        50,
                                        60,
                                        10,
                                        20,
                                        20,
                                        8);
        WorldMap map = new WorldMap(config.mapHeight(), config.mapWidth());
        Simulation simulation = new Simulation(map, config);
        simulation.run();
        
        System.out.println("Program finished");
    }

}
