package assets.model.mapelement;

import assets.model.Vector2d;
import javafx.scene.image.ImageView;

import java.util.Objects;
import java.util.UUID;

public class Grass extends MapElement {

    private final UUID id;
    private final Vector2d position;

    public Grass(Vector2d position) {
        this.id = UUID.randomUUID();
        this.position = position;
    }

    public Vector2d getPosition() {
        return position;
    }

    @Override
    public UUID getID() {
        return id;
    }

    @Override
    public ImageView getImageRepresentation() {
        return createImageView(GRASS_IMAGE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Grass grass)) return false;
        return this.id == grass.id && position.equals(grass.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, position);
    }
}
