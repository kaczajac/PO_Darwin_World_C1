package assets.model;

import assets.model.contract.MapElement;

public class Grass implements MapElement {
    private final Vector2d position;

    public Grass(Vector2d position) {
        this.position = position;
    }

    public Vector2d getPosition() {
        return position;
    }
}
