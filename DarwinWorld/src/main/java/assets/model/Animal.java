package assets.model;

public class Animal {
    private Vector2d position;
    private int[] genes;
    private Vector2d facingVector = new Vector2d(0,-1);

    public Animal(Vector2d position) {
        this.position = position;
    }

    public Animal(int x, int y) {
        position = new Vector2d(x, y);
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
}
