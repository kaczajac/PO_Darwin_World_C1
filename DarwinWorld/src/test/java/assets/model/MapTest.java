package assets.model;

import assets.model.enums.WorldMapType;
import assets.model.enums.TileState;
import assets.model.exceptions.IllegalPositionException;
import assets.model.map.DefaultWorldMap;
import assets.model.map.WaterWorldMap;
import assets.model.mapelement.Animal;
import assets.model.mapelement.Grass;
import assets.model.records.WorldMapSettings;
import assets.model.records.SimulationConfig;
import assets.model.records.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MapTest {

    @Test
    void testMapCreationWithSettings() {
        WorldMapSettings settings = new WorldMapSettings(10, 10, WorldMapType.DEFAULT, 0.0);
        DefaultWorldMap map = new DefaultWorldMap(settings);

        assertEquals(10, map.getWidth(), "Map width should be 10.");
        assertEquals(10, map.getHeight(), "Map height should be 10.");

        assertEquals(WorldMapType.DEFAULT, settings.mapType(), "Map type should be DEFAULT.");
    }

    @Test
    void testAnimalPlacementOnInvalidTile() {
        WorldMapSettings settings = new WorldMapSettings(10, 10, WorldMapType.WATER, 0.3);
        WaterWorldMap map = new WaterWorldMap(settings);
        int x = (int) (Math.random() * 10);
        int y = (int) (Math.random() * 10);
        Vector2d position = new Vector2d(x, y);
        Tile tile = map.getTileAt(position);
        boolean isWater = tile.getState() == TileState.WATER;
        Animal animal = new Animal(position, 100, 8);

        try {
            if (isWater) {
                map.place(animal);
                fail("Animal should not be placed on water!");
            } else {
                map.place(animal);
                assertEquals(position, animal.getPosition(), "Animal should be placed on a valid tile.");
            }
        } catch (IllegalPositionException e) {
            if (!isWater) {
                fail("Unexpected exception when placing animal on valid tile: " + e.getMessage());
            }
        }
    }

    @Test
    void testAnimalBlockedByTile() {
        WorldMapSettings settings = new WorldMapSettings(10, 10, WorldMapType.WATER, 0.3);
        WaterWorldMap map = new WaterWorldMap(settings);

        Vector2d startPosition = new Vector2d(4,4);
        Animal animal = new Animal(startPosition, 100, 4);
        int[] genes = {0,0,2,0};
        animal.setGenome(genes);

        try {
            map.place(animal);
        } catch (IllegalPositionException e) {
            fail("Animal placement failed: " + e.getMessage());
        }

        Vector2d blockedPosition = new Vector2d(4,5);
        map.setTile(new Tile(TileState.WATER), blockedPosition);
        assertEquals(TileState.WATER,map.getTileAt(blockedPosition).getState(), "Water tile was not placed!");

        map.moveAnimals();
        assertEquals(new Vector2d(4,4) , animal.getPosition(), "Animal should not have moved!");
        map.moveAnimals();
        assertEquals(new Vector2d(4,4) , animal.getPosition(), "Animal should not have moved!");
        map.moveAnimals();
        assertEquals(new Vector2d(5,4) , animal.getPosition(), "Animal should have moved!");
        map.moveAnimals();
        assertEquals(new Vector2d(5,5) , animal.getPosition(), "Animal should have moved!");
    }

    @Test
    void testAnimalCannotBePlacedOnWater() {
        WorldMapSettings settings = new WorldMapSettings(10, 10, WorldMapType.WATER, 0.3);
        WaterWorldMap map = new WaterWorldMap(settings);

        Vector2d position = new Vector2d(5, 5);

        map.setTile(new Tile(TileState.WATER) , position);
        Animal animal = new Animal(position, 100, 8);

        try {
            map.place(animal);
            fail("Animal should not be placed on water.");
        } catch (IllegalPositionException e) {
            assertEquals("Position (5, 5) is not correct", e.getMessage());
        }
    }

    @Test
    void testAnimalCanBePlacedOnEmptyTile() {
        WorldMapSettings settings = new WorldMapSettings(10, 10, WorldMapType.DEFAULT, 0.0);
        DefaultWorldMap map = new DefaultWorldMap(settings);

        Vector2d position = new Vector2d(3, 3);
        Animal animal = new Animal(position, 100, 8);

        try {
            map.place(animal);
        } catch (IllegalPositionException e) {
            fail("Animal should be placed on an empty tile.");
        }

        assertEquals(position, animal.getPosition(), "Animal should be at the specified position.");
    }

    @Test
    void testAnimalEatGrass() {
        WorldMapSettings settings = new WorldMapSettings(10, 10, WorldMapType.DEFAULT, 0.0);
        DefaultWorldMap map = new DefaultWorldMap(settings);

        Vector2d grassPosition = new Vector2d(4, 4);
        Grass grass = new Grass(grassPosition);

        try {
            map.place(grass);
        } catch (IllegalPositionException e) {
            fail("Grass should be placed on an empty tile.");
        }

        Vector2d animalPosition = new Vector2d(4, 4);
        Animal animal = new Animal(animalPosition, 10, 8);

        try {
            map.place(animal);
        } catch (IllegalPositionException e) {
            fail("Animal placement failed: " + e.getMessage());
        }

        int initialEnergy = animal.getEnergy();
        map.consumeGrass(new SimulationConfig(map, 0,0,5,0,
                0,0,0,5,0,0,0));

        assertTrue(animal.getEnergy() > initialEnergy, "Animal should gain energy after eating grass.");
    }

    @Test
    void testAnimalDeathFromLackOfEnergy() {
        WorldMapSettings settings = new WorldMapSettings(10, 10, WorldMapType.DEFAULT, 0.0);
        DefaultWorldMap map = new DefaultWorldMap(settings);

        Vector2d p1 = new Vector2d(6, 6);
        Vector2d p2 = new Vector2d(5, 6);
        Vector2d p3 = new Vector2d(4, 6);
        Animal a1 = new Animal(p1, 0, 8);
        Animal a2 = new Animal(p2, 100, 8);
        Animal a3 = new Animal(p3, 0, 8);

        try {
            map.place(a1);
            map.place(a2);
            map.place(a3);
        } catch (IllegalPositionException e) {
            fail("Animal placement failed: " + e.getMessage());
        }

        map.deleteDeadAnimals(1);
        assertTrue(map.countAnimals() == 1, "Animal should be dead and removed from the map.");
    }

    @Test
    void placeTwoAnimalsInTheSameSpot(){
        WorldMapSettings settings = new WorldMapSettings(10, 10, WorldMapType.DEFAULT, 0.0);
        DefaultWorldMap map = new DefaultWorldMap(settings);
        Vector2d position = new Vector2d(4, 4);
        Animal a1 = new Animal(position, 20, 8);
        Animal a2 = new Animal(position, 20, 8);
        try{
            map.place(a1);
            map.place(a2);
        }catch (IllegalPositionException e){
            fail("Animal placement failed: " + e.getMessage());
        }
        assertEquals(2 , map.countAnimals(), "Both animals are expected to spawn.");
    }

}
