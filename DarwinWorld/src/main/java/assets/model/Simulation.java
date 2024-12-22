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

public class Simulation implements Runnable{
    private final WorldMap map;

    public Simulation(WorldMap map, int numOfAnimals, int numOfGrass) {
        this.map = map;
        map.placeGrass(numOfGrass);
        map.placeAnimals(numOfAnimals);
    }

    @Override
    public void run() {
        map.drawMap();
    }
}
