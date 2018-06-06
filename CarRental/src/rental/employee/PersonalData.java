package rental.employee;

import javafx.scene.control.TextField;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import rental.User;
import rental.resources.db.ConnectionDB;


public class PersonalData extends AnchorPane {
   private User user;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField telField;
    @FXML
    private TextField emailField;

    private String prevFirstName;
    private String prevSurname;
    private String prevTel;
    private String prevEmail;

    @FXML
    private PasswordField oldPasswordField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button saveButton;

    public String getFirstName() {
        return firstNameField.getText();
    }

    public void setFirstName(String firstNameField) {
        if (firstNameField != null && !firstNameField.equals("")) {
            this.firstNameField.setText(firstNameField);
        }
    }

    public String getSurname() {
        return surnameField.getText();
    }

    public void setSurname(String surnameField) {
        this.surnameField.setText(surnameField);
    }

    public String getTel() {
        return telField.getText();
    }

    public void setTel(String telField) {
        this.telField.setText(telField);
    }

    public String getEmail() {
        return emailField.getText();
    }

    public void setEmail(String email) {
        this.emailField.setText(email);
    }

    public String getOldPassword() {
        return oldPasswordField.getText();
    }

    public void setOldPassword(String oldPassword) {
        this.oldPasswordField.setText(oldPassword);
    }

    public String getNewPassword() {
        return newPasswordField.getText();
    }

    public void setNewPassword(String newPassword) {
        this.newPasswordField.setText(newPassword);
    }

    public String getConfirmPassword() {
        return confirmPasswordField.getText();
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPasswordField.setText(confirmPassword);
    }
    
    public PersonalData(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("employeePersonalData.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    
    public void setUserAndPrepareFields(User user) {
        if (user.getId() != null) {
            this.user = user;
            this.setFirstName(this.user.getFirstName());
            this.setSurname(this.user.getSurname());
            this.setEmail(this.user.getEmail());
            this.setTel(this.user.getTel());

            bindingPrevAndActualData();
        }
        this.saveButton.setDisable(true);
    }

    private void bindingPrevAndActualData() {
        this.prevFirstName = getFirstName();
        this.prevSurname = getSurname();
        this.prevEmail = getEmail();
        this.prevTel = getTel();
    }

    private void updateUserAndFields() {
        user.setFirstName(this.getFirstName());
        user.setSurname(this.getSurname());
        user.setEmail(this.getEmail());
        user.setTel(this.getTel());
        bindingPrevAndActualData();
        this.saveButton.setDisable(true);
    }

    @FXML
    public void updatePersonalData_Click() {
        if (user.getId() != null && !user.getId().equals("")
                && isChangedPersonalData()) {
            String sqlUpdate = "UPDATE uzytkownicy SET imie = ?, nazwisko = ?, telefon = ?, email = ? WHERE user_id = " + this.user.getId();

            try (Connection conn = ConnectionDB.connect();
                    PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
                pstmt.setString(1, getFirstName());
                pstmt.setString(2, getSurname());
                pstmt.setString(3, getTel());
                pstmt.setString(4, getEmail());

                pstmt.executeUpdate();
                updateUserAndFields();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Moje dane");
                alert.setHeaderText("Uaktualnienie danych");
                alert.setContentText("Zaktualizowano pomyślnie.");
                alert.show();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                setUserAndPrepareFields(this.user);
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Moje dane");
                alert.setHeaderText("Uaktualnienie danych");
                alert.setContentText("Błąd podczas aktualizowania. Sprawdź poprawność wypełnionych danych.");
                alert.show();
            }
        }
    }

    @FXML
    public void dataModified(KeyEvent event) {
        if (event.getSource() instanceof TextField) {
            saveButton.setDisable(!isChangedPersonalData());
        }
    }

    private boolean isChangedPersonalData() {
        return (!prevFirstName.equals(getFirstName())
                || !prevSurname.equals(getSurname())
                || !prevTel.equals(getTel())
                || !prevEmail.equals(getEmail()));
    }

    @FXML
    public void changedPassword_Click() {
        if (user.getId() != null && !user.getId().equals("")
                && this.getOldPassword().equals(user.getPassword())
                && this.getNewPassword() != null
                && !this.getNewPassword().equals("")
                && this.getNewPassword().equals(this.getConfirmPassword())) {

            String sqlUpdate = "UPDATE uzytkownicy SET haslo = ? WHERE user_id = " + this.user.getId();

            try (Connection conn = ConnectionDB.connect();
                    PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
                pstmt.setString(1, getNewPassword());
                pstmt.executeUpdate();

                user.setPassword(this.getNewPassword());
                clearPasswordFields();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Zmiana hasła");
                alert.setHeaderText("Hasło zmienione pomyślnie.");
                alert.show();
            } catch (SQLException e) {
                System.out.println(e.getMessage());

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Zmiana hasła");
                alert.setHeaderText("Błąd podczas zmiany hasła. Spróbuj jeszcze raz.");
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Zmiana hasła");
            alert.setHeaderText("Błąd podczas zmiany hasła. Spróbuj jeszcze raz.");
            alert.show();
        }
    }

    private void clearPasswordFields() {
        this.setOldPassword("");
        this.setNewPassword("");
        this.setConfirmPassword("");
    }
}
