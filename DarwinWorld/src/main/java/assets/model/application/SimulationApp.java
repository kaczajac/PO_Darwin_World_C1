package assets.model.application;

import assets.SimulationManager;
import assets.model.contract.MapChangeListener;
import assets.model.map.AbstractMap;
import assets.model.util.ConsoleMapPrinter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.io.IOException;

public class SimulationApp implements MapChangeListener {

    @FXML
    private GridPane mapGrid;

    @FXML
    public void initialize() {
        // Ta metoda zostanie wywołana po załadowaniu FXML
        System.out.println("mapGrid initialized: " + (mapGrid != null));
    }

    public void launchSimulation() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("simulation-window.fxml"));
        loader.setController(this);
        BorderPane viewRoot = loader.load();

        Scene scene = new Scene(viewRoot, 800, 600);
        Stage stage = new Stage();
        stage.setTitle("Simulation Window");
        stage.setScene(scene);
        stage.show();
    }

    private void adjustGridPane(int width, int height) {
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();

        for (int i = 0; i < width; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setPercentWidth(100.0 / width);
            mapGrid.getColumnConstraints().add(colConstraints);
        }

        for (int i = 0; i < height; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0 / height);
            mapGrid.getRowConstraints().add(rowConstraints);
        }
    }

    public void clearGrid() {
        Platform.runLater(() -> {
            mapGrid.getChildren().clear();
            mapGrid.getColumnConstraints().clear();
            mapGrid.getRowConstraints().clear();
        });
    }

    public void drawMap(AbstractMap map) {
        Platform.runLater(() -> {
            mapGrid.getChildren().clear();

            // Ustawienie stałej wysokości GridPane na 700px
            double gridHeight = 500; // Stała wysokość w px
            double gridWidth = gridHeight; // Ustalamy szerokość na równą wysokości

            int rows = map.getHeight();
            int cols = map.getWidth();

            // Obliczanie wielkości komórki na podstawie stałej wysokości
            double cellSize = gridHeight / rows;

            // Ustawienia dla wierszy i kolumn GridPane
            mapGrid.getRowConstraints().clear();
            mapGrid.getColumnConstraints().clear();

            // Ustawianie wysokości wierszy na równą wielkość komórki
            for (int i = 0; i < rows; i++) {
                RowConstraints rowConstraints = new RowConstraints();
                rowConstraints.setMinHeight(cellSize);
                rowConstraints.setPrefHeight(cellSize);
                rowConstraints.setMaxHeight(cellSize);
                mapGrid.getRowConstraints().add(rowConstraints);
            }

            // Ustawianie szerokości kolumn na równą wielkość komórki
            for (int i = 0; i < cols; i++) {
                ColumnConstraints colConstraints = new ColumnConstraints();
                colConstraints.setMinWidth(cellSize);
                colConstraints.setPrefWidth(cellSize);
                colConstraints.setMaxWidth(cellSize);
                mapGrid.getColumnConstraints().add(colConstraints);
            }

            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    Rectangle cell = new Rectangle(cellSize, cellSize);
                    cell.setFill(Color.GREENYELLOW);
                    cell.setStroke(Color.GREEN);
                    cell.setStrokeWidth(1);

                    StackPane cellContainer = new StackPane();
                    cellContainer.getChildren().add(cell);

                    ImageView imageView = getImageForCell(row, col, map);

                    if (imageView != null) {
                        cellContainer.getChildren().add(imageView);
                    }

                    mapGrid.add(cellContainer, col, (rows - 1 - row));
                }
            }
        });
    }

    private ImageView getImageForCell(int row, int col, AbstractMap map) {
        
        return null;
    }

    public ImageView createImageView(String imagePath, double width, double height) {
        Image image = new Image(getClass().getResource(imagePath).toString());
        ImageView imageView = new ImageView(image);

        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(true);

        return imageView;
    }

    @Override
    public void mapChanged(AbstractMap map, int day) {
        drawMap(map);
    }
}
