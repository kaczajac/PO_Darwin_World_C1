package assets.model;

public class Scoreboard {

    private int numOfAnimals;
    private int numOfGrass;
    private int numOfEmptyPositions;
    private int averageAnimalEnergy;
    private int averageLifeTime;
    private int averageNumOfChildren;


    public void updateStatistics(int numOfAnimals,
                                int numOfGrass,
                                int numOfEmptyPositions,
                                int averageAnimalEnergy,
                                int averageLifeTime,
                                int averageNumOfChildren)
    {
        this.numOfAnimals = numOfAnimals;
        this.numOfGrass = numOfGrass;
        this.numOfEmptyPositions = numOfEmptyPositions;
        this.averageAnimalEnergy = averageAnimalEnergy;
        this.averageLifeTime = averageLifeTime;
        this.averageNumOfChildren = averageNumOfChildren;
    }

    public int getNumOfAnimals() {
        return numOfAnimals;
    }

    public int getNumOfGrass() {
        return numOfGrass;
    }

    public int getNumOfEmptyPositions() {
        return numOfEmptyPositions;
    }

    public int getAverageAnimalEnergy() {
        return averageAnimalEnergy;
    }

    public int getAverageLifeTime() {
        return averageLifeTime;
    }

    public int getAverageNumOfChildren() {
        return averageNumOfChildren;
    }

}
