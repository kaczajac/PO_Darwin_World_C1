package assets.model.application;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

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

    @Override
    public void start(Stage stage) throws Exception {
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
        mapHeightField.getText();
        mapWidthField.getText();
        mapWaterLevelField.getText();
        mapFlowsDurationField.getText();
        grassDailyField.getText();
        grassEnergyField.getText();
        animalStartNumberField.getText();
        animalStartEnergyField.getText();
        animalGenomeLengthField.getText();
        animalMinFedEnergyField.getText();
        animalBirthCostField.getText();
    }
}
