package assets.model;

import assets.model.enums.TileState;

public class Tile {

    private TileState state;
    private int grassCount = 0;

    public Tile(TileState state) {
        this.state = state;
    }

    public TileState getState() {
        return this.state;
    }

    public void setState(TileState state) {
        this.state = state;
    }

    public int getGrassCount() {
        return grassCount;
    }

    public void updateGrassCount() {
        this.grassCount += 1;
    }

}
