package assets.controllers;

import assets.SimulationThread;
import assets.model.MapElementBox;
import assets.model.Scoreboard;
import assets.model.Vector2d;
import assets.model.contract.MapChangeListener;
import assets.model.map.AbstractMap;
import assets.model.mapelement.Animal;
import assets.model.mapelement.MapElement;
import assets.model.records.SimulationConfig;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.util.Arrays;


public class SimulationController implements MapChangeListener {

    private static final double GRID_SIZE = 500;
    private final double CELL_SIZE;

    private final SimulationThread thread;
    private final SimulationConfig config;

    private MapElementBox selectedAnimalBox = null;

    @FXML private GridPane mapGrid;

    @FXML private Label simulationLabel;

    //// Scoreboard labels

    @FXML private Label numOfAnimals;
    @FXML private Label numOfGrasses;
    @FXML private Label numOfEmptyPositions;
    @FXML private Label averageAnimalEnergy;
    @FXML private Label averageLifeTime;
    @FXML private Label averageChildren;
    @FXML private Label dayCounter;

    //// Animal statistics labels

    @FXML private Label animalGenome;
    @FXML private Label animalGene;
    @FXML private Label animalEnergy;
    @FXML private Label animalGrass;
    @FXML private Label animalChildren;
    @FXML private Label animalDescendants;
    @FXML private Label animalAge;
    @FXML private Label animalDeathDay;
    @FXML private Label animalBirthDay;

    //// Other Controls

    @FXML private Button pauseButton;
    @FXML private Button unfollowAnimalButton;

    ////

    public SimulationController(SimulationConfig config, SimulationThread thread) {

        this.CELL_SIZE = GRID_SIZE / Math.max(config.map().getHeight(), config.map().getWidth());
        this.config = config;
        this.thread = thread;

        config.map().addObserver(this);
    }

////

    @FXML
    private void initialize() {

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
            selectedAnimalBox = null;
            clearSelectedAnimalStats();
            unfollowAnimalButton.setDisable(true);
        });

    }

    @Override
    public void mapChanged(AbstractMap map) {

        Platform.runLater(() -> {
            renderScoreboard();
            renderSelectedAnimalStats();
            drawMap(map);
        });

    }

    private void drawMap(AbstractMap map) {

        mapGrid.getChildren().clear();

        int rows = map.getHeight();
        int cols = map.getWidth();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                Vector2d position = new Vector2d(col, row);
                MapElementBox box = new MapElementBox(map, position, CELL_SIZE);
                configureBox(box);
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
        dayCounter.setText(String.valueOf(board.getDay()));

    }

    private void renderSelectedAnimalStats() {

        if (selectedAnimalBox == null) return;

        Animal selectedAnimal = (Animal) selectedAnimalBox.getMapElement();

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

    private void configureBox(MapElementBox box) {

        if (box.getMapElement() == null) return;

        if (box.equals(selectedAnimalBox)) {
            selectedAnimalBox = box;
            box.markAsSelected();
        }

        if (isAnimal(box.getMapElement())) {

            box.display().setOnMouseClicked(event -> {

                if (selectedAnimalBox != null) {
                    selectedAnimalBox.restoreDefaultBackground(config.map());
                }

                selectedAnimalBox = box;
                selectedAnimalBox.markAsSelected();
                renderSelectedAnimalStats();
                unfollowAnimalButton.setDisable(false);
            });

        }

    }

    private boolean isAnimal(MapElement element) {
        return element.getClass().isAssignableFrom(Animal.class);
    }



}
