package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AccountController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField retypePasswordField;
    @FXML
    private Text errorMessage;

    private Connection connection;
    private PreparedStatement preparedStatement;

    public void cancelButtonAction(ActionEvent actionEvent) {
        try {
            //close the account scene
            Node node = (Node) actionEvent.getSource();
            Stage accountStage = (Stage) node.getScene().getWindow();
            accountStage.close();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoginScene.fxml"));
            Parent root = (Parent) fxmlLoader.load();

            //open the login scene
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cant open the Login Scene");
            errorMessage.setText("Cant open Login window");
        }
    }
    public void createAccountButtonAction(ActionEvent actionEvent) {

        if(passwordField.getText().equals(retypePasswordField.getText())
            && (!usernameField.getText().equals(""))
            && (!passwordField.getText().equals(""))) {
            try {
                connection = DatabaseConnector.connect();
                String hashed = BCrypt.hashpw(passwordField.getText(), BCrypt.gensalt());

                preparedStatement = connection.prepareStatement("INSERT INTO accounts (username, password) VALUES (?, ?);");
                preparedStatement.setString(1, usernameField.getText());
                preparedStatement.setString(2, hashed);
                preparedStatement.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            //return to login scene
            cancelButtonAction(actionEvent);
        }else {
            System.out.println("Failed to create a new account");
            errorMessage.setText("Failed to create new a account");
        }
    }
}
