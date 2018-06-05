package rental.boss;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

public class HistoryOrders extends AnchorPane{
    @FXML
    private ListView listHistoryOrders;
    
    public HistoryOrders(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("bossHistoryOrders.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        
        ObservableList<OneOrder> items = FXCollections.observableArrayList();
        items.add(new OneOrder());
        listHistoryOrders.setItems(items);
    }
    
}
