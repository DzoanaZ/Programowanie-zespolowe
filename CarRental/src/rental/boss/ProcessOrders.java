package rental.boss;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

public class ProcessOrders extends AnchorPane{
    
    @FXML
    private ListView listProcessOrders;
    
    
    
    public ProcessOrders(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("bossProcessOrders.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        
        ObservableList<ProcessOneOrder> items = FXCollections.observableArrayList();
        ProcessOneOrder temp = new ProcessOneOrder();
        temp.setOrderData("Renault Megane III", "22/05/2018", "25/05/2018", "450,00 zł", "Oczekujące", "Stalowa Wola");
        items.add(new ProcessOneOrder());
        items.add(temp);
        listProcessOrders.setItems(items);
    }
}
