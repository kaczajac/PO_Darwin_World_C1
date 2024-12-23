package assets.model;

public class Animal {
    private Vector2d position;
    private int[] genes;
    private Vector2d facingVector = new Vector2d(0,-1);
    private Animal[] parents = new Animal[2];
    private int energy;

    public Animal(Vector2d position, int startEnergy , int geneLength) {
        this.position = position;
        randomGenes(geneLength);
        energy = startEnergy;
    }

    public Vector2d getPosition() {
        return position;
    }

    public void setPosition(Vector2d position) {
        this.position = position;
    }

    public void move(int moveDir){
        Vector2d moveDirection = getMoveVector(moveDir);
        position.add(moveDirection);
        facingVector = moveDirection;
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

    public int addEnergy(int amount) {
        energy += amount;
        return energy;
    }

    public int useEnergy(int amount) {
        energy -= amount;
        return energy;
    }

    public int[] getGenes(){
        return genes;
    }

    public void setBirthValues(Animal parent1 , Animal parent2){
        parents[0] = parent1;
        parents[1] = parent2;
        inheritGenes(parent1.getGenes() , parent1.getEnergy(), parent2.getGenes() , parent2.getEnergy());
    }

    public void randomGenes(int length){
        if(length == 0) return;
        genes = new int[length];
        for(int i = 0; i < length; i++){
            genes[i] = (int)(Math.random() * 8);
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

        genes = new int[length];
        for (int i = 0; i < midPoint; i++) {
            genes[i] = firstSet[i];
        }
        for (int i = midPoint; i < length; i++) {
            genes[i] = secondSet[i];
        }
    }
}
