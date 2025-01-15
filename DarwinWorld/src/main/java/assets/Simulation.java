package assets;

import assets.model.*;
import assets.model.map.WaterMap;
import assets.model.map.AbstractMap;
import assets.model.records.SimulationConfig;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Simulation implements Runnable{

    private final AbstractMap map;
    private final SimulationConfig config;
    private final Scoreboard scoreboard = new Scoreboard();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss.SS");

    private int day = 0;
    private boolean simulationIsRunning = true;
    private boolean suspendedThread = false;

    public Simulation(SimulationConfig config) {
        this.map = config.map();
        this.config = config;

        map.placeAnimals(config);
        map.placeGrasses(config.grassDaily());
        updateScoreboard();
    }

////

    @Override
    public void run() {

        while (simulationIsRunning) {

            try {
                map.sendMapChanges();
                Thread.sleep(200);
                synchronized (this) {
                    while (suspendedThread) {
                        wait();
                    }
                }
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
                if (!simulationIsRunning) return;
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

    }

//// Helpers

    private boolean flowCycleHasPassed() {
        if (isNotWaterMap()) return false;

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

    private boolean isNotWaterMap() {
        return !map.getClass().isAssignableFrom(WaterMap.class);
    }

//// Thread management

    public synchronized void pause() {
        suspendedThread = true;
        System.out.println(LocalDateTime.now().format(formatter) + " | " + map.getId() + " | " + "Pausing...");
    }

    public synchronized void revive() {
        suspendedThread = false;
        System.out.println(LocalDateTime.now().format(formatter) + " | " + map.getId() + " | " + "Resuming...");
        this.notify();
    }

    public void terminate() {
        simulationIsRunning = false;
        suspendedThread = true;
        System.out.println(LocalDateTime.now().format(formatter) + " | " + map.getId() + " | " + "Simulation has been terminated");
    }

    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }

}
