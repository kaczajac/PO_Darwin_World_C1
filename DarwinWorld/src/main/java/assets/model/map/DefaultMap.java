package assets.model.map;

import assets.model.mapelement.Animal;
import assets.model.Vector2d;
import assets.model.records.MapSettings;

public class DefaultMap extends AbstractMap {

    public DefaultMap(MapSettings settings) {
        super(settings);
    }

    @Override
    protected boolean isValidAnimalPosition(Vector2d position) {
        return inBounds(position);
    }

    @Override
    protected boolean isValidGrassPosition(Vector2d position) {
        return inBounds(position) && !grassAt(position);
    }

    @Override
    protected boolean isDead(Animal animal) {
        return animal.getEnergy() <= 0;
    }

    @Override
    protected boolean isValidEmptyPosition(Vector2d position) {
        return !isOccupied(position);
    }
}
