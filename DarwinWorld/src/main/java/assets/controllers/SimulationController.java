package assets.controllers;

import assets.SimulationThread;
import assets.model.MapElementBox;
import assets.model.Vector2d;
import assets.model.contract.MapChangeListener;
import assets.model.map.AbstractMap;
import assets.model.records.SimulationConfig;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.*;


public class SimulationController implements MapChangeListener {

    private static final double GRID_SIZE = 500;
    private final double CELL_SIZE;

    private final SimulationThread thread;
    private final SimulationConfig config;

    @FXML
    private GridPane mapGrid;

    @FXML
    private Button pauseButton;


    public SimulationController(SimulationConfig config, SimulationThread thread) {

        this.CELL_SIZE = GRID_SIZE / config.map().getHeight();
        this.config = config;
        this.thread = thread;

        config.map().addObserver(this);
    }

////

    @FXML
    private void initialize() {

        adjustGridPane();

        pauseButton.setOnAction(event -> {
            if (thread.isRunning()) {
                thread.pause();
                pauseButton.setText("Resume");
            } else {
                thread.revive();
                pauseButton.setText("Pause");
            }
        });

    }

    @Override
    public void mapChanged(AbstractMap map) {

        Platform.runLater(() -> drawMap(map));

    }

    private void drawMap(AbstractMap map) {

        mapGrid.getChildren().clear();

        int rows = map.getHeight();
        int cols = map.getWidth();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                Vector2d position = new Vector2d(col, row);
                MapElementBox box = new MapElementBox(map, position, CELL_SIZE);
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

}
