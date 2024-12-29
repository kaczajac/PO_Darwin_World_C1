package assets.model;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class WorldMap {

    private final UUID id;
    private final int width;
    private final int height;
    private final Tile[][] tiles;

    private final Map<Vector2d, List<Animal>> animals = new ConcurrentHashMap<>();
    private final Map<Vector2d, Grass> grasses = new HashMap<>();
    private final List<Vector2d> flowTiles = new ArrayList<>();

    public WorldMap(int height, int width, double waterLevel) {
        this.id = UUID.randomUUID();
        this.width = width;
        this.height = height;

        TileGenerator generator = new TileGenerator(height, width, waterLevel);
        this.tiles = generator.getTiles();

        findFlowTiles();
    }

//// Simulation functions

    public void placeAnimals(MapConfig config) {

        RandomPositionGenerator generator = new RandomPositionGenerator(this, config.animalStartNumber(), Animal.class);
        for (Vector2d position : generator) {
            Animal animal = new Animal(position, config.animalStartEnergy(), config.animalGenomeLength());
            place(animal);
        }

    }

    public void placeGrasses(int numOfGrass) {

        RandomPositionGenerator generator = new RandomPositionGenerator(this, numOfGrass, Grass.class);
        for (Vector2d position : generator) {
            Grass grass = new Grass(position);
            place(grass);
        }

    }

    public void place(Animal animal) {
        Vector2d position = animal.getPosition();
        if (inBounds(position) && getTileAt(position).getState() != TileState.WATER) {
            List<Animal> animalList = animals.get(position);

            if (animalList == null) {
                animalList = new ArrayList<>();
            }

            animalList.add(animal);
            animals.put(position, animalList);
        }
    }

    public void place(Grass grass) {
        Vector2d position = grass.getPosition();
        if (!grassAt(position) && getTileAt(position).getState() != TileState.WATER) {
            grasses.put(position, grass);
        }
    }

    public void deleteGrassAt(Vector2d position) {
        grasses.remove(position);
    }

    public void deleteDeadAnimals() {
        for (Vector2d position : animals.keySet()) {
            List<Animal> animalList = animals.get(position);

            if (animalList != null) {

                animalList = animalList.stream()
                        .filter(a -> a.getEnergy() > 0 && getTileAt(a.getPosition()).getState() != TileState.WATER)
                        .toList();

                if (animalList.isEmpty()) {
                    animals.remove(position);
                }
                else {
                    animals.put(position, animalList);
                }

            }

        }
    }

    public void moveAnimals() {
        List<Animal> animalsToPlace = new ArrayList<>();
        for (Vector2d position : animals.keySet()) {
            List<Animal> animalList = animals.get(position);

            if (animalList != null) {

                for (Animal animal : animalList) {
                    animal.move(this);
                    animal.useEnergy(1);
                    animalsToPlace.add(animal);
                }
                animals.remove(position);

            }
        }

        for (Animal animal : animalsToPlace) {
            place(animal);
        }

    }

    public void consumeGrass(MapConfig config){
        for(List<Animal> animalList : animals.values()){
            if(animalList.isEmpty()) continue;
            if(!grassAt(animalList.getFirst().getPosition())) continue;

            List<Animal> animalsAt = getAnimalsAt(animalList.getFirst().getPosition());
            Animal chosen = null;
            // young and hungry animals have priority
            for(Animal animal : animalsAt){
                if(chosen == null) chosen = animal;
                else if(chosen.getEnergy() > animal.getEnergy()) chosen = animal;
                else if(chosen.getBirthDay() > animal.getBirthDay()) chosen = animal;
            }

            // consume the grass
            chosen.addEnergy(config.grassEnergy());
            deleteGrassAt(chosen.getPosition());
        }
    }

    public void breedAnimals(MapConfig config, int day){
        for(List<Animal> animalList : animals.values()) {
            if(animalList.size() < 2) continue;
            List<Animal> breedList = new ArrayList<>(animalList);

            // check for suitable
            for(int i = breedList.size()-1; i >= 0; i--){
                if(breedList.get(i).getEnergy() < config.animalMinFedEnergy()) breedList.remove(i);
            }

            // breed possible
            while(breedList.size() > 1){
                Animal a1 = breedList.removeFirst();
                Animal a2 = breedList.removeFirst();
                Animal baby = new Animal(a1.getPosition() , config.animalStartEnergy(), config.animalGenomeLength());
                baby.setBirthValues(a1, a2, day);
                a1.useEnergy(config.animalBirthCost());
                a2.useEnergy(config.animalBirthCost());
                place(baby);
            }
        }
    }

    public boolean checkSimulationEnd() {
        return !animals.isEmpty();
    }

//// Getters and setters

    public UUID getId() {
        return id;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Tile getTileAt(Vector2d position) {
        return tiles[position.getY()][position.getX()];
    }

//// Other helper functions

    public boolean isOccupied(Vector2d position) {
        return animals.containsKey(position);
    }

    public boolean grassAt(Vector2d position) {
        return grasses.containsKey(position);
    }

    public List<Animal> getAnimalsAt(Vector2d position) {
        return animals.getOrDefault(position, null);
    }

    private void findFlowTiles() {

        int[][] neighbors = { {1, 0}, {-1, 0}, {0, -1}, {0, 1} };

        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                if (tiles[r][c].getState() != TileState.WATER) {
                    for (int[] n : neighbors) {
                        if (inBounds(r + n[0], c + n[1]) && tiles[r + n[0]][c + n[1]].getState() == TileState.WATER) {
                            flowTiles.add(new Vector2d(r, c));
                            break;
                        }
                    }
                }
            }
        }
    }

    private boolean inBounds(int r, int c) {
        return (r >= 0
                && r < height
                && c >= 0
                && c < width);
    }

    public boolean inBounds(Vector2d position) {
        return inBounds(position.getY(), position.getX());
    }

    // temporary draw function
    public void drawMap(int day) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Vector2d position = new Vector2d(x, y);
                if (isOccupied(position)) {
                    System.out.print("A "); // animal signature
                }
                else if (grassAt(position)) {
                    System.out.print("* "); // grass signature
                }
                else {
                    switch (getTileAt(position).getState()) {
                        case PLAINS -> System.out.print(". "); // tile of state PLAINS signature
                        case FOREST -> System.out.print("; "); // tile of state FOREST signature
                        case WATER -> System.out.print("  "); // tile of state WATER signature
                    }
                }
            }
            System.out.println();
        }
        System.out.println("Map ID: " + id);
        System.out.println("Animals: " + countAnimals());
        System.out.println("Grasses: " + grasses.size());
        System.out.println("Day: " + day);
        System.out.println("\n\n\n\n\n");
    }

    private int countAnimals() {
        int result = 0;
        for (List<Animal> animalList : animals.values()) {
            result += animalList.size();
        }
        return result;
    }




}
