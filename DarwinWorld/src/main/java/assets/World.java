package assets;

import assets.model.Simulation;
import assets.model.WorldMap;

public class World {

    public static void main(String[] args) {
        System.out.println("Program started");

        int height = 80;
        int width = 80;
        int numOfGrass = 60;
        int numOfAnimals = 20;
        WorldMap map = new WorldMap(height, width);
        Simulation simulation = new Simulation(map, numOfAnimals, numOfGrass);
        simulation.run();
        
        System.out.println("Program finished");
    }

}
