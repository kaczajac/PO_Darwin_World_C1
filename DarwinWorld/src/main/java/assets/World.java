package assets;

import assets.model.Animal;
import assets.model.Vector2d;
import assets.model.WorldMap;

public class World {

    public static void main(String[] args) {
        System.out.println("Program started");

        int height = 80;
        int width = 80;
        int numOfGrass = 60;
        int numOfAnimals = 20;
        WorldMap map = new WorldMap(height, width, numOfGrass, numOfAnimals);

        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                Vector2d position = new Vector2d(r, c);
                if (map.isOccupied(position)) {
                    System.out.print("A "); // znak animala
                }
                else if (map.grassAt(position)) {
                    System.out.print("* "); // znak krzaka
                }
                else {
                    switch (map.getTile(position).getType()) {
                        case PLAINS -> System.out.print(". "); // znak normalnej ziemi
                        case FOREST -> System.out.print("; "); // znak zalesionego równika
                        case WATER -> System.out.print("  "); // znak wody
                    }
                }
            }
            System.out.println();
        }
        
        System.out.println("Program finished");
    }

}
