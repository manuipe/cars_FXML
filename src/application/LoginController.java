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
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Text errorMessage;

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public void createAccountButtonAction(ActionEvent actionEvent) {

        try {
            //close the login scene
            Node node = (Node) actionEvent.getSource();
            Stage loginStage = (Stage) node.getScene().getWindow();
            loginStage.close();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AccountScene.fxml"));
            Parent root = (Parent) fxmlLoader.load();

            //open the accounts scene
            Stage stage = new Stage();
            stage.setTitle("Create Account");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
            System.out.println("Cant open account scene");
            errorMessage.setText("Cant open Accounts window");
    }
}

    private User getUser() {

        User user = new User();

        try {
            connection = DatabaseConnector.connect();
            preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE username = ?");
            preparedStatement.setString(1, usernameField.getText());
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user.setUserId(resultSet.getString(1));
                user.setUsername(resultSet.getString(2));
                user.setPassword(resultSet.getString(3));
            }
            resultSet.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            errorMessage.setText("Cant get user from database");
        }
        return user;
    }

    private Boolean checkPassword(User user) {

        if (BCrypt.checkpw(passwordField.getText(), user.getPassword())) {
            // it matches
            System.out.println("Password matches");
            return true;
        } else {
            //it does not match
            System.out.println("Password does not match");
            return false;
        }
    }


    public void loginButtonAction(ActionEvent actionEvent) {
        //Build a user from database results
        User user = getUser();

        if (usernameField.getText().equals(user.getUsername())
            && (!usernameField.getText().equals(""))
                && (checkPassword(user) == true)) {

            try {
                // close the login scene
                Node node = (Node) actionEvent.getSource();
                Stage loginStage = (Stage) node.getScene().getWindow();
                loginStage.close();

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DatabaseScene.fxml"));
                Parent root = (Parent) fxmlLoader.load();

                // Send the DatabaseController the user id of the user that is logged in
                // this will be the FOREIGN KEY for the cars table
                DatabaseController databaseController = fxmlLoader.<DatabaseController>getController();
                databaseController.setFK(user.getUserId());

                // open the database scene
                Stage stage = new Stage();
                stage.setTitle("Cars Database");
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Cant open Cars Database scene");
                errorMessage.setText("Cant open Cars Database window");
            }

        } else {
            errorMessage.setText("Invalid username or password");
        }
    }
}
