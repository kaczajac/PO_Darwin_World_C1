package assets.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RandomPositionGenerator implements Iterable<Vector2d>, Iterator<Vector2d> {
    private final List<Vector2d> allPossiblePositions = new ArrayList<>();
    private int index = 0;
    private final int numOfElements;

    public RandomPositionGenerator(WorldMap map, int maxHeight, int maxWidth, int numOfElements) {
        this.numOfElements = numOfElements;
        for (int r = 0; r < maxHeight; r++) {
            for (int c = 0; c < maxWidth; c++) {
                Vector2d position = new Vector2d(r, c);
                if (map.getTileAt(position).getState() != TileState.WATER) {
                    this.allPossiblePositions.add(new Vector2d(r, c));
                }
            }
        }
    }

    @Override
    public Iterator<Vector2d> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return index < numOfElements;
    }

    @Override
    public Vector2d next() {
        return getNextElement();
    }

    private Vector2d getNextElement() {
        int i = (int) (Math.random() * (allPossiblePositions.size() + 1));
        Vector2d randomPosition = allPossiblePositions.get(i);
        allPossiblePositions.remove(i);
        index++;
        return randomPosition;
    }

}
