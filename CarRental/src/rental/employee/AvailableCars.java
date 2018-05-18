package rental.employee;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class AvailableCars extends AnchorPane {
    @FXML
    VBox carsVBox;
    @FXML
    Label title;
    
    public AvailableCars(String miasto){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("employeeAvailableCars.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        finally{
            title.setText("Dostępne samochody - "+miasto);
        }
        
        
        ObservableList<OneAvailableCar> items = FXCollections.observableArrayList();
        items.add(new OneAvailableCar());
        OneAvailableCar oac = new OneAvailableCar();
        oac.setCarData("RENAULT MEGANE III GRANDTOUR", "2.0 16v 140KM Benzyna", "5", "189,00 zł", "2 szt.", "Rzeszów");
        items.add(oac);
        carsVBox.getChildren().setAll(items);
    }
}
