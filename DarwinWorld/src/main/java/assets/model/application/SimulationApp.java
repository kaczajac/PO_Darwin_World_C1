package assets.model.application;

import assets.SimulationManager;
import assets.model.contract.MapChangeListener;
import assets.model.map.AbstractMap;
import assets.model.util.ConsoleMapPrinter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SimulationApp implements MapChangeListener {

    @FXML
    private GridPane mapGrid;

    public void launchSimulation() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("simulation-window.fxml"));
        BorderPane viewRoot = loader.load();

        Scene scene = new Scene(viewRoot, 800, 600);
        Stage stage = new Stage();
        stage.setTitle("Simulation Window");
        stage.setScene(scene);
        stage.show();
    }

    public void drawMap(BaseMap map){

    }

    @Override
    public void mapChanged(AbstractMap map, int day) {
        drawMap();
    }
}
