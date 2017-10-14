package cslmusicmod.stationeditor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        primaryStage.getIcons().add(new Image("/cslmusicmod/stationeditor/icon.png"));
        Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("CSL Music Mod Station Editor");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
