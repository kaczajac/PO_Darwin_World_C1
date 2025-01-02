package assets.model;

import java.util.ArrayList;
import java.util.List;

public class Animal implements WorldElement{
    private Vector2d position;
    private int[] genome;
    private int activeGene = 0;
    private Vector2d facingVector = new Vector2d(0,-1);
    private Animal[] parents = new Animal[2];
    private int energy;
    private int birthDay = 0;

    private final List<Animal> children = new ArrayList<>();

    public Animal(Vector2d position, int startEnergy , int geneLength) {
        this.position = position;
        randomGenes(geneLength);
        energy = startEnergy;
        birthDay = 0;
    }

    public Vector2d getPosition() {
        return position;
    }

    public void setPosition(Vector2d position) {
        this.position = position;
    }

    public void move(WorldMap map){
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

        if (!map.isWater(map.getTileAt(newPosition))) {
            position = newPosition;
            facingVector = moveVector;
        }

    }

    public int getNumberOfChildren(){
        return children.size();
    }

    public void addNewChild(Animal child) {
        children.add(child);
    }

    private void updateGene(int currGene) {
        activeGene = (currGene + 1) % genome.length;
    }

    public Vector2d getMoveVector(int moveDirection){
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

    public int getEnergy() {
        return energy;
    }

    public void addEnergy(int amount) {
        energy += amount;
    }

    public void useEnergy(int amount) {
        energy -= amount;
    }

    public int[] getGenome(){
        return genome;
    }

    public void setBirthValues(Animal parent1 , Animal parent2, int birthDay){
        parents[0] = parent1;
        parents[1] = parent2;
        inheritGenes(parent1.getGenome() , parent1.getEnergy(), parent2.getGenome() , parent2.getEnergy());
        this.birthDay = birthDay;
    }

    public int getBirthDay() { return birthDay; }

    public void randomGenes(int length){
        if(length == 0) return;
        genome = new int[length];
        for(int i = 0; i < length; i++){
            genome[i] = (int)(Math.random() * 8);
        }
    }

    public void inheritGenes(int[] genes1 , int energy1, int[] genes2 , int energy2){
        int length = genes1.length;
        int midPoint;
        int maxEnergy = energy1 + energy2;
        int[] firstSet, secondSet;
        if(energy1 > energy2){
            midPoint = (int)Math.floor(length * ((double)energy1 / maxEnergy));
            firstSet = genes1; secondSet = genes2;
        }
        else {
            midPoint = (int)Math.floor(length * ((double)energy2 / maxEnergy));
            firstSet = genes2; secondSet = genes1;
        }

        genome = new int[length];
        for (int i = 0; i < midPoint; i++) {
            genome[i] = firstSet[i];
        }
        for (int i = midPoint; i < length; i++) {
            genome[i] = secondSet[i];
        }
    }

    public boolean isFed(MapConfig config){
        return energy >= config.animalMinFedEnergy();
    }
}
