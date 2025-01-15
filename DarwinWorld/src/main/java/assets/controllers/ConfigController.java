package assets.controllers;

import assets.Simulation;
import assets.SimulationThread;
import assets.model.enums.MapType;
import assets.model.exceptions.IllegalMapSettingsException;
import assets.model.map.AbstractMap;
import assets.model.records.MapSettings;
import assets.model.records.SimulationConfig;
import assets.model.util.MapBuilder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ConfigController {

    // FXML Variables
    @FXML private TextField mapHeightField;
    @FXML private TextField mapWidthField;
    @FXML private TextField mapWaterLevelField;
    @FXML private TextField mapFlowsDurationField;
    @FXML private TextField grassDailyField;
    @FXML private TextField grassEnergyField;
    @FXML private TextField animalStartNumberField;
    @FXML private TextField animalStartEnergyField;
    @FXML private TextField animalGenomeLengthField;
    @FXML private TextField animalMinFedEnergyField;
    @FXML private TextField animalBirthCostField;

    @FXML private RadioButton defaultMapButton;
    @FXML private RadioButton waterMapButton;
    @FXML private Button startSimulationButton;

    @FXML
    private void initialize() {

        startSimulationButton.setOnAction(event -> {
            try {
                SimulationConfig config = configParser();
                startSimulation(config);
            } catch (IOException | IllegalMapSettingsException e) {
                System.out.println("Problems with initializing a new simulation: " + e.getMessage());
            }

        });

        defaultMapButton.setOnAction(event -> {
            mapWaterLevelField.setText("0.0");
            mapWaterLevelField.setDisable(true);
            mapFlowsDurationField.setText("0");
            mapFlowsDurationField.setDisable(true);
        });

        waterMapButton.setOnAction(event -> {
            mapWaterLevelField.setDisable(false);
            mapFlowsDurationField.setDisable(false);
        });

    }

    private void startSimulation(SimulationConfig config) throws IOException {

        Simulation simulation = new Simulation(config);
        SimulationThread thread = new SimulationThread(simulation);

        SimulationController controller = setupController(config, thread);
        FXMLLoader loader = setupFXMLLoader(controller);
        BorderPane viewRoot = loader.load();
        Stage stage = setupNewStage(viewRoot);

        thread.start();
        stage.setOnCloseRequest(event -> simulation.terminate());
        stage.show();
    }

//// Helper functions

    private SimulationController setupController(SimulationConfig config, SimulationThread thread) {
        return new SimulationController(config, thread);
    }

    private FXMLLoader setupFXMLLoader(SimulationController controller) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulation-window.fxml"));
        loader.setController(controller);

        return loader;
    }

    private Stage setupNewStage(BorderPane viewRoot) {
        Stage stage = new Stage();
        Scene scene = new Scene(viewRoot, 800, 600);

        stage.setTitle("Darwin World - Simulation");
        stage.setScene(scene);
        return stage;
    }


    private SimulationConfig configParser() throws IllegalMapSettingsException {

        int mapHeight = Integer.parseInt(mapHeightField.getText());
        int mapWidth = Integer.parseInt(mapWidthField.getText());
        MapType mapType = waterMapButton.isSelected() ? MapType.WATER : MapType.DEFAULT;
        double waterLevel = Double.parseDouble(mapWaterLevelField.getText());
        int mapFlowDuration = Integer.parseInt(mapFlowsDurationField.getText());

        int animalsOnStartup = Integer.parseInt(animalStartNumberField.getText());
        int animalStartEnergy = Integer.parseInt(animalStartEnergyField.getText());
        int animalGenomeLength = Integer.parseInt(animalGenomeLengthField.getText());
        int animalMinFedEnergy = Integer.parseInt(animalMinFedEnergyField.getText());
        int animalBirthCost = Integer.parseInt(animalBirthCostField.getText());

        int grassDailyGrow = Integer.parseInt(grassDailyField.getText());
        int grassEnergy = Integer.parseInt(grassEnergyField.getText());


        MapSettings mapSettings = new MapSettings(mapHeight, mapWidth, mapType, waterLevel);
        AbstractMap map = new MapBuilder().changeSettings(mapSettings).build();

        return new SimulationConfig(map, mapFlowDuration, grassDailyGrow,
                grassEnergy, animalsOnStartup, animalStartEnergy, animalGenomeLength,
                animalMinFedEnergy, animalBirthCost);

    }
}
