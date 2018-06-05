package rental.boss;

import javafx.scene.control.TextField;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;


public class NewPerson extends AnchorPane {
    @FXML
    TextField emailField;

    public String getEmail() {
        return emailField.getText();
    }
    
    public TextField getEmailField() {
        return emailField;
    }

    public void setEmail(String email) {
        this.emailField.setText(email);
    }
    
    public NewPerson(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("bossNewPerson.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
}
