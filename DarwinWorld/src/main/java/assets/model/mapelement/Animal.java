package assets.model.mapelement;

import assets.model.records.SimulationConfig;
import assets.model.records.Vector2d;
import assets.model.enums.TileState;
import assets.model.map.AbstractWorldMap;
import assets.model.util.GenomeGenerator;
import javafx.scene.image.ImageView;

import java.util.*;

public class Animal extends WorldElement {

    private final UUID id;
    private Vector2d position;
    private Vector2d facingVector;
    private int energy;
    private int grassEaten;
    private int activeGene;
    private int age;
    private int birthDay;
    private int deathDay;
    private int descendantsCount;

    private final List<Animal> children = new ArrayList<>();
    private final int[] genome;
    private final int imageNumber;


    public Animal(Vector2d position, int startEnergy , int geneCount) {

        this.id = UUID.randomUUID();
        this.position = position;
        this.energy = startEnergy;
        this.grassEaten = 0;
        this.activeGene = 0;
        this.age = 0;
        this.deathDay = -1;

        this.facingVector = getMoveVector((int) (Math.random() * 8));
        this.genome = GenomeGenerator.getRandomGenes(geneCount);
        this.imageNumber = (int) (Math.random() * 3) + 1;

    }

//// Move mechanism

    public void move(AbstractWorldMap map){
        Vector2d moveVector = getMoveVector(genome[activeGene]);
        updateGene(activeGene);

        Vector2d newPosition = position.add(moveVector);

        if (!map.inBounds(newPosition)) {

            if (newPosition.y() <= -1 || newPosition.y() >= map.getHeight()) return;

            if (newPosition.x() <= -1) {
                newPosition = new Vector2d(map.getWidth() - 1, newPosition.y());
            }
            else if (newPosition.x() >= map.getWidth()) {
                newPosition = new Vector2d(0, newPosition.y());
            }

        }

        if (map.getTileAt(newPosition).getState() != TileState.WATER) {
            position = newPosition;
            facingVector = moveVector;
        }

    }

    private Vector2d getMoveVector(int moveDirection){
        switch (moveDirection){
            case 0 -> {
                return new Vector2d(0,1);
            }
            case 1 -> {
                return new Vector2d(1,1);
            }
            case 2 -> {
                return new Vector2d(1,0);
            }
            case 3 -> {
                return new Vector2d(1,-1);
            }
            case 4 -> {
                return new Vector2d(0,-1);
            }
            case 5 -> {
                return new Vector2d(-1,-1);
            }
            case 6 -> {
                return new Vector2d(-1,0);
            }
            case 7 -> {
                return new Vector2d(-1,1);
            }
        }
        return new Vector2d(0,0);
    }

//// Genes and inheritance mechanism

    public void setBirthValues(Animal parent1 , Animal parent2, int day, SimulationConfig config) {

        this.birthDay = day;
        this.activeGene = (int) (Math.random() * config.animalGenomeLength());

        int[] newGenome = GenomeGenerator.getInheritedGenes(parent1, parent2);
        if (Math.random() < config.animalMutationChance()) {
            GenomeGenerator.mutateGenes(config, newGenome);
        }
        this.setGenome(newGenome);

    }

//// Getters for simulation and statistics

    public Vector2d getPosition() {
        return position;
    }

    public int getNumberOfChildren(){
        return children.size();
    }

    public int getNumberOfDescendants() {
        return descendantsCount;
    }

    public int getEnergy() {
        return energy;
    }

    public int getGrassEaten() {
        return grassEaten;
    }

    public int[] getGenome(){
        return genome;
    }

    public void setGenome(int[] genes){
        int length = genome.length;
        System.arraycopy(genes, 0, genome, 0, length);
    }

    public int getGene() {
        return activeGene;
    }

    public int getAge() {
        return age;
    }

    public int getBirthDay() {
        return birthDay;
    }

    public int getDeathDay() {
        return deathDay;
    }

    @Override
    public ImageView getImageRepresentation() {
        return createImageView(ANIMAL_IMAGES.get(imageNumber));
    }

    @Override
    public UUID getID() {
        return id;
    }

//// Setters

    public void addNewChild(Animal child) {
        children.add(child);
    }

    public void addEnergy(int amount) {
        energy += amount;
    }

    public void useEnergy(int amount) {
        energy -= amount;
    }

    public void setDeathDay(int deathDay) {
        this.deathDay = deathDay;
    }

//// Helper functions

    public void updateDescendants() {

        int count = 0;
        for (Animal child : children) {
            count += (1 + child.getNumberOfDescendants());
        }

        this.descendantsCount = count;

    }

    private void updateGene(int currentGene) {
        activeGene = (currentGene + 1) % genome.length;
    }

    public void updateAge() { age += 1; }

    public void updateGrassEaten() {
        this.grassEaten += 1;
    }

    public boolean isFed(int minFedEnergy){
        return energy >= minFedEnergy;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Animal other)) return false;
        return this.id == other.id;
    }
}
