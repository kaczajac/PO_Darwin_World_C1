package assets;

import assets.controllers.ConfigController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class DarwinWorld extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("config-window.fxml"));
        loader.setController(new ConfigController());
        BorderPane viewRoot = loader.load();

        setupConfigStage(primaryStage, viewRoot);
        primaryStage.show();
    }

    private void setupConfigStage(Stage stage, BorderPane viewRoot) {
        Scene scene = new Scene(viewRoot, 400, 600);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setOnCloseRequest(event -> System.exit(0));
        stage.setTitle("Darwin World - Configuration");
    }

}
