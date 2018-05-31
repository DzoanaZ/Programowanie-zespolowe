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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import rental.User;
import rental.resources.db.ConnectionDB;

public class LogonController {

    @FXML
    TextField loginField;
    @FXML
    PasswordField passwordField;
    @FXML
    Label errorLabel;
    
    User logonUser;
    

    public void logonButton_Click(ActionEvent event) {
        logonUser = new User();
        
        if (loginField.getText() != null && !loginField.getText().equals("")) {
            
            String sql = "SELECT * FROM uzytkownicy WHERE email = ? AND haslo = ?";

            try (Connection conn = ConnectionDB.connect();
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, loginField.getText());
                pstmt.setString(2, passwordField.getText());

                ResultSet rs  = pstmt.executeQuery();

                while (rs.next()) {
                    logonUser.setId(rs.getString("user_id"));
                    logonUser.setEmail(rs.getString("email"));
                    logonUser.setImie(rs.getString("imie"));
                    logonUser.setNazwisko(rs.getString("nazwisko"));
                    logonUser.setTelefon(rs.getString("telefon"));
                    logonUser.setTyp(rs.getString("typ"));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        UserType user = null;
        System.out.println("Click! " + loginField.getText() + " | " + passwordField.getText());

        if(logonUser.getId()!=null){
            if(logonUser.getTyp().equalsIgnoreCase("S"))
                user = UserType.Boss;
            else if (logonUser.getTyp().equalsIgnoreCase("K"))
                user = UserType.Client;
            else if (logonUser.getTyp().equalsIgnoreCase("P"))
                user = UserType.Employee;
            else
                errorLabel.setText("DB: Nieprawidłowy login i/lub hasło");
        }
        else if (!loginField.getText().equals("") && loginField != null) {
            errorLabel.setText("");

            if (loginField.getText().equalsIgnoreCase("admin") && passwordField.getText().equalsIgnoreCase("admin")) {
                user = UserType.Boss;
            } else if (loginField.getText().equalsIgnoreCase("employee") && passwordField.getText().equalsIgnoreCase("employee")) {
                user = UserType.Employee;
            } else if (loginField.getText().equals(passwordField.getText())) {
                user = UserType.Client;
            } else {
                errorLabel.setText("Nieprawidłowy login i/lub hasło");
            }
        } else {
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
                case Client:
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
                case Client:
                    stage.setTitle("Car Rental - Panel Klienta");
                    break;
            }

            stage.setScene(new Scene(loader.load(), 900, 620));
            stage.setResizable(false);
            AController controller = loader.<AController>getController();
            controller.initData(logonUser);

            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private enum UserType {
        Boss,
        Employee,
        Client
    }
}
