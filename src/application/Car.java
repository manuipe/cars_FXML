package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Car {

    //Attributes or state
    private StringProperty carId;
    private StringProperty userId;
    private StringProperty brand;
    private StringProperty type;

    //Constructor
    public Car(String carId, String userId, String brand, String type) {

        this.carId = new SimpleStringProperty(carId);
        this.userId = new SimpleStringProperty(userId);
        this.brand = new SimpleStringProperty(brand);
        this.type = new SimpleStringProperty(type);
    }

    //Methods or Behaviour (Setter - Mutator, Getter - Accessor)
    public void setCarId(String carId) {
        this.carId.set(carId);
    }
    public String getCarId() {
        return carId.get();
    }
    public StringProperty carIdProperty() {
        return carId;
    }


    //Methods or Behaviour (Setter - Mutator, Getter - Accessor)
    public void setUserId(String userId) {
        this.userId.set(userId);
    }
    public String getUserId() {
        return userId.get();
    }
    public StringProperty userIdProperty() {
        return userId;
    }


    //Methods or Behaviour (Setter - Mutator, Getter - Accessor)
    public void setBrand(String brand) {
        this.brand.set(brand);
    }
    public String getBrand() {
        return brand.get();
    }
    public StringProperty brandProperty() {
        return brand;
    }

    //Methods or Behaviour (Setter - Mutator, Getter - Accessor)
    public void setType(String type) {
        this.type.set(type);
    }
    public String getType() {
        return type.get();
    }
    public StringProperty typeProperty() {
        return type;
    }
}
