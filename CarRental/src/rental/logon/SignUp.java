package rental.logon;

import java.io.IOException;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

public class SignUp extends AnchorPane {
    AnchorPane anchorPane;
     ObservableList<Node> children;
    
    @FXML
    Button signupButton;
    @FXML
    Label backLabel;
    
    @FXML 
    TextField emailField;
    @FXML 
    TextField firstField;
    @FXML 
    TextField surnameField;
    @FXML 
    TextField telField;
    @FXML
    PasswordField passwordField;
    @FXML
    PasswordField confirmField;
    
    public SignUp(AnchorPane ap,  ObservableList<Node> children) {
        this.anchorPane=ap;
        this.children = children;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("signupPanel.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    @FXML
    public void signupButton_Click(){
    
    }
    
    @FXML
    public void backLabel_Click(){
        anchorPane.getChildren().setAll(this.children);
    }
    
}
