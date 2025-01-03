package assets.model;

import assets.model.enums.TileState;

public class Tile {
    private TileState state;

    public Tile(TileState state) {
        this.state = state;
    }

    public TileState getState() {
        return this.state;
    }

    public void setState(TileState state) {
        this.state = state;
    }

}
