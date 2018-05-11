package rental.client;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import rental.AController;

public class ClientController extends AController {

    @FXML
    Label myPersolanlDataLabel;

    @FXML
    AnchorPane contentBox;

    private PersonalData personalData;

    public ClientController() {
        //TODO: log4j implemented
        System.out.println("---ClientController opened");
    }

    @FXML
    public void myProfilTab_Clicked(MouseEvent mouseEvent) {
        System.out.println("Login: " + login);

        personalData = new PersonalData();
        contentBox.getChildren().setAll(personalData);
        personalData.setEmailField(login);

        selectMenuItem(myPersolanlDataLabel);
    }

    private void selectMenuItem(Label label) {
        label.underlineProperty().set(true);
        label.setFont(Font.font("System", FontWeight.BOLD, 14));
    }

    
}
