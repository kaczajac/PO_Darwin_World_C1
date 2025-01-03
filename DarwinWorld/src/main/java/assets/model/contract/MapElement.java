package assets.model.contract;

import assets.model.Vector2d;

public interface MapElement {
    /**
     * Returns Vector2d which represents position of a world element
     *
     * @return position of the world element
     */
    Vector2d getPosition();
}
