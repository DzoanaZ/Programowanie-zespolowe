package rental.boss;

import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import rental.AController;

public class BossController extends AController {

    private Label activeLabel;
    private PersonalData personalData;
    private OrdersActual ordersActual;
    private OrdersHistory ordersHistory;
    private AvailableCars availableCars;
    private OrdersExpect ordersExpect;
    private NewCar newCar;
    private NewPerson newPerson;
    private ShowUser showUser;
        
    @FXML
    private AnchorPane contentBox;
    
    @FXML
    private Label myPersonalDataLabel;
    @FXML
    private Label actualOrdersLabel;
    @FXML
    private Label historyOrdersLabel;
    @FXML
    private Label expectOrdersLabel;
    @FXML
    private Label customersLabel;
    @FXML
    private Label employeesLabel;

    @FXML
    private Label rzeszowLabel;
    @FXML
    private Label stalowaLabel;
    @FXML
    private Label dynowLabel;
    @FXML
    private Label jasloLabel;
    @FXML
    private Label newCarLabel;
    
    @FXML
    private Label newPersonLabel;


    public BossController() {
        //TODO: log4j implemented
        System.out.println("---BossController opened");
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
            else if (selectedItem.getId().equals("actualOrdersLabel")) {
                item = LabelType.actualOrdersLabel;
            } 
            else if (selectedItem.getId().equals("historyOrdersLabel")) {
                item = LabelType.historyOrdersLabel;
            } 
            else if (selectedItem.getId().equals("expectOrdersLabel")) {
                item = LabelType.expectOrderLabel;
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
            else if (selectedItem.getId().equals("newCarLabel")) {
                item = LabelType.newCarLabel;
            }
            else if (selectedItem.getId().equals("newPersonLabel")) {
                item = LabelType.newPersonLabel;
            }
            else if (selectedItem.getId().equals("customersLabel")) {
                item = LabelType.customersLabel;
            }
            else if (selectedItem.getId().equals("employeesLabel")) {
                item = LabelType.employeesLabel;
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
        ObservableList<Node> children ;
        switch (item) {
            case myPersonalDataLabel:
                personalData = new PersonalData();
                contentBox.getChildren().setAll(personalData);
                personalData.setUserAndPrepareFields(user);
                selectMenuItem(this.myPersonalDataLabel);
                break;
            case actualOrdersLabel:
                ordersActual = new OrdersActual();
                contentBox.getChildren().setAll(ordersActual);
                ordersActual.prepareData(user);
                selectMenuItem(this.actualOrdersLabel);
                break;
            case historyOrdersLabel:               
                ordersHistory = new OrdersHistory();
                contentBox.getChildren().setAll(ordersHistory);
                ordersHistory.prepareData(user);
                selectMenuItem(this.historyOrdersLabel);
                break;
            case expectOrderLabel:               
                ordersExpect = new OrdersExpect();
                contentBox.getChildren().setAll(ordersExpect);
                ordersExpect.prepareData(user);
                selectMenuItem(this.expectOrdersLabel);
                break;
            case dynowLabel:
                availableCars = new AvailableCars("Dynów");
                availableCars.prepareFields();
                contentBox.getChildren().setAll(availableCars);
                selectMenuItem(this.dynowLabel);
            break;
            case jasloLabel:
                availableCars = new AvailableCars("Jasło");
                availableCars.prepareFields();
                contentBox.getChildren().setAll(availableCars);
                selectMenuItem(this.jasloLabel);
                break;
            case rzeszowLabel:
                availableCars = new AvailableCars("Rzeszów");
                contentBox.getChildren().setAll(availableCars);
                availableCars.prepareFields();
                selectMenuItem(this.rzeszowLabel);
                break;
            case stalowaLabel:
                availableCars = new AvailableCars("Stalowa Wola");
                availableCars.prepareFields();
                contentBox.getChildren().setAll(availableCars);
                selectMenuItem(this.stalowaLabel);
                break;
            case newCarLabel:
                newCar = new NewCar();
                contentBox.getChildren().setAll(newCar);
                selectMenuItem(this.newCarLabel);
                break;
            case newPersonLabel:
                newPerson = new NewPerson();
                contentBox.getChildren().setAll(newPerson);
                selectMenuItem(this.newPersonLabel);
                break;
            case customersLabel:
                showUser = new ShowUser("Klienci");
                contentBox.getChildren().setAll(showUser);
                showUser.prepareData(user);
                selectMenuItem(this.customersLabel);
                break;
            case employeesLabel:
                showUser = new ShowUser("Pracownicy");
                contentBox.getChildren().setAll(showUser);
                showUser.prepareData(user);
                selectMenuItem(this.employeesLabel);
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
        actualOrdersLabel,
        historyOrdersLabel,
        expectOrderLabel,
        dynowLabel,
        jasloLabel,
        rzeszowLabel,
        stalowaLabel,
        newCarLabel,
        newPersonLabel,
        customersLabel,
        employeesLabel
    }
}
