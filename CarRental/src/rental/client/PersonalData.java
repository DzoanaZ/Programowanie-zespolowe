package rental.client;

import javafx.scene.control.TextField;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;


public class PersonalData extends AnchorPane {
    @FXML
    TextField emailField;

    public String getEmailField() {
        return emailField.getText();
    }
    
    public TextField getEmailTextField() {
        return emailField;
    }

    public void setEmailField(String emailField) {
        this.emailField.setText(emailField);
    }
    
    public PersonalData(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("clientPersonalData.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
}
