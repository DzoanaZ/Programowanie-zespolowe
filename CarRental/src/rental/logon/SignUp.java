package rental.logon;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import rental.boss.OneOrder;
import rental.resources.db.ConnectionDB;

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

    public SignUp(AnchorPane ap, ObservableList<Node> children) {
        this.anchorPane = ap;
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
    public void signupButton_Click() {
        if (emailField.getText() != null && !emailField.getText().equals("")
                && firstField.getText() != null && !firstField.getText().equals("")
                && surnameField.getText() != null && !surnameField.getText().equals("")
                && passwordField.getText() != null && !passwordField.getText().equals("")
                && passwordField.getText().equals(confirmField.getText())) {

            String sql = "INSERT INTO uzytkownicy (email, imie, nazwisko, telefon, haslo) "
                    + "VALUES (?,?,?,?,?)";

            try (Connection conn = ConnectionDB.connect();
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, emailField.getText());
                pstmt.setString(2, firstField.getText());
                pstmt.setString(3, surnameField.getText());
                pstmt.setString(4, (telField.getText() != null && telField.getText().equals("")) ? telField.getText() : "");
                pstmt.setString(5, passwordField.getText());

                pstmt.execute();
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Dodawanie pracownika.");
                alert.setHeaderText("Nowy użytkownik został dodany.");
                alert.showAndWait();
                backLabel_Click();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Dodawanie pracownika.");
                alert.setHeaderText("Wystąpił błąd. Sprawdź poprawność wszystkich danych i spróbuj ponownie.");
                alert.show();
            }
        }
        else{
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Dodawanie pracownika.");
            alert.setHeaderText("Musisz uzupełnić wszystkie dane. Opcjonalny jest jedynie numer telefonu.");
            alert.show();
        }
    }

    @FXML
    public void backLabel_Click() {
        emailField.clear();
        firstField.clear();
        surnameField.clear();
        telField.clear();
        passwordField.clear();
        confirmField.clear();
        anchorPane.getChildren().setAll(this.children);
    }

}
