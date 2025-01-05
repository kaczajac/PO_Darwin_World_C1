package assets.model.map;

import assets.model.Animal;
import assets.model.Grass;
import assets.model.Tile;
import assets.model.Vector2d;
import assets.model.contract.MapChangeListener;
import assets.model.exceptions.IllegalPositionException;
import assets.model.records.MapSettings;
import assets.model.records.SimulationConfig;
import assets.model.util.RandomPositionGenerator;
import assets.model.util.TileGenerator;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseMap {

    private final UUID id;
    private final int width;
    private final int height;
    private final Tile[][] tiles;

    private final Map<Vector2d, List<Animal>> animals = new ConcurrentHashMap<>();
    private final Map<Vector2d, Grass> grasses = new HashMap<>();
    private final List<MapChangeListener> observers = new ArrayList<>();

    // Additional parameters for statistics
    private int numOfDeadAnimals = 0;
    private int sumOfDeadAnimalsLifeTime = 0;

    public BaseMap(MapSettings settings) {
        this.id = UUID.randomUUID();
        this.width = settings.mapWidth();
        this.height = settings.mapHeight();
        this.tiles = new TileGenerator(settings).getTiles();
    }

//// Simulation functions

    public void placeAnimals(SimulationConfig config) {

        RandomPositionGenerator generator = new RandomPositionGenerator(this, config.animalStartNumber(), Animal.class);
        for (Vector2d position : generator) {
            Animal animal = new Animal(position, config.animalStartEnergy(), config.animalGenomeLength());

            try {
                place(animal);
            } catch (IllegalPositionException e) {
                System.out.println(e.getMessage());
            }

        }

    }

    public void placeGrasses(int numOfGrass) {

        RandomPositionGenerator generator = new RandomPositionGenerator(this, numOfGrass, Grass.class);
        for (Vector2d position : generator) {
            Grass grass = new Grass(position);

            try {
                place(grass);
            } catch (IllegalPositionException e) {
                System.out.println(e.getMessage());
            }

        }

    }

    public void place(Animal animal) throws IllegalPositionException {

        Vector2d position = animal.getPosition();
        if (isValidAnimalPosition(position)) {
            List<Animal> animalList = animals.get(position);

            if (animalList == null) {
                animalList = new ArrayList<>();
            }

            animalList.add(animal);
            animals.put(position, animalList);
        }
        else throw new IllegalPositionException(position);

    }

    public void place(Grass grass) throws IllegalPositionException {

        Vector2d position = grass.getPosition();
        if (isValidGrassPosition(position)) {
            grasses.put(position, grass);
        }
        else throw new IllegalPositionException(position);

    }

    public void deleteGrassAt(Vector2d position) {
        grasses.remove(position);
    }

    public void deleteDeadAnimals(int day) {
        for (Vector2d position : animals.keySet()) {
            List<Animal> animalList = animals.get(position);

            if (animalList != null) {

                List<Animal> deadAnimals = animalList.stream()
                        .filter(this::isDead)
                        .toList();

                deadAnimals.forEach(deadAnimal -> {
                    numOfDeadAnimals++;
                    sumOfDeadAnimalsLifeTime += (day - deadAnimal.getBirthDay() + 1);
                });

                animalList.removeAll(deadAnimals);

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
                    animalsToPlace.add(animal);
                }
                animals.remove(position);

            }
        }

        for (Animal animal : animalsToPlace) {

            try {
                place(animal);
            } catch (IllegalPositionException e) {
                System.out.println(e.getMessage());
            }

        }

    }

    public void consumeGrass(SimulationConfig config){
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

    public void breedAnimals(SimulationConfig config, int day){
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
                a1.addNewChild(baby);
                a2.addNewChild(baby);

                try {
                    place(baby);
                } catch (IllegalPositionException e) {
                    System.out.println(e.getMessage());
                }

            }
        }
    }

    public boolean checkSimulationEnd() {
        return !animals.isEmpty();
    }

    public void updateAnimalEnergy() {
        for (List<Animal> animalList : animals.values()) {
            for (Animal animal : animalList) {
                animal.useEnergy(1);
            }
        }
    }

//// Scoreboard functions

    public int countAnimals() {
        int result = 0;
        for (List<Animal> animalList : animals.values()) {
            result += animalList.size();
        }
        return result;
    }

    public int countGrasses() {
        return grasses.size();
    }

    public int countEmptyPositions() {
        int result = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                Vector2d position = new Vector2d(x, y);
                if (isValidEmpty(position)) {
                    result++;
                }

            }
        }
        return result;
    }

    public int calculateAverageEnergy() {
        if (animals.isEmpty()) return 0;

        int sumOfEnergy = 0;
        int numOfAnimals = 0;
        for (List<Animal> animalList : animals.values()) {

            for (Animal animal : animalList) {
                sumOfEnergy += animal.getEnergy();
            }
            numOfAnimals += animalList.size();

        }
        return (sumOfEnergy / numOfAnimals);
    }

    public int calculateAverageLifeTime() {
        return numOfDeadAnimals == 0 ? 0 : (sumOfDeadAnimalsLifeTime / numOfDeadAnimals);
    }

    public int calculateAverageNumOfChildren() {
        if (animals.isEmpty()) return 0;

        int allChildren = 0;
        int numOfAnimals = 0;
        for (List<Animal> animalList : animals.values()) {

            for (Animal animal : animalList) {
                allChildren += animal.getNumberOfChildren();
            }
            numOfAnimals += animalList.size();

        }
        return (allChildren / numOfAnimals);
    }

//// Listeners

    public void addObserver(MapChangeListener observer) {
        observers.add(observer);
    }

    public void sendMapChanges(int day){
        for(MapChangeListener observer : observers){
            observer.mapChanged(this, day);
        }
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

//// Helper/abstract functions

    public boolean isOccupied(Vector2d position) {
        return animals.containsKey(position);
    }

    public boolean grassAt(Vector2d position) {
        return grasses.containsKey(position);
    }

    public List<Animal> getAnimalsAt(Vector2d position) {
        return animals.getOrDefault(position, null);
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

    protected abstract boolean isValidAnimalPosition(Vector2d position);

    protected abstract boolean isValidGrassPosition(Vector2d position);

    protected abstract boolean isDead(Animal animal);

    protected abstract boolean isValidEmpty(Vector2d position);

}
