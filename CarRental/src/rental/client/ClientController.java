package rental.client;

import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import rental.AController;

public class ClientController extends AController {

    @FXML
    private Label myPersonalDataLabel;
    private Label activeLabel;

    @FXML
    AnchorPane contentBox;

    private PersonalData personalData;
    @FXML
    private TitledPane myProfilTab;
    @FXML
    private Label myCurrentOrdersLabel;
    @FXML
    private Label myRentalHistoryLabel;
    @FXML
    private TitledPane availableCarsTab;
    @FXML
    private Label rzeszowLabel;
    @FXML
    private Label stalowaLabel;
    @FXML
    private Label dynowLabel;
    @FXML
    private Label jasloLabel;
    @FXML
    private TitledPane applyOrderTab;
    @FXML
    private Label askForAvailabilityLabel;

    public ClientController() {
        //TODO: log4j implemented
        System.out.println("---ClientController opened");
    }
    
    @FXML
    public void menuItem_Clicked(MouseEvent mouseEvent){
        Object sender = mouseEvent.getSource();
        Label selectedItem = null;
        LabelType item = null;
        
        if(sender instanceof Label){
            selectedItem = (Label)sender;
        }
        
        if(selectedItem!=null){
            if(selectedItem.getId().toString().equals("myPersonalDataLabel"))
                item = LabelType.myPersonalDataLabel;
            else
            if(selectedItem.getId().toString().equals("myCurrentOrdersLabel"))
                item = LabelType.myCurrentOrdersLabel;
            else
            if(selectedItem.getId().toString().equals("myRentalHistoryLabel"))
                item = LabelType.myRentalHistoryLabel;
            else
            if(selectedItem.getId().toString().equals("dynowLabel"))
                item = LabelType.dynowLabel;
            else
            if(selectedItem.getId().toString().equals("jasloLabel"))
                item = LabelType.jasloLabel;
            else
            if(selectedItem.getId().toString().equals("rzeszowLabel"))
                item = LabelType.rzeszowLabel;
            else
            if(selectedItem.getId().toString().equals("stalowaLabel"))
                item = LabelType.stalowaLabel;
            else
            if(selectedItem.getId().toString().equals("askForAvailabilityLabel"))
                item = LabelType.askForAvailabilityLabel; 
        }
        if(item!=null)
            readPuzzel(item);
            
    }

    private void selectMenuItem(Label label) {
        //activeLabel.getStyleClass().
        if(activeLabel!=null)
            activeLabel.getStyleClass().setAll("menuItem");
        
        activeLabel = label;
        activeLabel.getStyleClass().add("active");
    }
    
    private void readPuzzel(LabelType item){
        if(item == null)
            return;
        switch(item){
            case myPersonalDataLabel:
                personalData = new PersonalData();
                contentBox.getChildren().setAll(personalData);
                personalData.setEmailField(login);
                selectMenuItem(this.myPersonalDataLabel);
                break;
                
            case myCurrentOrdersLabel:
                selectMenuItem(this.myCurrentOrdersLabel);
                contentBox.getChildren().clear();
                break;
            case myRentalHistoryLabel:
                selectMenuItem(this.myRentalHistoryLabel);
                contentBox.getChildren().clear();
                break;
            case dynowLabel:
                selectMenuItem(this.dynowLabel);
                contentBox.getChildren().clear();
                break;
            case jasloLabel:
                selectMenuItem(this.jasloLabel);
                contentBox.getChildren().clear();
                break;
            case rzeszowLabel:
                selectMenuItem(this.rzeszowLabel);
                contentBox.getChildren().clear();
                break;
            case stalowaLabel:
                selectMenuItem(this.stalowaLabel);
                contentBox.getChildren().clear();
                break;
            case askForAvailabilityLabel:
                selectMenuItem(this.askForAvailabilityLabel);
                contentBox.getChildren().clear();
                break;
        }
    }

    enum LabelType{
        myPersonalDataLabel,
        myCurrentOrdersLabel,
        myRentalHistoryLabel,
        dynowLabel,
        jasloLabel,
        rzeszowLabel,
        stalowaLabel,
        askForAvailabilityLabel
    }
}
