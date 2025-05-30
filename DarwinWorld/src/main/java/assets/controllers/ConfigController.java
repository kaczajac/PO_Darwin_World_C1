package assets.controllers;

import assets.Simulation;
import assets.SimulationThread;
import assets.model.contract.Controller;
import assets.model.enums.WorldMapType;
import assets.model.exceptions.IllegalMapElementConfig;
import assets.model.exceptions.IllegalMapSettingsException;
import assets.model.map.AbstractWorldMap;
import assets.model.records.WorldMapSettings;
import assets.model.records.SimulationConfig;
import assets.model.util.CSVManager;
import assets.model.util.WorldMapBuilder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.security.InvalidKeyException;
import java.util.Map;

public class ConfigController implements Controller {

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
    @FXML public TextField minAnimalMutationField;
    @FXML public TextField maxAnimalMutationField;
    @FXML public TextField animalMutationChanceField;

    @FXML public RadioButton defaultMapButton;
    @FXML public RadioButton waterMapButton;

    @FXML private Button startSimulationButton;
    @FXML private Button importConfigButton;
    @FXML private Button exportConfigButton;

    @FXML private CheckBox csvFileWriterBox;

    @FXML
    public void initialize() {

        startSimulationButton.setOnAction(event -> {
            try {
                SimulationConfig config = configParser();
                validateConfig(config);
                startSimulation(config);
            } catch (IOException | IllegalMapSettingsException | IllegalMapElementConfig e) {
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

        exportConfigButton.setOnAction(event -> {
            try {
                exportConfigToAFile();
            } catch (IOException e) {
                System.out.println("Error occurred when exporting config to a file:  " + e.getMessage());
            }

        });

        importConfigButton.setOnAction(event -> {
            try {
                importConfigFromAFile();
            } catch (IOException | InvalidKeyException e) {
                System.out.println("Error occurred when importing a config file:  " + e.getMessage());
            }
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
        stage.setOnCloseRequest(event -> thread.terminate());
        stage.show();

    }

//// Helper functions

    private SimulationController setupController(SimulationConfig config, SimulationThread thread) {
        SimulationController controller = new SimulationController(config, thread);

        if (csvFileWriterBox.isSelected()) {
            controller.setExportToCsv(true);
            controller.setCsvFile(new File("src/main/resources/logs/" + config.map().getId()));
        }
        else {
            controller.setExportToCsv(false);
        }

        return controller;
    }

    private FXMLLoader setupFXMLLoader(SimulationController controller) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulation-window.fxml"));
        loader.setController(controller);

        return loader;
    }

    private Stage setupNewStage(BorderPane viewRoot) {
        Stage stage = new Stage();
        Scene scene = new Scene(viewRoot);

        stage.setTitle("Darwin World - Simulation");
        stage.setScene(scene);
        stage.setResizable(false);
        
        return stage;
    }


    private SimulationConfig configParser() throws IllegalMapSettingsException {

        int mapHeight = Integer.parseInt(mapHeightField.getText());
        int mapWidth = Integer.parseInt(mapWidthField.getText());
        WorldMapType mapType = waterMapButton.isSelected() ? WorldMapType.WATER : WorldMapType.DEFAULT;
        double waterLevel = Double.parseDouble(mapWaterLevelField.getText());
        int mapFlowDuration = Integer.parseInt(mapFlowsDurationField.getText());

        int animalsOnStartup = Integer.parseInt(animalStartNumberField.getText());
        int animalStartEnergy = Integer.parseInt(animalStartEnergyField.getText());
        int animalGenomeLength = Integer.parseInt(animalGenomeLengthField.getText());
        int animalMinFedEnergy = Integer.parseInt(animalMinFedEnergyField.getText());
        int animalBirthCost = Integer.parseInt(animalBirthCostField.getText());
        int animalMinMutations = Integer.parseInt(minAnimalMutationField.getText());
        int animalMaxMutations = Integer.parseInt(maxAnimalMutationField.getText());
        float animalMutationChance = Float.parseFloat(animalMutationChanceField.getText());

        int grassDailyGrow = Integer.parseInt(grassDailyField.getText());
        int grassEnergy = Integer.parseInt(grassEnergyField.getText());


        WorldMapSettings mapSettings = new WorldMapSettings(mapHeight, mapWidth, mapType, waterLevel);
        AbstractWorldMap map = new WorldMapBuilder().changeSettings(mapSettings).build();

        return new SimulationConfig(map, mapFlowDuration, grassDailyGrow,
                grassEnergy, animalsOnStartup, animalStartEnergy, animalGenomeLength,
                animalMinFedEnergy, animalBirthCost, animalMinMutations, animalMaxMutations, animalMutationChance);

    }

    private void validateConfig(SimulationConfig config) throws IllegalMapElementConfig {

        if ((config.grassDaily() < 0)
                || (config.grassEnergy() < 0)
                || (config.animalStartEnergy() < 0)
                || (config.animalGenomeLength() < 0)
                || (config.animalBirthCost() < 0)
                || (config.animalMinMutations() < 0)
                || (config.animalMaxMutations() < 0)) throw new IllegalMapElementConfig();

    }

    private void exportConfigToAFile() throws IOException{

        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialDirectory(new File("src/main/resources/configs/"));

        File fileName = fileChooser.showSaveDialog(exportConfigButton.getScene().getWindow());
        if (fileName == null) return;

        CSVManager.writeConfigFile(fileName, this);
        System.out.println("Config file saved successfully");

    }

    private void importConfigFromAFile() throws IOException, InvalidKeyException{

        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialDirectory(new File("src/main/resources/configs/"));

        File fileName = fileChooser.showOpenDialog(importConfigButton.getScene().getWindow());
        if (fileName == null) return;

        Map<String, String> configParameters = CSVManager.readConfigFile(fileName);
        renderConfigParameters(configParameters);
        System.out.println("Config file imported successfully");

    }

    private void renderConfigParameters(Map<String, String> params) throws InvalidKeyException{

        for (Map.Entry<String, String> entry : params.entrySet()) {

            switch (entry.getKey()) {
                case "Parameter" -> {}
                case "MapHeight" -> mapHeightField.setText(entry.getValue());
                case "MapWidth" -> mapWidthField.setText(entry.getValue());
                case "MapWaterLevel" -> mapWaterLevelField.setText(entry.getValue());
                case "MapFlowsDuration" -> mapFlowsDurationField.setText(entry.getValue());
                case "AnimalStartNumber" -> animalStartNumberField.setText(entry.getValue());
                case "AnimalStartEnergy" -> animalStartEnergyField.setText(entry.getValue());
                case "AnimalGenomeLength" -> animalGenomeLengthField.setText(entry.getValue());
                case "AnimalMinFedEnergy" -> animalMinFedEnergyField.setText(entry.getValue());
                case "AnimalBirthCost" -> animalBirthCostField.setText(entry.getValue());
                case "GrassDaily" -> grassDailyField.setText(entry.getValue());
                case "GrassEnergy" -> grassEnergyField.setText(entry.getValue());
                case "MapType" -> {
                    if (entry.getValue().equals("WATER")) {
                        waterMapButton.setSelected(true);
                    } else {
                        defaultMapButton.setSelected(true);
                    }
                }
                case "MinAnimalMutations" -> minAnimalMutationField.setText(entry.getValue());
                case "MaxAnimalMutations" -> maxAnimalMutationField.setText(entry.getValue());
                case "AnimalMutationChance" -> animalMutationChanceField.setText(entry.getValue());
                default -> throw new InvalidKeyException();
            }

        }


    }
}
