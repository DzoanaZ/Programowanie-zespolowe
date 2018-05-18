package rental.employee;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;

public class OneAvailableCar extends AnchorPane {
    @FXML
    Label carName;
    @FXML
    Label engine;
    @FXML
    Label doors;
    @FXML
    Label price;
    @FXML
    Label pieces;
    @FXML
    Label city;

    public OneAvailableCar(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("employeeOneAvailableCar.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public String getCarName() {
        return carName.getText();
    }

    public void setCarName(String carName) {
        this.carName.setText(carName);
    }

    public String getEngine() {
        return engine.getText();
    }

    public void setEngine(String engine) {
        this.engine.setText(engine);
    }

    public String getDoors() {
        return doors.getText();
    }

    public void setDoors(String doors) {
        this.doors.setText(doors);
    }

    public String getPrice() {
        return price.getText();
    }

    public void setPrice(String price) {
        this.price.setText(price);
    }

    public String getPieces() {
        return pieces.getText();
    }

    public void setPieces(String pieces) {
        this.pieces.setText(pieces);
    }

    public String getCity() {
        return city.getText();
    }

    public void setCity(String city) {
        this.city.setText(city.toUpperCase());
    }
    
    public void setCarData(String carName, String engine, String doors, 
                            String price, String pieces, String city){
        this.setCarName(carName);
        this.setEngine(engine);
        this.setDoors(doors);
        this.setPrice(price);
        this.setPieces(pieces);
        this.setCity(city);
    }
}
