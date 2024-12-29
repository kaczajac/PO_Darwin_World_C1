package assets.model;

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
    private final MapConfig config;

    private int day = 0;
    private boolean simulationIsRunning = true;
    private SimulationManager simulationManager;

    public Simulation(MapConfig config , SimulationManager simulationManager) {
        this.map = new WorldMap(config.mapHeight(), config.mapWidth(), config.mapWaterLevel());
        this.config = config;
        this.simulationManager = simulationManager;

        map.placeAnimals(config);
        map.placeGrasses(config.grassDaily());
    }

////

    @Override
    public void run() {

        while (simulationIsRunning && !Thread.currentThread().isInterrupted()) {
            map.drawMap(day);
            try{
                Thread.sleep(100);
            } catch (InterruptedException e) {}
            simulationIsRunning = map.checkSimulationEnd();

            if (flowCycleHasPassed()) {
                map.triggerFlow();
            }

            day++;

            // 1. Usunięcie martwych zwierzaków z mapy.
            map.deleteDeadAnimals();

            // 2. Skręt i przemieszczenie każdego zwierzaka.
            map.moveAnimals();

            // 3. Konsumpcja roślin, na których pola weszły zwierzaki.
            map.consumeGrass(config);

            // 4. Rozmnażanie zwierząt
            map.breedAnimals(config , day);

            // 5. Wzrastanie nowych roślin na wybranych polach mapy.
            map.placeGrasses(config.grassDaily());
        }

        if(!Thread.currentThread().isInterrupted()){
            simulationManager.removeSimulation(this);
        }

    }

    private boolean flowCycleHasPassed() {

        int cycle = config.mapFlowsDuration();

        if (cycle == 0) return false;
        else return (day % cycle) == 0;

    }
}
