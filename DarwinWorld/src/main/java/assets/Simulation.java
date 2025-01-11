package assets;

import assets.model.*;
import assets.model.contract.MapChangeListener;
import assets.model.enums.MapType;
import assets.model.exceptions.IllegalMapSettingsException;
import assets.model.map.WaterMap;
import assets.model.map.AbstractMap;
import assets.model.records.SimulationConfig;
import assets.model.util.ConsoleMapPrinter;
import assets.model.util.MapBuilder;

public class Simulation implements Runnable{
    private final AbstractMap map;
    private final SimulationConfig config;
    private final Scoreboard scoreboard = new Scoreboard();

    private int day = 0;
    private boolean simulationIsRunning = true;
    private final SimulationManager simulationManager;

    public Simulation(SimulationConfig config, SimulationManager simulationManager, MapChangeListener mapListener) throws IllegalMapSettingsException{
        this.map = new MapBuilder().changeSettings(config.mapSettings()).build();
        this.config = config;
        this.simulationManager = simulationManager;

        map.addObserver(mapListener);
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

            // Inflows and outflows handling if the map is 'WaterMap'
            if (flowCycleHasPassed()) {
                ( (WaterMap) map ).triggerFlow();
            }

            // Simulation procedure
            map.deleteDeadAnimals(day);
            map.moveAnimals();
            map.consumeGrass(config);
            map.breedAnimals(config, day);
            map.placeGrasses(config.grassDaily());

            // New day setup
            day++;
            map.updateAnimalStatistics();
            updateScoreboard();
        }

        if(!Thread.currentThread().isInterrupted()){
            simulationManager.removeSimulation(this);
        }

    }

////

    private boolean flowCycleHasPassed() {
        if (config.mapSettings().mapType() != MapType.WATER) return false;

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
