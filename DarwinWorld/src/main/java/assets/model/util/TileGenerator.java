package assets.model.util;

import assets.model.records.WorldMapSettings;
import assets.model.Tile;
import assets.model.enums.TileState;

public class TileGenerator {

    // size
    private final int rows;
    private final int columns;

    // optional parameters
    private final Double waterLevel;

    // final result
    private Tile[][] tiles;

    public TileGenerator(WorldMapSettings settings) {

        this.rows = settings.mapHeight();
        this.columns = settings.mapWidth();
        this.waterLevel = settings.mapWaterLevel() != null ? settings.mapWaterLevel() : 0.0;
        this.tiles = new Tile[rows][columns];

        switch (settings.mapType()) {
            case DEFAULT -> defaultMapGeneration();
            case WATER -> waterMapGeneration();
            default -> throw new IllegalStateException("Unexpected value: " + settings.mapType());
        }

    }

    public Tile[][] getTiles() {
        return tiles;
    }

//// Water map generation ('cellular automata rule 45' algorithm for procedural generation)

    private void waterMapGeneration() {
        setup();
        iterateTiles();
    }

    private void setup() {

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                if (Math.random() < waterLevel) tiles[r][c] = new Tile(TileState.WATER);
                else tiles[r][c] = new Tile(TileState.PLAINS);
            }
        }

    }

    private void iterateTiles() {

        int loops = Math.max(rows, columns);
        for (int i = 0; i < loops; i++) {

            Tile[][] newTiles = new Tile[rows][columns];

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < columns; c++) {
                    if (numWallsAround(r, c) >= 5) newTiles[r][c] = new Tile(TileState.PLAINS);
                    else newTiles[r][c] = new Tile(TileState.WATER);
                }
            }

            tiles = newTiles;

        }

    }

//// Default map generation

    private void defaultMapGeneration() {

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                tiles[r][c] = new Tile(TileState.PLAINS);
            }
        }
        fillEquator();

    }

//// Helper functions

    private int numWallsAround(int x, int y) {

        int num = 0;
        for (int r = -1; r <= 1; r++) {
            for (int c = -1; c <= 1; c++) {
                if (inBounds(x + r, y + c)) {
                    if (tiles[x + r][y + c].getState() != TileState.WATER){
                        num++;
                    }
                }
            }
        }
        return num;

    }

    private void fillEquator() {

        int numOfLines = (int) Math.ceil(rows * 0.2);
        int r = (int) Math.ceil((rows - numOfLines) / 2.0);

        while (numOfLines > 0) {
            for (int c = 0; c < columns; c++) {
                if (tiles[r][c].getState() != TileState.WATER) tiles[r][c].setState(TileState.FOREST);
            }
            r++; numOfLines--;
        }

    }

    private boolean inBounds(int r, int c) {

        return (r >= 0
                && r < rows
                && c >= 0
                && c < columns);

    }

}
