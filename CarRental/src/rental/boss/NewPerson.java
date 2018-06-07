package rental.boss;

import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import rental.resources.db.ConnectionDB;

public class NewPerson extends AnchorPane {

    @FXML
    private Label title;
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
    
    private ObservableList<Node> children;
    private AnchorPane contentBox;

    public Label getTitle() {
        return title;
    }

    public void setTitle(Label title) {
        this.title = title;
    }

    public Button getAddButton() {
        return addButton;
    }

    public void setAddButton(Button addButton) {
        this.addButton = addButton;
    }

    public TextField getFirstNameField() {
        return firstNameField;
    }

    public void setFirstNameField(TextField firstNameField) {
        this.firstNameField = firstNameField;
    }

    public TextField getSurnameField() {
        return surnameField;
    }

    public void setSurnameField(TextField surnameField) {
        this.surnameField = surnameField;
    }

    public TextField getTelField() {
        return telField;
    }

    public void setTelField(TextField telField) {
        this.telField = telField;
    }

    public TextField getEmailField() {
        return emailField;
    }

    public void setEmailField(TextField emailField) {
        this.emailField = emailField;
    }

    public RadioButton getEmployeeRadio() {
        return employeeRadio;
    }

    public void setEmployeeRadio(RadioButton employeeRadio) {
        this.employeeRadio = employeeRadio;
    }

    public RadioButton getBossRadio() {
        return bossRadio;
    }

    public void setBossRadio(RadioButton bossRadio) {
        this.bossRadio = bossRadio;
    }

    public ToggleGroup getGroup() {
        return group;
    }

    public void setGroup(ToggleGroup group) {
        this.group = group;
    }

    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }
    
    private ObservableList<Node> nodeList;

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
        nodeList = FXCollections.observableArrayList();

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
                    String haslo = "klient";
                    if (personType.equalsIgnoreCase("pracownik")) {
                        haslo = "pracownik";
                    } else if (personType.equalsIgnoreCase("szef")) {
                        haslo = "szef";
                    }
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
                            + "Jego hasło zostało ustawione domyślne: " + haslo + "\nZmień je jak najszybciej!");
                    alert.show();
                    clearFields();

                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Błąd");
                    alert.setHeaderText("Nie można dodać nowej osoby.\nSprawdź dane i spróbuj jeszcze raz.");
                    alert.show();
                }

            } else {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Uwaga");
                alert.setHeaderText("Sprawdź poprawność danych");
                alert.show();
            }

        });
    }
    
    public void setNode(ObservableList<Node> nodeList){
        nodeList.addAll(nodeList);
    }

    private void clearFields() {
        emailField.clear();
        firstNameField.clear();
        surnameField.clear();
        telField.clear();
        personType = null;
        group.selectToggle(null);
    }
    
    public void setChildren(ObservableList<Node> children, AnchorPane contentBox){
        this.children = children;
        this.contentBox = contentBox;
    }

}
