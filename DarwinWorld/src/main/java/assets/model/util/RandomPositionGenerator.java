package assets.model.util;

import assets.model.Vector2d;
import assets.model.contract.MapElement;
import assets.model.WorldMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RandomPositionGenerator implements Iterable<Vector2d> {
    private final List<Vector2d> plainsPositions = new ArrayList<>();
    private final List<Vector2d> forestPositions = new ArrayList<>();

    private final int numOfElements;
    private final String className;
    private int cursor = 0;

    public <T extends MapElement> RandomPositionGenerator(WorldMap map, int numOfElements, Class<T> elementClass) {
        this.numOfElements = numOfElements;
        this.className = elementClass.getSimpleName();

        int height = map.getHeight();
        int width = map.getWidth();
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                Vector2d position = new Vector2d(r, c);

                if (isValidPosition(position, map)) {

                    switch (map.getTileAt(position).getState()) {
                        case PLAINS -> plainsPositions.add(position);
                        case FOREST -> forestPositions.add(position);
                        default -> {}
                    }

                }

            }
        }
    }

    private boolean isValidPosition(Vector2d position, WorldMap map) {
        boolean isGrass = className.equals("Grass");
        boolean isAnimal = className.equals("Animal");

        return ((isGrass && !map.grassAt(position))
                || (isAnimal && !map.isOccupied(position)));
    }

    @Override
    public Iterator<Vector2d> iterator() {
        if (className.equals("Grass")) return new GrassIterator();
        else if (className.equals("Animal")) return new AnimalIterator();
        else throw new IllegalArgumentException("Unexpected class name: " + className);
    }

//// Different iterators for different world elements

    private class AnimalIterator implements Iterator<Vector2d> {

        private final List<Vector2d> allPossiblePositions;
        private final int size;

        public AnimalIterator() {
            allPossiblePositions = new ArrayList<>(plainsPositions);
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
            Vector2d randomPosition = selectRandomPosition(allPossiblePositions);

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
            boolean selectFromPlains = (Math.random() < 0.2 && !plainsPositions.isEmpty()) || forestPositions.isEmpty();
            Vector2d randomPosition = selectRandomPosition(selectFromPlains ? plainsPositions : forestPositions);

            cursor++;
            return randomPosition;
        }

    }

    private Vector2d selectRandomPosition(List<Vector2d> positions) {
        int i = (int) (Math.random() * positions.size());
        Vector2d randomPosition = positions.get(i);
        positions.remove(i);
        return randomPosition;
    }

}
