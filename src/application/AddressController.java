package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AddressController {
    @FXML
    private  TextField addressField;
    @FXML
    private Text errorMessage;

    // Server address used across classes
    public static  String address;


    public void addressButtonAction(ActionEvent actionEvent) {

        //Server address used across classes
        address = addressField.getText();
        DatabaseConnector.connect();

        if (DatabaseConnector.connection == null) {
            System.out.println("Connection Failure");
            errorMessage.setText("Connection Failed");
        } else {
            try {
                // close the address scene
                Node node = (Node) actionEvent.getSource();
                Stage addressStage = (Stage) node.getScene().getWindow();
                addressStage.close();

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoginScene.fxml"));
                Parent root = (Parent) fxmlLoader.load();

                //open the login Scene
                Stage stage = new Stage();
                stage.setTitle("Login");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Cant oprn Login Scene");
                errorMessage.setText("Can open Login Window");
            }
        }
    }
}
