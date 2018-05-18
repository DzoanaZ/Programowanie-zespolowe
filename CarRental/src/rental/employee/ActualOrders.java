package rental.employee;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

public class ActualOrders extends AnchorPane{
    
    @FXML
    private ListView listActualOrders;
    
    
    
    public ActualOrders(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("employeeActualOrders.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        
        ObservableList<OneOrder> items = FXCollections.observableArrayList();
        OneOrder temp = new OneOrder();
        temp.setOrderData("Renault Megane III", "22/05/2018", "25/05/2018", "450,00 zł", "Oczekujące", "Stalowa Wola");
        items.add(new OneOrder());
        items.add(temp);
        listActualOrders.setItems(items);
    }
}
