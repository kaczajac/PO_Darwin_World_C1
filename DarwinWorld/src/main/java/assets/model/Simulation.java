package assets.model;

/*
Symulacja każdego dnia składa się z poniższej sekwencji kroków:
    1. Usunięcie martwych zwierzaków z mapy.
    2. Skręt i przemieszczenie każdego zwierzaka.
    3. Konsumpcja roślin, na których pola weszły zwierzaki.
    4. Rozmnażanie się najedzonych zwierzaków znajdujących się na tym samym polu.
    5. Wzrastanie nowych roślin na wybranych polach mapy.
 */

/*
Daną symulację opisuje szereg parametrów:

    1. wysokość i szerokość mapy,
    2. wariant mapy (wyjaśnione w sekcji poniżej),
    3. startowa liczba roślin,
    4. energia zapewniana przez zjedzenie jednej rośliny,
    5. liczba roślin wyrastająca każdego dnia,
    6. wariant wzrostu roślin (wyjaśnione w sekcji poniżej),
    7. startowa liczba zwierzaków,
    8. startowa energia zwierzaków,
    9. energia konieczna, by uznać zwierzaka za najedzonego (i gotowego do rozmnażania),
    10. energia rodziców zużywana by stworzyć potomka,
    11. minimalna i maksymalna liczba mutacji u potomków (może być równa 0),
    12. wariant mutacji (wyjaśnione w sekcji poniżej),
    13. długość genomu zwierzaków,
    14. wariant zachowania zwierzaków (wyjaśnione w sekcji poniżej).
 */

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable{
    private final WorldMap map;
    private final List<Animal> animals = new ArrayList<>();

    public Simulation(WorldMap map, int numOfAnimals, int numOfGrass) {
        this.map = map;
        placeGrass(numOfGrass);
        placeAnimals(numOfAnimals);
    }

//// Initializing simulation

    private void placeAnimals(int numOfAnimals) {

        RandomPositionGenerator generator = new RandomPositionGenerator(map, numOfAnimals);
        for (Vector2d position : generator) {
            int energy = (int) (Math.random() * (20 - 10) + 10); // initial energy is in the range of [10, 20] (temporary)
            Animal animal = new Animal(position, energy, 8); // geneLength = 8 is temporary
            map.place(animal);
            animals.add(animal);
        }

    }

    private void placeGrass(int numOfGrass) {

        RandomPositionGenerator generator = new RandomPositionGenerator(map, numOfGrass);
        for (Vector2d position : generator) {
            TileState state = map.getTileAt(position).getState();
            try {
                switch(state) {
                    case PLAINS -> {
                        if (Math.random() <= 0.2) map.place(new Grass(position));
                    }
                    case FOREST -> {
                        if (Math.random() <= 0.8) map.place(new Grass(position));
                    }
                    case WATER -> {}
                    default -> throw new IllegalStateException("Unexpected value: " + state);
                }
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
            }
        }

    }

////     

    @Override
    public void run() {
        map.drawMap();
    }
}
