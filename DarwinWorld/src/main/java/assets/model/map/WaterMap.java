package assets.model.map;

import assets.model.Animal;
import assets.model.Tile;
import assets.model.Vector2d;
import assets.model.enums.TileState;
import assets.model.records.MapSettings;

import java.util.ArrayList;
import java.util.List;

public class WaterMap extends BaseMap {

    private final List<Vector2d> flowTilesPositions = new ArrayList<>();

    public WaterMap(MapSettings settings) {
        super(settings);
        findFlowTiles();
    }

//// Additional simulation functions

    public void triggerFlow() {
        if (flowTilesPositions.isEmpty()) return;

        Vector2d position = flowTilesPositions.getFirst();
        Tile tile = getTileAt(position);

        TileState targetState = isWater(tile) ? TileState.PLAINS : TileState.WATER;

        for (Vector2d flowPosition : flowTilesPositions) {
            Tile flowTile = getTileAt(flowPosition);
            flowTile.setState(targetState);

            if (grassAt(position)) {
                deleteGrassAt(position);
            }
        }
    }

    private void findFlowTiles() {

        int[][] neighbors = { {1, 0}, {-1, 0}, {0, -1}, {0, 1} };
        int height = super.getHeight();
        int width = super.getWidth();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                Vector2d position = new Vector2d(x, y);
                if (!isWater(getTileAt(position))) {

                    for (int[] n : neighbors) {
                        Vector2d neighbor = new Vector2d(x + n[0], y + n[1]);
                        if (inBounds(neighbor) && isWater(getTileAt(neighbor))) {
                            flowTilesPositions.add(position);
                            break;
                        }
                    }

                }

            }
        }

    }

//// Other functions

    private boolean isWater(Tile tile) {
        return tile.getState() == TileState.WATER;
    }

    @Override
    protected boolean isValidAnimalPosition(Vector2d position) {
        return inBounds(position) && !isWater(getTileAt(position));
    }

    @Override
    protected boolean isValidGrassPosition(Vector2d position) {
        return inBounds(position) && !grassAt(position) && !isWater(getTileAt(position));
    }

    @Override
    protected boolean isDead(Animal animal) {
        return animal.getEnergy() <= 0 || isWater(getTileAt(animal.getPosition()));
    }

    @Override
    protected boolean isValidEmpty(Vector2d position) {
        return !isWater(getTileAt(position)) && !isOccupied(position);
    }
}
