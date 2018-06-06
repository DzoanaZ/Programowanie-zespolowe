package rental.employee;

import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import rental.AController;

public class EmployeeController extends AController {

    private Label activeLabel;
    private PersonalData personalData;
    private OrdersActual actualOrders;
    private OrdersHistory historyOrders;
    private AvailableCars availableCars;
    private OrdersExpect processOrders;
        
    @FXML
    private AnchorPane contentBox;
    
    @FXML
    private Label myPersonalDataLabel;
    @FXML
    private Label currentOrdersLabel;
    @FXML
    private Label historyOrdersLabel;
    @FXML
    private Label expectOrdersLabel;

    @FXML
    private Label rzeszowLabel;
    @FXML
    private Label stalowaLabel;
    @FXML
    private Label dynowLabel;
    @FXML
    private Label jasloLabel;


    public EmployeeController() {
        //TODO: log4j implemented
        System.out.println("---EmployeeController opened");
    }

    @FXML
    public void menuItem_Clicked(MouseEvent mouseEvent) {
        Object sender = mouseEvent.getSource();
        Label selectedItem = null;
        LabelType item = null;

        if (sender instanceof Label) {
            selectedItem = (Label) sender;
        }

        if (selectedItem != null) {
            if (selectedItem.getId().equals("myPersonalDataLabel")) {
                item = LabelType.myPersonalDataLabel;
            } 
            else if (selectedItem.getId().equals("currentOrdersLabel")) {
                item = LabelType.currentOrdersLabel;
            } 
            else if (selectedItem.getId().equals("historyOrdersLabel")) {
                item = LabelType.historyOrdersLabel;
            } 
            else if (selectedItem.getId().equals("expectOrdersLabel")) {
                item = LabelType.expectOrdersLabel;
            }
            else if (selectedItem.getId().equals("dynowLabel")) {
                item = LabelType.dynowLabel;
            } 
            else if (selectedItem.getId().equals("jasloLabel")) {
                item = LabelType.jasloLabel;
            } 
            else if (selectedItem.getId().equals("rzeszowLabel")) {
                item = LabelType.rzeszowLabel;
            } 
            else if (selectedItem.getId().equals("stalowaLabel")) {
                item = LabelType.stalowaLabel;
            }
        }
        if (item != null) {
            readPuzzel(item);
        }

    }

    private void readPuzzel(LabelType item) {
        if (item == null) {
            return;
        }
        switch (item) {
            case myPersonalDataLabel:
                personalData = new PersonalData();
                contentBox.getChildren().setAll(personalData);
                personalData.setUserAndPrepareFields(user);
                selectMenuItem(this.myPersonalDataLabel);
                break;
            case currentOrdersLabel:
                actualOrders = new OrdersActual();
                contentBox.getChildren().setAll(actualOrders);
                actualOrders.prepareData(user);
                selectMenuItem(this.currentOrdersLabel);
                break;
            case historyOrdersLabel:               
                historyOrders = new OrdersHistory();
                contentBox.getChildren().setAll(historyOrders);
                historyOrders.prepareData(user);
                selectMenuItem(this.historyOrdersLabel);
                break;
            case expectOrdersLabel:               
                processOrders = new OrdersExpect();
                contentBox.getChildren().setAll(processOrders);
                processOrders.prepareData(user);
                selectMenuItem(this.expectOrdersLabel);
                break;
            case dynowLabel:
                availableCars = new AvailableCars("Dynów");
                contentBox.getChildren().setAll(availableCars);
                selectMenuItem(this.dynowLabel);
            break;
            case jasloLabel:
                availableCars = new AvailableCars("Jasło");
                contentBox.getChildren().setAll(availableCars);
                selectMenuItem(this.jasloLabel);
                break;
            case rzeszowLabel:
                availableCars = new AvailableCars("Rzeszów");
                contentBox.getChildren().setAll(availableCars);
                selectMenuItem(this.rzeszowLabel);
                break;
            case stalowaLabel:
                availableCars = new AvailableCars("Stalowa Wola");
                contentBox.getChildren().setAll(availableCars);
                selectMenuItem(this.stalowaLabel);
                break;
        }
    }

    private void selectMenuItem(Label label) {
        if (activeLabel != null) {
            activeLabel.getStyleClass().setAll("menuItem");
        }
        activeLabel = label;
        activeLabel.getStyleClass().add("active");
    }

    enum LabelType {
        myPersonalDataLabel,
        currentOrdersLabel,
        historyOrdersLabel,
        expectOrdersLabel,
        dynowLabel,
        jasloLabel,
        rzeszowLabel,
        stalowaLabel
    }
}
