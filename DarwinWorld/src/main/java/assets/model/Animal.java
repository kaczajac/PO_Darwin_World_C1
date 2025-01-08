package assets.model;

import assets.model.contract.MapElement;
import assets.model.enums.TileState;
import assets.model.map.AbstractMap;

import java.util.ArrayList;
import java.util.List;

public class Animal implements MapElement {

    private Vector2d position;
    private Vector2d facingVector;
    private int energy;
    private int activeGene;
    private int age;
    private int birthDay;
    private int deathDay;

    private final List<Animal> children = new ArrayList<>();
    private final int[] genome;

    public Animal(Vector2d position, int startEnergy , int geneCount) {
        this.position = position;
        this.energy = startEnergy;
        this.activeGene = 0;
        this.age = 0;

        this.facingVector = getMoveVector((int) (Math.random() * 8));
        this.genome = randomGenes(geneCount);
    }

//// Move mechanism

    public void move(AbstractMap map){
        Vector2d moveVector = getMoveVector(genome[activeGene]);
        updateGene(activeGene);

        Vector2d newPosition = position.add(moveVector);

        if (!map.inBounds(newPosition)) {

            if (newPosition.equals(new Vector2d(-1, newPosition.getY()))) {
                newPosition = new Vector2d(map.getWidth() - 1, newPosition.getY());
            }
            else if (newPosition.equals(new Vector2d(map.getWidth(), newPosition.getY()))) {
                newPosition = new Vector2d(0, newPosition.getY());
            }
            else return;

        }

        if (map.getTileAt(newPosition).getState() != TileState.WATER) {
            position = newPosition;
            facingVector = moveVector;
        }

    }

//// Genes and inheritance mechanism

    public void setBirthValues(Animal parent1 , Animal parent2, int day) {
        inheritGenes(parent1.getGenome(), parent1.getEnergy(), parent2.getGenome(), parent2.getEnergy());
        this.birthDay = day;
    }

    private void inheritGenes(int[] genes1 , int energy1, int[] genes2 , int energy2){
        int length = genome.length;
        int maxEnergy = energy1 + energy2;

        // Determine the mid-point based on the energy levels
        int midPoint = (int) Math.floor(length * (Math.max(energy1, energy2) / (double) maxEnergy));

        // Choose which gene set to use for the first and second part of the genome
        int[] firstSet = (energy1 > energy2) ? genes1 : genes2;
        int[] secondSet = (energy1 > energy2) ? genes2 : genes1;

        // Create the new genome by combining both gene sets
        int[] newGenome = new int[length];
        System.arraycopy(firstSet, 0, newGenome, 0, midPoint);
        System.arraycopy(secondSet, midPoint, newGenome, midPoint, length - midPoint);

        // Update the current genome
        System.arraycopy(newGenome, 0, genome, 0, length);

    }

//// Getters for simulation and statistics

    public Vector2d getPosition() {
        return position;
    }

    public Vector2d getFacingVector() {
        return facingVector;
    }

    public int getNumberOfChildren(){
        return children.size();
    }

    public int getNumberOfDescendants() {
        int result = this.getNumberOfChildren();
        for (Animal child : children) {
            result += child.getNumberOfDescendants();
        }
        return result;
    }

    public int getEnergy() {
        return energy;
    }

    public int[] getGenome(){
        return genome;
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

    private int[] randomGenes(int geneCount){
        if (geneCount == 0) {
            return new int[0];
        }

        int[] newGenome = new int[geneCount];
        for(int i = 0; i < geneCount; i++){
            newGenome[i] = (int) (Math.random() * 8);
        }
        return newGenome;
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

    private void updateGene(int currentGene) {
        activeGene = (currentGene + 1) % genome.length;
    }

    public void updateAge() { age += 1; }

    public boolean isFed(int minFedEnergy){
        return energy >= minFedEnergy;
    }

}
