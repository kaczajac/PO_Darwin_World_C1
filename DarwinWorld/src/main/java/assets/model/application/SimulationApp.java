package assets.model.application;

import assets.SimulationManager;
import assets.model.contract.MapChangeListener;
import assets.model.map.BaseMap;
import assets.model.util.ConsoleMapPrinter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SimulationApp implements MapChangeListener {

    public void launchSimulation() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("simulation-window.fxml"));
        BorderPane viewRoot = loader.load();

        Scene scene = new Scene(viewRoot, 800, 600);
        Stage stage = new Stage();
        stage.setTitle("Simulation Window");
        stage.setScene(scene);
        stage.show();
    }

    public void drawMap(){

    }

    @Override
    public void mapChanged(BaseMap map, int day) {
        drawMap();
    }
}
