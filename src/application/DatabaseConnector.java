package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    public static Connection connection = null;

    /**
     * Connect to a database
     * @return
     */

    public static Connection connect() {

        //Server address from user inputs
        String address = AddressController.address;
        try {
            connection = DriverManager.getConnection("jdbc:mariadb://" + address + ":3306/cars_javafx?useLegacyDatetimeCode=false&serverTimezone=UTC","manu", "kO(QHKJX4]/.Do7b");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }

    /**
     * Disconnect from Database
     */
    public static void disconnect() {
        if(connection != null) {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}


/* "jdbc:mariadb://" + address + ":3306/cars_javafx?useLegacyDatetimeCode=false&serverTimezone=UTC","bob", "E0750FAS7F6Tvpzx" */