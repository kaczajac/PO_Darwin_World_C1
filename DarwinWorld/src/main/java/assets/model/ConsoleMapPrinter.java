package assets.model;

public class ConsoleMapPrinter implements MapChangeListener {

    @Override
    public synchronized void mapChanged(WorldMap map, int day) {
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {

                Vector2d position = new Vector2d(x, y);

                if (map.isWater(map.getTileAt(position))) {
                    System.out.print("  "); // tile of state WATER signature
                }
                else if (map.isOccupied(position)) {
                    System.out.print("A "); // animal signature
                }
                else if (map.grassAt(position)) {
                    System.out.print("* "); // grass signature
                }
                else {
                    switch (map.getTileAt(position).getState()) {
                        case PLAINS -> System.out.print(". "); // tile of state PLAINS signature
                        case FOREST -> System.out.print("; "); // tile of state FOREST signature
                    }
                }
            }
            System.out.println();
        }
        System.out.println("Map ID: " + map.getId());
        //System.out.println("Animals: " + map.countAnimals());
        //System.out.println("Grasses: " + map.grasses.size());
        System.out.println("Day: " + day);
        System.out.println("\n\n\n\n\n");
    }
}
