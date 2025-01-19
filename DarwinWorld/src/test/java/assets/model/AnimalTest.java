package assets.model;

import assets.Simulation;
import assets.model.enums.MapType;
import assets.model.map.AbstractMap;
import assets.model.map.DefaultMap;
import assets.model.mapelement.Animal;
import assets.model.records.MapSettings;
import assets.model.records.SimulationConfig;
import assets.model.records.Vector2d;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AnimalTest {

    @Test
    void GeneInheritanceTest(){
        int[] genes1 = {1,4,3,6,2,3,1};
        int[] genes2 = {2,4,1,1,6,7};
        Animal a1 = new Animal(new Vector2d(0,0), 50, 6);
        Animal a2 = new Animal(new Vector2d(0,0), 100, 6);
        a1.setGenome(genes1);
        a2.setGenome(genes2);
        assertEquals(6 , a1.getGenome().length);
        Animal a3 = new Animal(new Vector2d(0,0), 50, 6);
        a3.setBirthValues(a1,a2,3, null);
        int[] expected = {2,4,1,1,2,3};
        int[] animalGenome = a3.getGenome();
        for(int i = 0; i < animalGenome.length; i++){
            assertEquals(expected[i] , animalGenome[i]);
        }
    }

    @Test
    void movementTest() {
        MapSettings mapSettings = new MapSettings(10, 10, MapType.DEFAULT, 0d);
        AbstractMap map = new DefaultMap(mapSettings);
        SimulationConfig simulationConfig = new SimulationConfig(
                map, 8, 8, 0, 0, 400, 6, 10, 20, 0, 0, 0);
        Simulation simulation = new Simulation(simulationConfig);

        Animal animal = new Animal(new Vector2d(2, 2), 180, 6);
        int[] genes = {0, 0, 2, 4, 5, 6};
        animal.setGenome(genes);

        try {
            map.place(animal);
        } catch (Exception e) {
            fail("Animal was not placed on the map.");
        }

        animal.move(map);
        assertEquals(new Vector2d(2, 3), animal.getPosition(), "Animal did not move correctly.");
        animal.move(map);
        assertEquals(new Vector2d(2, 4), animal.getPosition(), "Animal did not move correctly.");
        animal.move(map);
        assertEquals(new Vector2d(3, 4), animal.getPosition(), "Animal did not move correctly.");
        animal.move(map);
        assertEquals(new Vector2d(3, 3), animal.getPosition(), "Animal did not move correctly.");
        animal.move(map);
        assertEquals(new Vector2d(2, 2), animal.getPosition(), "Animal did not move correctly.");
        animal.move(map);
        assertEquals(new Vector2d(1, 2), animal.getPosition(), "Animal did not move correctly.");
        animal.move(map);
        assertEquals(new Vector2d(1, 3), animal.getPosition(), "Animal did not move correctly.");
    }


    @Test
    void MutationTest() {
        int[] genes = {1, 2, 3, 4, 5, 6, 7, 0};
        Animal animal = new Animal(new Vector2d(0, 0), 100, 8);
        animal.setGenome(genes);

        SimulationConfig config = new SimulationConfig(null, 8, 8, 0,
                0, 400, 6, 10, 20, 3, 5, 1); // 10% szansa na mutację, 1-3 mutacje.

        animal.mutateGenes(config);
        int[] mutatedGenome = animal.getGenome();
        assertEquals(8, mutatedGenome.length, "Genome length should be unchanged.");

        boolean mutated = false;
        for (int i = 0; i < genes.length; i++) {
            if (genes[i] != mutatedGenome[i]) {
                mutated = true;
                break;
            }
        }
        assertTrue(mutated, "At least one gene should be changed.");
    }

    @Test
    void geneShiftMutationTest() {
        int[] genes = {1, 2, 3, 4, 5, 6, 7, 0};
        Animal animal = new Animal(new Vector2d(0, 0), 100, genes.length);
        animal.setGenome(genes);
        animal.geneShiftMutation();
        int[] mutatedGenome = animal.getGenome();

        assertEquals(genes.length , mutatedGenome.length, "Genome length should be unchanged.");
        boolean mutated = false;
        for(int i = 0; i < genes.length; i++){
            if(genes[i] != mutatedGenome[i]) mutated = true;
            if(mutatedGenome[i] < 0 || mutatedGenome[i] > 7) fail("Gene value: " + mutatedGenome[i] + " - Genomes should have values between 0 and 7.");
        }
        assertTrue(mutated, "At least one gene should be changed.");
    }

    @Test
    void geneShuffleMutationTest() {
        int[] genes = {1, 2, 3, 4, 5, 6, 7, 0};
        Animal animal = new Animal(new Vector2d(0, 0), 100, genes.length);
        animal.setGenome(genes);
        animal.geneShuffleMutation();
        int[] mutatedGenome = animal.getGenome();

        assertEquals(genes.length, mutatedGenome.length, "Genome length should remain unchanged.");

        boolean mutated = false;
        for (int i = 0; i < genes.length; i++) {
            if (genes[i] != mutatedGenome[i]) mutated = true;
            if (mutatedGenome[i] < 0 || mutatedGenome[i] > 7) {
                fail("Genes should have values between 0 and 7.");
            }
        }

        assertTrue(mutated, "At least one gene should have been shuffled.");
    }

    @Test
    void movementBoundaryTest() {
        MapSettings mapSettings = new MapSettings(5, 5, MapType.DEFAULT, 0d);
        AbstractMap map = new DefaultMap(mapSettings);

        Animal animal = new Animal(new Vector2d(4, 2), 100, 6);
        int[] genes = {2,0,0,0,0,0};
        animal.setGenome(genes);

        try {
            map.place(animal);
        } catch (Exception e) {
            fail("Animal could not be placed.");
        }

        animal.move(map);
        assertEquals(new Vector2d(0, 2), animal.getPosition(), "Animal should wrap around to the left edge of the map.");
    }

    @Test
    void ageAndDescendantsUpdateTest() {
        Animal parent = new Animal(new Vector2d(0, 0), 100, 8);
        Animal child1 = new Animal(new Vector2d(1, 1), 100, 8);
        Animal child2 = new Animal(new Vector2d(2, 2), 100, 8);

        parent.addNewChild(child1);
        parent.addNewChild(child2);

        parent.updateAge();
        parent.updateDescendants();

        assertEquals(1, parent.getAge(), "Age should increment by 1.");
        assertEquals(2, parent.getNumberOfChildren(), "Number of direct children should be 2.");
        assertEquals(2, parent.getNumberOfDescendants(), "Total descendants count should be 2.");
    }

    @Test
    void grassEatingTest() {
        Animal animal = new Animal(new Vector2d(0, 0), 50, 8);
        int grassEnergy = 10;

        int initialGrassEaten = animal.getGrassEaten();
        int initialEnergy = animal.getEnergy();

        animal.updateGrassEaten();
        animal.addEnergy(grassEnergy);

        assertEquals(initialGrassEaten + 1, animal.getGrassEaten(), "Grass eaten count should increase by 1.");
        assertEquals(initialEnergy + grassEnergy, animal.getEnergy(), "Energy should increase by the grass energy value.");
    }

    @Test
    void energyDrainAndDeathTest() {
        int initialEnergy = 10;
        int energyCostPerMove = 2;
        Animal animal = new Animal(new Vector2d(2, 2), initialEnergy, 8);

        // Simulating animal energy usage
        while (animal.getEnergy() > 0) {
            animal.useEnergy(energyCostPerMove);
        }

        // Check if the animal is dead and energy is zero
        assertEquals(0, animal.getEnergy(), "Energy should be zero after using all energy.");
        assertEquals(-1, animal.getDeathDay(), "Death day should be unset if not set manually.");
        animal.setDeathDay(5); // Simulate the animal dying on day 5
        assertEquals(5, animal.getDeathDay(), "Death day should be correctly set after death.");
    }





}
