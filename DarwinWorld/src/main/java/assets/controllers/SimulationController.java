package assets.controllers;

import assets.SimulationThread;
import assets.model.WorldElementBox;
import assets.model.Scoreboard;
import assets.model.contract.Controller;
import assets.model.mapelement.WorldElement;
import assets.model.records.Vector2d;
import assets.model.contract.MapChangeListener;
import assets.model.map.AbstractWorldMap;
import assets.model.mapelement.Animal;
import assets.model.records.SimulationConfig;
import assets.model.util.CSVManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class SimulationController implements MapChangeListener, Controller {

    private static final double GRID_SIZE = 500;
    private final double CELL_SIZE;

    private final SimulationThread thread;
    private final SimulationConfig config;
    private File csvFile;

    private WorldElementBox selectedAnimalBox = null;
    private Set<Vector2d> popularGrassPositions = new HashSet<>();
    private Set<Animal> popularGenomeAnimals = new HashSet<>();

    private boolean exportToCsv;
    private boolean showPopularGrassPositions = false;
    private boolean showPopularGenomeAnimals = false;

    @FXML private GridPane mapGrid;

    @FXML private Label simulationLabel;

    //// Scoreboard labels

    @FXML public Label numOfAnimals;
    @FXML public Label numOfGrasses;
    @FXML public Label numOfEmptyPositions;
    @FXML public Label averageAnimalEnergy;
    @FXML public Label averageLifeTime;
    @FXML public Label averageChildren;
    @FXML public Label mostPopularGenome;
    @FXML public Label dayCounter;

    //// Animal statistics labels

    @FXML public Label animalGenome;
    @FXML public Label animalGene;
    @FXML public Label animalEnergy;
    @FXML public Label animalGrass;
    @FXML public Label animalChildren;
    @FXML public Label animalDescendants;
    @FXML public Label animalAge;
    @FXML public Label animalDeathDay;
    @FXML public Label animalBirthDay;

    //// Other Controls

    @FXML private Button pauseButton;
    @FXML private Button unfollowAnimalButton;
    @FXML private Button popularGrassPositionsButton;
    @FXML private Button popularGenomeAnimalsButton;

    ////

    public SimulationController(SimulationConfig config, SimulationThread thread) {

        this.CELL_SIZE = GRID_SIZE / Math.max(config.map().getHeight(), config.map().getWidth());
        this.config = config;
        this.thread = thread;

        config.map().addObserver(this);

    }

////

    @FXML
    public void initialize() {

        adjustGridPane();
        renderSimulationNumber();

        pauseButton.setOnAction(event -> {
            if (thread.isRunning()) {
                thread.pause();
                pauseButton.setText("Resume");
            } else {
                thread.revive();
                pauseButton.setText("Pause");
            }
        });

        unfollowAnimalButton.setOnAction(event -> {
            selectedAnimalBox.restoreDefaultBackground(config.map());
            clearSelectedAnimalStats();
            selectedAnimalBox = null;
            unfollowAnimalButton.setDisable(true);
            drawMap(config.map());
        });

        popularGrassPositionsButton.setOnAction(event -> {
            showPopularGrassPositions = !showPopularGrassPositions;
            popularGrassPositionsButton.setText(showPopularGrassPositions
                    ? "Hide Popular Grass Positions"
                    : "Show Popular Grass Positions");
            popularGrassPositions = config.map().getPopularGrassPositions();
            drawMap(config.map());
        });

        popularGenomeAnimalsButton.setOnAction(event -> {
            showPopularGenomeAnimals = !showPopularGenomeAnimals;
            popularGenomeAnimalsButton.setText(showPopularGenomeAnimals
                    ? "Hide Popular Genome Animals"
                    : "Show Popular Genome Animals");
            popularGenomeAnimals = config.map().getPopularGenomeAnimals();
            drawMap(config.map());
        });

    }

    @Override
    public void mapChanged(AbstractWorldMap map) {

        if (showPopularGrassPositions) {
            popularGrassPositions = map.getPopularGrassPositions();
        }

        if (showPopularGenomeAnimals) {
            popularGenomeAnimals = map.getPopularGenomeAnimals();
        }

        Platform.runLater(() -> {
            renderScoreboard();
            renderSelectedAnimalStats();
            drawMap(map);

            if (exportToCsv) {
                try {
                    CSVManager.logChangesToCsvFile(csvFile, this);
                } catch (IOException e) {
                    System.out.println("Error occurred when logging changes to a CSV file: " + e.getMessage());
                }
            }
        });

    }

    private void drawMap(AbstractWorldMap map) {

        mapGrid.getChildren().clear();

        int rows = map.getHeight();
        int cols = map.getWidth();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                Vector2d position = new Vector2d(col, row);
                WorldElementBox box = new WorldElementBox(map, position, CELL_SIZE);
                handleAnimalEvents(box);
                handleGrassEvents(box);
                mapGrid.add(box.display(), col, (rows - 1 - row));

            }
        }

    }


//// Helper functions

    private void adjustGridPane() {

        int rows = config.map().getHeight();
        int cols = config.map().getWidth();

        // Ustawianie wysokości wierszy na równą wielkość komórki
        for (int i = 0; i < rows; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setMinHeight(CELL_SIZE);
            rowConstraints.setPrefHeight(CELL_SIZE);
            rowConstraints.setMaxHeight(CELL_SIZE);
            mapGrid.getRowConstraints().add(rowConstraints);
        }

        // Ustawianie szerokości kolumn na równą wielkość komórki
        for (int i = 0; i < cols; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setMinWidth(CELL_SIZE);
            colConstraints.setPrefWidth(CELL_SIZE);
            colConstraints.setMaxWidth(CELL_SIZE);
            mapGrid.getColumnConstraints().add(colConstraints);
        }

    }

    private void renderSimulationNumber() {
        simulationLabel.setText("Simulation ID : " + config.map().getId());
    }

    private void renderScoreboard() {

        Scoreboard board = thread.getSimulationScoreboard();

        numOfAnimals.setText(String.valueOf(board.getNumOfAnimals()));
        numOfGrasses.setText(String.valueOf(board.getNumOfGrass()));
        numOfEmptyPositions.setText(String.valueOf(board.getNumOfEmptyPositions()));
        averageAnimalEnergy.setText(String.valueOf(board.getAverageAnimalEnergy()));
        averageLifeTime.setText(String.valueOf(board.getAverageLifeTime()));
        averageChildren.setText(String.valueOf(board.getAverageNumOfChildren()));
        mostPopularGenome.setText(board.getMostPopularGenome());
        dayCounter.setText(String.valueOf(board.getDay()));

    }

    private void renderSelectedAnimalStats() {

        if (selectedAnimalBox == null) return;

        Animal selectedAnimal = (Animal) selectedAnimalBox.getWorldElement();

        animalGenome.setText(Arrays.toString(selectedAnimal.getGenome()));
        animalGene.setText(String.valueOf(selectedAnimal.getGene()));
        animalEnergy.setText(String.valueOf(selectedAnimal.getEnergy()));
        animalGrass.setText(String.valueOf(selectedAnimal.getGrassEaten()));
        animalChildren.setText(String.valueOf(selectedAnimal.getNumberOfChildren()));
        animalDescendants.setText(String.valueOf(selectedAnimal.getNumberOfDescendants()));
        animalAge.setText(String.valueOf(selectedAnimal.getAge()));
        animalDeathDay.setText(selectedAnimal.getDeathDay() == -1 ? "-" : String.valueOf(selectedAnimal.getDeathDay()));
        animalBirthDay.setText(String.valueOf(selectedAnimal.getBirthDay()));

    }

    private void clearSelectedAnimalStats() {

        animalGenome.setText("-");
        animalGene.setText("-");
        animalEnergy.setText("-");
        animalGrass.setText("-");
        animalChildren.setText("-");
        animalDescendants.setText("-");
        animalAge.setText("-");
        animalDeathDay.setText("-");
        animalBirthDay.setText("-");

    }

    private void handleAnimalEvents(WorldElementBox box) {

        if (box.containsAnimal()) {

            addHealthBar(box);

            box.display().setOnMouseClicked(event -> {
                if (selectedAnimalBox != null) {
                    selectedAnimalBox.restoreDefaultBackground(config.map());
                }
                selectedAnimalBox = box;
                box.markAsSelectedAnimalBox();
                renderSelectedAnimalStats();
                unfollowAnimalButton.setDisable(false);
                drawMap(config.map());
            });

            if (showPopularGenomeAnimals && popularGenomeAnimals.contains((Animal) box.getWorldElement())) {
                box.markAsPopularGenomeAnimalBox();
            }

            if (box.containsSelectedAnimal(selectedAnimalBox)) {
                selectedAnimalBox = box;
                box.markAsSelectedAnimalBox();
            }

        }

    }

    private void addHealthBar(WorldElementBox box) {

        Line hp = new Line();
        hp.setStartX(CELL_SIZE * 0.4);
        hp.setStroke(getHealthColorBasedOnEnergyLevel(box.getWorldElement()));
        hp.setStrokeWidth(2.5);
        box.display().getChildren().add(hp);
        StackPane.setAlignment(hp, Pos.BOTTOM_CENTER);

    }

    private Color getHealthColorBasedOnEnergyLevel(WorldElement element) {

        Animal animal = (Animal) element;
        if (animal.getEnergy() >= config.animalStartEnergy() * 0.7) return Color.LIGHTGREEN;
        if (animal.getEnergy() >= config.animalStartEnergy() * 0.3) return Color.GOLD;
        else return Color.CRIMSON;

    }

    private void handleGrassEvents(WorldElementBox box) {

        if (showPopularGrassPositions) {

            if (popularGrassPositions.contains(box.getPosition())) {
                box.markAsPopularGrassPositionBox();
            }
        }

    }


    public void setExportToCsv(boolean bool) {
        this.exportToCsv = bool;
    }

    public void setCsvFile(File csvFile) {
        this.csvFile = csvFile;
    }


}
