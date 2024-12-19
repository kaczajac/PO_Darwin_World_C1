package assets.model;

import java.util.HashMap;
import java.util.Map;

public class WorldMap {

    private final int width;
    private final int height;
    private Tile[][] tiles;

    private Map<Vector2d, Animal> animals = new HashMap<>();
    private Map<Vector2d, Grass> grasses = new HashMap<>();

    public WorldMap(int height, int width) {
        this.width = width;
        this.height = height;
        this.tiles = new Tile[height][width];

        setup();

        int loops = Math.max(height, width);
        for (int i = 0; i < loops; i++) {
            iterateTiles();
        }

        fillEquator();
        placeGrass();

    }

//// Helper functions for initializing a map ('cellular automata rule 45' algorithm for procedural generation)

    private void fillEquator() {
        int numOfLines = (int) Math.ceil(height * 0.2);
        int row = (int) Math.ceil((height - numOfLines) / 2.0);

        while (numOfLines > 0) {
            for (int col = 0; col < width; col++) {
                tiles[row][col].setType(TileType.FOREST);
            }
            row++; numOfLines--;
        }
    }

    private void setup() {

        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                if (Math.random() < 0.4) tiles[r][c] = new Tile(TileType.WATER);
                else tiles[r][c] = new Tile(TileType.PLAINS);
            }
        }

    }

    private void iterateTiles() {
        Tile[][] newTiles = new Tile[height][width];

        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                if (numWallsAround(r, c) >= 5) newTiles[r][c] = new Tile(TileType.PLAINS);
                else newTiles[r][c] = new Tile(TileType.WATER);
            }
        }

        tiles = newTiles;
    }

    private int numWallsAround(int x, int y) {
        int num = 0;
        for (int r = -1; r <= 1; r++) {
            for (int c = -1; c <= 1; c++) {
                if (inBounds(x + r, y + c)) {
                    if (tiles[x + r][y + c].getType() != TileType.WATER){
                        num++;
                    }
                }
            }
        }
        return num;
    }

    private boolean inBounds(int x, int y) {
        return (x >= 0 && x < height && y >= 0 && y < width);
    }

    private void placeAnimals() {
        // to do
    }

    private void placeGrass() {

        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {

                Vector2d position = new Vector2d(r, c);

                if (!grasses.containsKey(position)) {
                    Grass newGrass = new Grass(position);
                    try {
                        switch(tiles[r][c].getType()) {
                            case PLAINS -> {
                                if (Math.random() <= 0.2) grasses.put(position, newGrass);
                            }
                            case FOREST -> {
                                if (Math.random() <= 0.8) grasses.put(position, newGrass);
                            }
                            case WATER -> {}
                            default -> throw new IllegalStateException("Unexpected value: " + tiles[r][c].getType());
                        }
                    } catch (IllegalStateException e) {
                        System.out.println(e.getMessage());
                    }
                }

            }
        }

    }

//// Other methods

    public boolean isOccupied(Vector2d position) {
        return animals.containsKey(position);
    }

    public boolean grassAt(Vector2d position) {
        return grasses.containsKey(position);
    }

    public Tile getTile(Vector2d position) {
        return tiles[position.getX()][position.getY()];
    }


}
