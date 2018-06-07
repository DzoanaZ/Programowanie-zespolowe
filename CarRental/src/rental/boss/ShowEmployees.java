package rental.boss;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class ShowEmployees extends AnchorPane {
    @FXML
    VBox carsVBox;
    @FXML
    Label title;
    
    public ShowEmployees(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("bossShowEmployees.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        
        //ObservableList<OneEmployee> items = FXCollections.observableArrayList();
        //carsVBox.getChildren().setAll(items);
    }
}
