package assets.model.map;

import assets.model.mapelement.Animal;
import assets.model.mapelement.Grass;
import assets.model.Tile;
import assets.model.records.Vector2d;
import assets.model.contract.MapChangeListener;
import assets.model.mapelement.MapElement;
import assets.model.exceptions.IllegalPositionException;
import assets.model.records.MapSettings;
import assets.model.records.SimulationConfig;
import assets.model.util.RandomPositionGenerator;
import assets.model.util.TileGenerator;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public abstract class AbstractMap {

    private final UUID id;
    private final int width;
    private final int height;
    private final Tile[][] tiles;

    private final Map<Vector2d, List<Animal>> animals = new ConcurrentHashMap<>();
    private final Map<Vector2d, Grass> grasses = new HashMap<>();
    private final List<MapChangeListener> observers = new ArrayList<>();

    private int numOfNewBornAnimals = 0;
    private int numOfDeadAnimals = 0;
    private int sumOfDeadAnimalsLifeTime = 0;


    public AbstractMap(MapSettings settings) {
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
            if (animalList == null) continue;

            List<Animal> deadAnimals = animalList.stream()
                    .filter(this::isDead)
                    .toList();

            deadAnimals.forEach(deadAnimal -> {
                numOfDeadAnimals++;
                sumOfDeadAnimalsLifeTime += deadAnimal.getAge();
                deadAnimal.setDeathDay(day);
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

    public void moveAnimals() {

        List<Animal> animalsToPlace = new ArrayList<>();

        for (Vector2d position : animals.keySet()) {
            List<Animal> animalList = animals.get(position);
            if (animalList == null) continue;

            for (Animal animal : animalList) {
                animal.move(this);
                animalsToPlace.add(animal);
            }
            animals.remove(position);

        }

        for (Animal animal : animalsToPlace) {

            try {
                place(animal);
            } catch (IllegalPositionException e) {
                System.out.println(e.getMessage());
            }

        }

    }

    public void consumeGrass(SimulationConfig config) {

        for (List<Animal> animalList : animals.values()) {
            if (animalList.isEmpty() || !grassAt(animalList.getFirst().getPosition())) continue;

            Animal chosen = selectDominantAnimal(animalList);
            chosen.addEnergy(config.grassEnergy());
            chosen.updateGrassEaten();
            deleteGrassAt(chosen.getPosition());

        }
    }

    public void breedAnimals(SimulationConfig config, int day){

        for(List<Animal> animalList : animals.values()) {
            if (animalList.size() < 2) continue;

            // check for suitable and sort them in order of dominance
            List<Animal> breedList = animalList.stream()
                    .filter(a -> a.isFed(config.animalMinFedEnergy()))
                    .sorted(sortByDominance())
                    .collect(Collectors.toCollection(ArrayList::new));

            // breed possible
            while(breedList.size() > 1){
                Animal a1 = breedList.removeFirst();
                Animal a2 = breedList.removeFirst();
                Animal baby = new Animal(a1.getPosition() , 2 * config.animalBirthCost(), config.animalGenomeLength());
                baby.setBirthValues(a1, a2, day);
                a1.useEnergy(config.animalBirthCost());
                a2.useEnergy(config.animalBirthCost());
                a1.addNewChild(baby);
                a2.addNewChild(baby);
                numOfNewBornAnimals++;

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

    public void updateAnimalStatistics() {

        List<Animal> allAnimals = new ArrayList<>();

        for (List<Animal> animalList : animals.values()) {
            allAnimals.addAll(animalList);
        }

        allAnimals.sort(Comparator.comparingInt(Animal::getAge));

        for (Animal animal : allAnimals) {
            animal.useEnergy(1);
            animal.updateAge();
            animal.updateDescendants();
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
                if (isValidEmptyPosition(position)) {
                    result++;
                }

            }
        }
        return result;
    }

    public int calculateAverageEnergy() {
        if (animals.isEmpty()) return 0;

        int sumOfEnergy = 0;
        int numOfLivingAnimals = 0;
        for (List<Animal> animalList : animals.values()) {

            for (Animal animal : animalList) {
                sumOfEnergy += animal.getEnergy();
            }
            numOfLivingAnimals += animalList.size();

        }
        return (sumOfEnergy / (numOfLivingAnimals + numOfDeadAnimals));
    }

    public int calculateAverageLifeTime() {
        return numOfDeadAnimals == 0 ? 0 : (sumOfDeadAnimalsLifeTime / numOfDeadAnimals);
    }

    public int calculateAverageNumOfChildren() {
        if (animals.isEmpty()) return 0;

        int numOfLivingAnimals = 0;
        for (List<Animal> animalList : animals.values()) {
            numOfLivingAnimals += animalList.size();
        }

        return (numOfNewBornAnimals / numOfLivingAnimals);
    }

//// Listeners

    public void addObserver(MapChangeListener observer) {
        observers.add(observer);
    }

    public void sendMapChanges(){
        for(MapChangeListener observer : observers){
            observer.mapChanged(this);
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
        return tiles[position.y()][position.x()];
    }

//// Helper/abstract functions

    private Comparator<Animal> sortByDominance() {
        return Comparator.comparingInt(Animal::getEnergy)
                .thenComparingInt(Animal::getAge)
                .thenComparingInt(Animal::getNumberOfChildren);
    }

    private Animal selectDominantAnimal(List<Animal> animalList) {

        return animalList.stream()
                .max(Comparator.comparingInt(Animal::getEnergy)
                        .thenComparingInt(Animal::getAge)
                        .thenComparingInt(Animal::getNumberOfChildren))
                .orElseGet(() -> selectRandomAnimal(animalList));

    }

    private Animal selectRandomAnimal(List<Animal> animalList) {
        int randomIndex = (int) (Math.random() * animalList.size());
        return animalList.get(randomIndex);
    }

    public boolean isOccupied(Vector2d position) {
        return animals.containsKey(position);
    }

    public boolean grassAt(Vector2d position) {
        return grasses.containsKey(position);
    }

    public Optional<MapElement> objectAt(Vector2d position) {
        if (animals.containsKey(position)) return Optional.ofNullable(selectDominantAnimal(animals.get(position)));
        else return Optional.ofNullable(grasses.get(position));
    }

    public boolean inBounds(Vector2d position) {
        return position.follows(new Vector2d(0, 0)) && position.precedes(new Vector2d(this.getWidth() - 1, this.getHeight() - 1));
    }

    protected abstract boolean isValidAnimalPosition(Vector2d position);

    protected abstract boolean isValidGrassPosition(Vector2d position);

    protected abstract boolean isDead(Animal animal);

    protected abstract boolean isValidEmptyPosition(Vector2d position);

}
