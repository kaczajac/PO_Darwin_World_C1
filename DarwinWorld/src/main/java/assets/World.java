package assets;

import assets.model.MapConfig;
import assets.model.Simulation;

public class World {

    public static void main(String[] args) {
        System.out.println("Program started");

        MapConfig config = new MapConfig(20,
                                        20,
                                        8,
                                        10,
                                        8,
                                        10,
                                        8);
        Simulation simulation = new Simulation(config);
        simulation.run();
        
        System.out.println("Program finished");
    }

}
