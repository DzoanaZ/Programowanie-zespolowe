package rental.boss;

import javafx.scene.control.TextField;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import static javafx.scene.web.WebEvent.ALERT;
import rental.resources.db.ConnectionDB;

public class NewPerson extends AnchorPane {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField telField;
    @FXML
    private TextField emailField;
    @FXML
    private Button addButton;
    @FXML
    private RadioButton employeeRadio;
    @FXML
    private RadioButton bossRadio;

    private ToggleGroup group = new ToggleGroup();
    private String personType;

    public NewPerson() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("bossNewPerson.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        employeeRadio.setToggleGroup(group);
        bossRadio.setToggleGroup(group);

        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) {
                RadioButton chk = (RadioButton) t1.getToggleGroup().getSelectedToggle(); // Cast object to radio button

                switch (chk.getText().toLowerCase()) {
                    case "pracownik":
                        personType = "pracownik";
                        break;
                    case "prezes":
                        personType = "prezes";
                        break;
                    default:
                        personType = null;
                        break;
                }
                System.out.println("Selected Radio Button - " + personType);
            }
        });

        addButton.setOnMouseClicked((event) -> {
            if (!firstNameField.getText().equals("") && firstNameField.getText() != null
                    && !surnameField.getText().equals("") && surnameField.getText() != null
                    && !telField.getText().equals("") && telField.getText() != null
                    && !emailField.getText().equals("") && emailField.getText() != null
                    && !personType.equals("") && personType != null) {

                String sql = "INSERT INTO uzytkownicy (email, haslo, imie, nazwisko, telefon, typ)"
                        + "VALUES (?, ?, ?, ?, ?, ?)";

                try (Connection conn = ConnectionDB.connect();
                        PreparedStatement pstmt = conn.prepareStatement(sql)) {

                    pstmt.setString(1, emailField.getText());
                    String haslo = (personType.equalsIgnoreCase("pracownik")) ? "pracownik" : "szef";
                    pstmt.setString(2, haslo);
                    pstmt.setString(3, firstNameField.getText());
                    pstmt.setString(4, surnameField.getText());
                    pstmt.setString(5, telField.getText());
                    String typ = (personType.equalsIgnoreCase("pracownik")) ? "P" : "S";
                    pstmt.setString(6, typ);
                    pstmt.execute();
                    
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Dodano nowego użytkownika");
                    alert.setHeaderText("Dodano nowego użytkownika.\n"
                            + "Jego hasło zostało ustawione domyślne: "+haslo+"\nZmień je jak najszybciej!");
                    alert.show();
                    clearFields();

                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Błąd");
                    alert.setHeaderText("Nie można dodać nowej osoby.\nSprawdź dane i spróbuj jeszcze raz.");
                    alert.show();
                }
                
            }
            else{
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Uwaga");
                alert.setHeaderText("Sprawdź poprawność danych");
                alert.show();
            }

        });
    }

    private void clearFields() {
        emailField.clear();
        firstNameField.clear();
        surnameField.clear();
        telField.clear();
        personType = null;
        group.selectToggle(null);
    }

}
