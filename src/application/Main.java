package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddressScene.fxml"));
        Parent root = (Parent) fxmlLoader.load();

        // Open the Address Scene
        stage.setTitle("Server Address");
        stage.setScene(new Scene(root));
        stage.show();

    }
}
