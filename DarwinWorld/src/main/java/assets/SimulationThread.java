package assets;


import assets.model.Scoreboard;

public class SimulationThread extends Thread {

    private final Simulation simulation;
    private boolean running = true;

    public SimulationThread(Simulation simulation) {
        super(simulation);
        this.simulation = simulation;
    }

    public boolean isRunning() {
        return this.running;
    }

    public void revive() {
        this.running = true;
        simulation.revive();
    }

    public void pause() {
        this.running = false;
        simulation.pause();
    }

    public void terminate() {
        this.interrupt();
        this.revive();
        simulation.terminate();
    }

    public Scoreboard getSimulationScoreboard() {
        return simulation.getScoreboard();
    }

}
