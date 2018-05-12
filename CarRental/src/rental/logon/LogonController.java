package rental.logon;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import rental.AController;

import java.io.IOException;


public class LogonController {

    @FXML
    TextField loginField;
    @FXML
    PasswordField passwordField;
    @FXML
    Label errorLabel;

    public void logonButton_Click(ActionEvent event) {
        UserType user = null;
        System.out.println("Click! " + loginField.getText() + " | " + passwordField.getText());

        //TODO: Database connection implemented
        if (!loginField.getText().equals("") && loginField != null) {
            errorLabel.setText("");

            if (loginField.getText().equalsIgnoreCase("admin") && passwordField.getText().equalsIgnoreCase("admin")) {
                user = UserType.Boss;
            }
            else if (loginField.getText().equalsIgnoreCase("employee") && passwordField.getText().equalsIgnoreCase("employee")) {
                user = UserType.Employee;
            }
            else if (loginField.getText().equals(passwordField.getText())) {
                user = UserType.Customer;
            }
            else {
                errorLabel.setText("Nieprawidłowy login i/lub hasło");
            }
        }
        else {
            errorLabel.setText("Nieprawidłowy login i/lub hasło");
        }
        if (user != null) {
            switch (user) {
                case Boss:
                    openNewStage(event, "/rental/boss/bossPanel.fxml", user);
                    break;
                case Employee:
                    openNewStage(event, "/rental/employee/employeePanel.fxml", user);
                    break;
                case Customer:
                    openNewStage(event, "/rental/client/clientPanel.fxml", user);
                    break;
            }
        }
    }


    private void openNewStage(ActionEvent event, String patch, UserType user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(patch));
            Stage stage = new Stage(StageStyle.DECORATED);

            switch (user) {
                case Boss:
                    stage.setTitle("Car Rental - Panel Prezesa");
                    break;
                case Employee:
                    stage.setTitle("Car Rental - Panel Pracownika");
                    break;
                case Customer:
                    stage.setTitle("Car Rental - Panel Klienta");
                    break;
            }

            stage.setScene(new Scene(loader.load(),900,620));
            stage.setResizable(false);
            AController controller = loader.<AController>getController();
            controller.initData(loginField.getText());

            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private enum UserType {
        Boss,
        Employee,
        Customer
    }
}
