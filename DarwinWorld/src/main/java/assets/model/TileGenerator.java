package assets.model;

public class TileGenerator {

    private final int rows;
    private final int columns;
    private final double waterLevel;

    private Tile[][] tiles;

    public TileGenerator(int rows, int columns, double waterLevel) {
        this.rows = rows;
        this.columns = columns;
        this.waterLevel = waterLevel;
        this.tiles = new Tile[rows][columns];

        setup();
        iterateTiles();
        fillEquator();
    }

    public Tile[][] getTiles() {
        return tiles;
    }

//// Helper functions for initializing tiles ('cellular automata rule 45' algorithm for procedural generation)

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
