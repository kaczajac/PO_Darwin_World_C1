package assets.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RandomPositionGenerator implements Iterable<Vector2d> {
    private final List<Vector2d> plainsPositions = new ArrayList<>();
    private final List<Vector2d> forestPositions = new ArrayList<>();

    private final int numOfElements;
    private final String element;
    private int cursor = 0;

    public RandomPositionGenerator(WorldMap map, int numOfElements, String element) {
        this.numOfElements = numOfElements;
        this.element = element;

        int height = map.getHeight();
        int width = map.getWidth();
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                Vector2d position = new Vector2d(r, c);
                if (!map.grassAt(position)) {

                    switch (map.getTileAt(position).getState()) {
                        case PLAINS -> plainsPositions.add(position);
                        case FOREST -> forestPositions.add(position);
                        default -> {}
                    }

                }

            }
        }
    }

    @Override
    public Iterator<Vector2d> iterator() {
        return element.equals("Animal") ? new AnimalIterator() : new GrassIterator();
    }

//// Different iterators for different world elements

    private class AnimalIterator implements Iterator<Vector2d> {

        private final List<Vector2d> allPossiblePositions;
        private final int size;

        public AnimalIterator() {
            allPossiblePositions = plainsPositions;
            allPossiblePositions.addAll(forestPositions);

            size = Math.min(numOfElements, allPossiblePositions.size());
        }

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public Vector2d next() {
            return getNextElement();
        }

        private Vector2d getNextElement() {
            int i = (int) (Math.random() * allPossiblePositions.size());
            Vector2d randomPosition = allPossiblePositions.get(i);
            allPossiblePositions.remove(i);
            cursor++;
            return randomPosition;
        }
    }

    private class GrassIterator implements Iterator<Vector2d> {

        private final int size;

        public GrassIterator() {
            int availablePositions = plainsPositions.size() + forestPositions.size();
            size = Math.min(numOfElements, availablePositions);
        }

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public Vector2d next() {
            return getNextElement();
        }

        private Vector2d getNextElement() {
            // 80% chance of not-preferred positions (plains)
            if ((Math.random() < 0.2 && !plainsPositions.isEmpty()) || forestPositions.isEmpty()) {
                int i = (int) (Math.random() * plainsPositions.size());
                Vector2d randomPosition = plainsPositions.get(i);
                plainsPositions.remove(i);
                cursor++;
                return randomPosition;
            }

            // 20% chance of preferred positions (forest)
            else {
                int i = (int) (Math.random() * forestPositions.size());
                Vector2d randomPosition = forestPositions.get(i);
                forestPositions.remove(i);
                cursor++;
                return randomPosition;
            }
        }
    }

}
