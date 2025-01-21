package assets.model.contract;


import assets.model.Tile;
import assets.model.mapelement.WorldElement;
import assets.model.records.Vector2d;

import java.util.Optional;
import java.util.UUID;

public interface WorldMap {

    /**
     * Return true if given position on the map is occupied.
     *
     * @param position Position to check.
     * @return True if the position is occupied.
     */
    boolean isOccupied(Vector2d position);

    /**
     * Return true if there is grass at given position
     *
     * @param position Position to check.
     * @return True if grass is at the position.
     */
    boolean grassAt(Vector2d position);

    /**
     * Return a WorldElement at a given position.
     *
     * @param position The position of the WorldElement.
     * @return WorldElement or null if the position is not occupied.
     */
    Optional<WorldElement> objectAt(Vector2d position);
    /**
     * Return a map tile at a given position.
     *
     * @param position The position of the Tile.
     * @return Tile at the given position.
     */
    Tile getTileAt(Vector2d position);

    UUID getId();

}
