package assets.model.application;

import assets.Simulation;
import assets.SimulationManager;
import assets.model.enums.MapType;
import assets.model.exceptions.IllegalMapSettingsException;
import assets.model.records.MapSettings;
import assets.model.records.SimulationConfig;
import assets.model.util.ConsoleMapPrinter;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ConfigurationApp extends Application {

    // FXML Variables
    @FXML public TextField mapHeightField;
    @FXML public TextField mapWidthField;
    @FXML public TextField mapWaterLevelField;
    @FXML public TextField mapFlowsDurationField;
    @FXML public TextField grassDailyField;
    @FXML public TextField grassEnergyField;
    @FXML public TextField animalStartNumberField;
    @FXML public TextField animalStartEnergyField;
    @FXML public TextField animalGenomeLengthField;
    @FXML public TextField animalMinFedEnergyField;
    @FXML public TextField animalBirthCostField;
    @FXML public Button startSimulationButton;

    SimulationManager simulationManager;
    ConsoleMapPrinter consoleMapPrinter;

    @Override
    public void start(Stage stage) throws Exception {
        simulationManager = new SimulationManager();
        consoleMapPrinter = new ConsoleMapPrinter();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("config-window.fxml"));
        loader.setController(this);
        BorderPane viewRoot = loader.load();
        Scene scene = new Scene(viewRoot, 400, 500);  // Określamy rozmiar okna (400x500)
        stage.setTitle("Configuration Window");
        stage.setScene(scene);
        startSimulationButton.setOnAction(e -> startSimulation());
        stage.show();
    }

    // Called when button pressed
    public void startSimulation() {
        int mapHeight = Integer.parseInt(mapHeightField.getText());
        int mapWidth = Integer.parseInt(mapWidthField.getText());
        double waterLevel = Double.parseDouble(mapWaterLevelField.getText());
        int mapFlowDuration = Integer.parseInt(mapFlowsDurationField.getText());
        int grassDailyGrow = Integer.parseInt(grassDailyField.getText());
        int grassEnergy = Integer.parseInt(grassEnergyField.getText());
        int animalsOnStartup = Integer.parseInt(animalStartNumberField.getText());
        int animalStartEnergy = Integer.parseInt(animalStartEnergyField.getText());
        int animalGenomeLength = Integer.parseInt(animalGenomeLengthField.getText());
        int animalMinFedEnergy = Integer.parseInt(animalMinFedEnergyField.getText());
        int animalBirthCost = Integer.parseInt(animalBirthCostField.getText());

        MapSettings mapSettings = new MapSettings(mapHeight, mapWidth, MapType.WATER, waterLevel);
        SimulationConfig simConfig = new SimulationConfig(mapSettings , mapFlowDuration, grassDailyGrow,
                grassEnergy, animalsOnStartup, animalStartEnergy, animalGenomeLength,
                animalMinFedEnergy, animalBirthCost);

        SimulationApp simApp = new SimulationApp();
        try {
            simApp.launchSimulation();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            Simulation sim = new Simulation(simConfig, simulationManager, simApp);
            simulationManager.addAndStartSimulation(sim);
        } catch (IllegalMapSettingsException e) {
            System.out.println("Unable to build a map: " + e.getMessage());
        }

    }
}
