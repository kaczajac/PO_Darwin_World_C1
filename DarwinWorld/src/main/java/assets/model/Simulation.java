package assets.model;

public class Simulation implements Runnable{
    private final WorldMap map;
    private final MapConfig config;
    private final Scoreboard scoreboard = new Scoreboard();

    private int day = 0;
    private boolean simulationIsRunning = true;
    private final SimulationManager simulationManager;

    public Simulation(MapConfig config, SimulationManager simulationManager, ConsoleMapPrinter cmp) {
        this.map = new WorldMap(config.mapHeight(), config.mapWidth(), config.mapWaterLevel());
        this.config = config;
        this.simulationManager = simulationManager;

        map.addObserver(cmp);
        map.placeAnimals(config);
        map.placeGrasses(config.grassDaily());
        updateScoreboard();
    }

////

    @Override
    public void run() {

        while (simulationIsRunning && !Thread.currentThread().isInterrupted()) {

            try {
                map.sendMapChanges(day);
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }

            simulationIsRunning = map.checkSimulationEnd();

            if (flowCycleHasPassed()) {
                map.triggerFlow();
            }

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

            // New day setup
            day++;
            map.updateAnimalEnergy();
            updateScoreboard();
        }

        if(!Thread.currentThread().isInterrupted()){
            simulationManager.removeSimulation(this);
        }

    }

////

    private boolean flowCycleHasPassed() {

        int cycle = config.mapFlowsDuration();

        if (cycle == 0) return false;
        else return (day % cycle) == 0;

    }

    private void updateScoreboard() {
        scoreboard.updateStatistics(map.countAnimals(),
                                    map.countGrasses(),
                                    map.countEmptyPositions(),
                                    map.calculateAverageEnergy(),
                                    map.calculateAverageLifeTime(),
                                    map.calculateAverageNumOfChildren());
    }
}
