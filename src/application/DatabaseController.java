package application;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;


public class DatabaseController {

    @FXML
    private TextField searchTextField;
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private RadioButton insertIntoRadioBtn;
    @FXML
    private RadioButton updateRadioBtn;
    @FXML
    private RadioButton deleteRadioBtn;

    @FXML
    private TableView<Car> tableView;
    @FXML
    private TableColumn<Car, String> carIdColumn;
    @FXML
    private TableColumn<Car, String> userIdColumn;
    @FXML
    private TableColumn<Car, String> brandColumn;
    @FXML
    private TableColumn<Car, String> typeColumn;

    @FXML
    private TextField carIdTextField;
    @FXML
    private TextField userIdTextField;
    @FXML
    private TextField brandTextField;
    @FXML
    private TextField typeTextField;

    @FXML
    private Button insertIntoBtn;
    @FXML
    private Button updateBtn;
    @FXML
    private Button deleteBtn;

    @FXML
    private Text errorMessage;

    //from LoginController, the PK of the user that is logged in
    private String userId;

    private static Connection connection;
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;




    public void setFK(String userId) {
        //from LoginController, the PK of the user that is logged in
        this.userId = userId;
    }

    public void deleteBtnAction(ActionEvent actionEvent) {

        if (carIdTextField.getText() != null && !carIdTextField.getText().isEmpty()) {
            Car car = new Car(null, null, null, null);

        car.setCarId(carIdTextField.getText());
        car.setUserId(userIdTextField.getText());
        car.setBrand(brandTextField.getText());
        car.setType(typeTextField.getText());

        try {
            PreparedStatement preparedStatement =
                    DatabaseConnector.connect().prepareStatement("DELETE FROM cars WHERE car_id = ?");
            preparedStatement.setString(1, car.getCarId());
            preparedStatement.executeUpdate();
            preparedStatement.close();

            DatabaseConnector.disconnect();
            initialize();

            errorMessage.setText("");

            brandTextField.setText("");
            typeTextField.setText("");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    } else {
        errorMessage.setText("Please select a car from the table");
    }
    }

    @FXML
    public void updateBtnAction(ActionEvent actionEvent) {

        if (brandTextField.getText() != null && !brandTextField.getText().isEmpty()
                && typeTextField.getText() != null && !typeTextField.getText().isEmpty()) {

            Car car = new Car(null, null, null, null);

            car.setCarId(carIdTextField.getText());
            car.setUserId(userIdTextField.getText());
            car.setBrand(brandTextField.getText());
            car.setType(typeTextField.getText());

            try {
                PreparedStatement preparedStatement =
                        DatabaseConnector.connect().prepareStatement("UPDATE cars SET brand = ?, type = ? WHERE car_id = ?");
                preparedStatement.setString(1, car.getBrand());
                preparedStatement.setString(2, car.getType());
                preparedStatement.setString(3, car.getCarId());
                preparedStatement.executeUpdate();
                preparedStatement.close();

                DatabaseConnector.disconnect();
                initialize();

                errorMessage.setText("");

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        } else {
            errorMessage.setText("Please complete all fields");
        }
    }

    @FXML
    public void insertBtnAction(ActionEvent actionEvent) {

        if (brandTextField.getText() != null && !brandTextField.getText().isEmpty()
        && typeTextField.getText() != null && !typeTextField.getText().isEmpty()) {

            Car car = new Car(null, null, null,null);

            car.setBrand(brandTextField.getText());
            car.setType(typeTextField.getText());

            try {
                PreparedStatement preparedStatement = DatabaseConnector.connect().prepareStatement("INSERT INTO cars VALUES(?,?,?,?)");
                preparedStatement.setString(1,null);
                preparedStatement.setString(2,userId);
                preparedStatement.setString(3,car.getBrand());
                preparedStatement.setString(4,car.getType());
                preparedStatement.executeUpdate();
                preparedStatement.close();

                DatabaseConnector.disconnect();
                initialize();

                errorMessage.setText("");

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
            errorMessage.setText("Please complete all fields");
        }
    }

    private void initialize() {
        // initialize the table view with four columns
        carIdColumn.setCellValueFactory(cellData -> cellData.getValue().carIdProperty());
        userIdColumn.setCellValueFactory(cellData -> cellData.getValue().userIdProperty());
        brandColumn.setCellValueFactory(cellData -> cellData.getValue().brandProperty());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());

        setVisibleItems();

        Platform.runLater(() -> {
            //fill the tableview with data from observable list

            try {
                System.out.println("User ID is: + userId");
                tableView.setItems(getCarData());
                buildCar();
                sortFilterTableView();
                observeRadioButtonChanges();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        });
    }

    private void observeRadioButtonChanges() {

        toggleGroup.selectedToggleProperty().addListener((observableValue, oldValue, newValue) ->{

            //Cast object to Radio Button
            RadioButton radioButton = (RadioButton) newValue.getToggleGroup().getSelectedToggle();

            if (radioButton.getText().contains("Insert Into")) {

                insertIntoBtn.setVisible(true);
                insertIntoBtn.setManaged(true);

                updateBtn.setVisible(false);
                updateBtn.setManaged(false);

                deleteBtn.setVisible(false);
                deleteBtn.setManaged(false);

                brandTextField.setVisible(true);
                brandTextField.setManaged(true);

                typeTextField.setVisible(true);
                typeTextField.setManaged(true);
            } else if (radioButton.getText().contains("Update")) {

                insertIntoBtn.setVisible(false);
                insertIntoBtn.setManaged(false);

                updateBtn.setVisible(true);
                updateBtn.setManaged(true);

                deleteBtn.setVisible(false);
                deleteBtn.setManaged(false);

                brandTextField.setVisible(true);
                brandTextField.setManaged(true);

                typeTextField.setVisible(true);
                typeTextField.setManaged(true);

            } else {
                insertIntoBtn.setVisible(false);
                insertIntoBtn.setManaged(false);

                updateBtn.setVisible(false);
                updateBtn.setManaged(false);

                deleteBtn.setVisible(true);
                deleteBtn.setManaged(true);

                brandTextField.setVisible(false);
                brandTextField.setManaged(false);

                typeTextField.setVisible(false);
                typeTextField.setManaged(false);
            }
        } );
    }

    private void sortFilterTableView() throws SQLException {

        FilteredList<Car> filteredList = new FilteredList<>(getCarData(), p -> true);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(car -> {
                //if search text is empty display all pets
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (car.getBrand().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // search string is a match
                } else if (car.getType().toLowerCase().contains(lowerCaseFilter)) {
                    return false; // search string is a match
                }
                return false; //does not match
            });
        });

        SortedList<Car> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);

    }

    private void buildCar() {
        tableView.setOnMouseClicked((MouseEvent mouseEvent) -> {
            if (mouseEvent.getClickCount() > 0) {
                if (tableView.getSelectionModel().getSelectedItem() != null) {
                    Car car = tableView.getSelectionModel().getSelectedItem();
                    carIdTextField.setText(car.getCarId());
                    userIdTextField.setText(car.getUserId());
                    brandTextField.setText(car.getBrand());
                    typeTextField.setText(car.getType());
                }
            }
        });
    }

    private ObservableList<Car> getCarData() throws  SQLException {

        connection = DatabaseConnector.connect();
        preparedStatement = connection.prepareStatement("SELECT * FROM cars WHERE user_id = ?");
        preparedStatement.setString(1, userId);
        resultSet = preparedStatement.executeQuery();

        ObservableList<Car> carList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            String carId = resultSet.getString(1);
            String userId = resultSet.getString(2);
            String brand = resultSet.getString(3);
            String type = resultSet.getString(4);
            //construct Car objects
            Car car = new Car(carId, userId, brand, type);
            // add the car to the list
            carList.add(car);
        }
        DatabaseConnector.disconnect();
        return carList;
    }

    private void setVisibleItems() {

        Toggle toggle = toggleGroup.getSelectedToggle();
        System.out.println("Radio button selected: " + toggle);

        if (toggle == null) {
            //set what the user see's and doesn't see
            insertIntoBtn.setVisible(true);
            insertIntoBtn.setManaged(true);

            updateBtn.setVisible(false);
            updateBtn.setManaged(false);

            deleteBtn.setVisible(false);
            deleteBtn.setManaged(false);

            insertIntoRadioBtn.setSelected(true);
        }
    }
}




























