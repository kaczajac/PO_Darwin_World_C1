package assets.model;

import java.util.*;
import java.util.stream.Collectors;

public class WorldMap {

    private final UUID id;
    private final int width;
    private final int height;
    private Tile[][] tiles;

    private Map<Vector2d, Animal> animals = new HashMap<>();
    private final Map<Vector2d, Grass> grasses = new HashMap<>();
    private final List<Vector2d> flowTiles = new ArrayList<>();

    public WorldMap(int height, int width) {
        this.id = UUID.randomUUID();
        this.width = width;
        this.height = height;
        this.tiles = new Tile[height][width];

        setup();

        int loops = Math.max(height, width);
        for (int i = 0; i < loops; i++) {
            iterateTiles();
        }

        fillEquator();
        findFlowTiles();
    }

//// Helper functions for initializing a map ('cellular automata rule 45' algorithm for procedural generation)

    private void fillEquator() {
        int numOfLines = (int) Math.ceil(height * 0.2);
        int r = (int) Math.ceil((height - numOfLines) / 2.0);

        while (numOfLines > 0) {
            for (int c = 0; c < width; c++) {
                if (tiles[r][c].getState() != TileState.WATER) tiles[r][c].setState(TileState.FOREST);
            }
            r++; numOfLines--;
        }
    }

    private void setup() {

        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                if (Math.random() < 0.4) tiles[r][c] = new Tile(TileState.WATER);
                else tiles[r][c] = new Tile(TileState.PLAINS);
            }
        }

    }

    private void iterateTiles() {
        Tile[][] newTiles = new Tile[height][width];

        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                if (numWallsAround(r, c) >= 5) newTiles[r][c] = new Tile(TileState.PLAINS);
                else newTiles[r][c] = new Tile(TileState.WATER);
            }
        }

        tiles = newTiles;
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

    private void findFlowTiles() {

        int[][] neighbors = { {1, 0}, {-1, 0}, {0, -1}, {0, 1} };

        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                if (tiles[r][c].getState() != TileState.WATER) {
                    for (int[] n : neighbors) {
                        if (inBounds(r + n[0], c + n[1]) && tiles[r + n[0]][c + n[1]].getState() == TileState.WATER) {
                            flowTiles.add(new Vector2d(r, c));
                            break;
                        }
                    }
                }
            }
        }
    }

    private boolean inBounds(int x, int y) {
        return (x >= 0 && x < height && y >= 0 && y < width);
    }

//// Functions for placing/removing elements from the map

    public void place(Animal animal) {
        Vector2d position = animal.getPosition();
        if (getTileAt(position).getState() != TileState.WATER) {
            animals.put(position, animal);
        }
    }

    public void place(Grass grass) {
        Vector2d position = grass.getPosition();
        if (!grassAt(position) && getTileAt(position).getState() != TileState.WATER) {
            grasses.put(position, grass);
        }
    }

    public void deleteGrassAt(Vector2d position) {
        grasses.remove(position);
    }

    public void deleteDeadAnimals() {
        animals = animals.entrySet().stream()
                .filter(entry -> entry.getValue().getEnergy() > 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

//// Getters and setters

    public UUID getId() {
        return id;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public List<Vector2d> getFlowTiles() {
        return flowTiles;
    }

    public Tile getTileAt(Vector2d position) {
        return tiles[position.getX()][position.getY()];
    }

//// Other functions

    public boolean isOccupied(Vector2d position) {
        return animals.containsKey(position);
    }

    public boolean grassAt(Vector2d position) {
        return grasses.containsKey(position);
    }

    public void move(Animal animal, int moveDirection) {
        if (animals.containsValue(animal)) {

            Vector2d initPosition = animal.getPosition();
            animals.remove(initPosition);

            animal.move(moveDirection);

            Vector2d newPosition = animal.getPosition();
            animals.put(newPosition, animal);
        }
    }

    // temporary draw function
    public void drawMap() {
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                Vector2d position = new Vector2d(r, c);
                if (isOccupied(position)) {
                    System.out.print("A "); // animal signature
                }
                else if (grassAt(position)) {
                    System.out.print("* "); // grass signature
                }
                else {
                    switch (getTileAt(position).getState()) {
                        case PLAINS -> System.out.print(". "); // tile of state PLAINS signature
                        case FOREST -> System.out.print("; "); // tile of state FOREST signature
                        case WATER -> System.out.print("  "); // tile of state WATER signature
                    }
                }
            }
            System.out.println();
        }
    }




}
