package rental.boss;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import rental.User;
import rental.resources.db.ConnectionDB;

public class ShowUser extends AnchorPane {

    @FXML
    private VBox userVBox;
    @FXML
    private Label title;

    private ObservableList<OneUser> usersList;

    private String userType = null;

    private ObservableList<Node> children;
    private AnchorPane contentBox;

    public ShowUser(String title) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("bossShowUser.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        this.title.setText(title);

        userType = title.equalsIgnoreCase("Pracownicy") ? "P" : "K";
    }

    public void prepareData(User user) {
        usersList = FXCollections.observableArrayList();
        if (userType != null && usersList != null) {
            String sql;
            if (userType.equals("P")) {
                sql = "SELECT * FROM uzytkownicy WHERE typ = ? OR typ =? ORDER BY nazwisko ASC";
            } else {
                sql = "SELECT * FROM uzytkownicy WHERE typ = ? ORDER BY nazwisko ASC";
            }
            try (Connection conn = ConnectionDB.connect();
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, userType);
                if (userType.equals("P")) {
                    pstmt.setString(2, "S");
                }

                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    OneUser temp = new OneUser(user);
                    temp.setAndPrepareData(rs.getString("user_id"), rs.getString("email"),
                            rs.getString("imie"), rs.getString("nazwisko"),
                            rs.getString("telefon"), rs.getString("typ"));

                    usersList.add(temp);
                }
                userVBox.getChildren().setAll(usersList);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            attachEvents(user);
            children = FXCollections.observableArrayList();
            children.setAll(this.getChildren());
        }
    }

    public void attachEvents(User user) {
        for (OneUser temp : usersList) {
            //Usuwanie
            temp.getDelete().setOnMouseClicked((event) -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Wymagane potwierdzenie");
                alert.setHeaderText("Czy na pewno chcesz usunąc użytkownika: " + temp.getFirstName() + " " + temp.getSurname() + "?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    String query = "DELETE FROM uzytkownicy WHERE user_id = " + temp.getUser_id();
                    try (Connection conn = ConnectionDB.connect();
                            PreparedStatement pstmt = conn.prepareStatement(query)) {

                        pstmt.execute();

                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Usuwanie");
                        alert.setHeaderText("Użytkownik został pomyslnie usunięty.");
                        alert.show();
                        prepareData(user);

                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Błąd");
                        alert.setHeaderText("Nie udało się zapisać nowych danych. Spróbuj ponownie później.");
                        alert.show();
                    }
                }
            });

            //Edytowanie
            temp.getEdit().setOnMouseClicked((event) -> {
                //Nowe okno Person
                NewPerson edit = new NewPerson();
                edit.getEmailField().setText(temp.getEmail());
                edit.getFirstNameField().setText(temp.getFirstName());
                edit.getSurnameField().setText(temp.getSurname());
                edit.getTelField().setText(temp.getTel());
                edit.getTitle().setText("Edytuj użytkownika");

                if (temp.getType().equalsIgnoreCase("S")) {
                    edit.getBossRadio().setSelected(true);
                    edit.getEmployeeRadio().setSelected(false);
                } else if (temp.getType().equalsIgnoreCase("K")) {
                    edit.getBossRadio().setSelected(false);
                    edit.getEmployeeRadio().setSelected(false);
                    edit.getBossRadio().setVisible(false);
                    edit.getEmployeeRadio().setVisible(false);
                    edit.setPersonType("K");
                } else {
                    edit.getEmployeeRadio().setSelected(true);
                    edit.getBossRadio().setSelected(false);
                }
                edit.getAddButton().setText("Zatwierdź");
                this.getChildren().setAll(edit);

                edit.getAddButton().setOnMouseClicked((event2) -> {
                    if (!edit.getFirstNameField().getText().equals("") && edit.getFirstNameField().getText() != null
                            && !edit.getSurnameField().getText().equals("") && edit.getSurnameField().getText() != null
                            && !edit.getTelField().getText().equals("") && edit.getTelField().getText() != null
                            && !edit.getEmailField().getText().equals("") && edit.getEmailField().getText() != null
                            && !edit.getPersonType().equals("") && edit.getPersonType() != null) {

                        String sql = "UPDATE uzytkownicy SET email = ?, imie = ?, "
                                + "nazwisko = ?, telefon = ?, typ = ? "
                                + "WHERE user_id = ?";

                        try (Connection conn = ConnectionDB.connect();
                                PreparedStatement pstmt = conn.prepareStatement(sql)) {

                            pstmt.setString(1, edit.getEmailField().getText());
                            
                            String haslo = "klient";
                            if (edit.getPersonType().equalsIgnoreCase("pracownik")) {
                                haslo = "pracownik";
                            } else if (edit.getPersonType().equalsIgnoreCase("szef")){
                                haslo = "szef";
                            }

                            //pstmt.setString(2, haslo);
                            pstmt.setString(2, edit.getFirstNameField().getText());
                            pstmt.setString(3, edit.getSurnameField().getText());
                            pstmt.setString(4, edit.getTelField().getText());
                            String typ = null;

                            if (edit.getPersonType().equalsIgnoreCase("pracownik")) {
                                typ = "P";
                            } else if (edit.getPersonType().equalsIgnoreCase("szef")) {
                                typ = "S";
                            } else {
                                typ = "K";
                            }
                            pstmt.setString(5, typ);
                            pstmt.setString(6, temp.getUser_id());
                            pstmt.execute();

                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Potwierdzenie");
                            alert.setHeaderText("Dane zaktualizowane pomyślnie.");
                            alert.show();
                            this.getChildren().setAll(children);
                            this.prepareData(user);

                        } catch (SQLException e) {
                            System.out.println(e.getMessage());
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Błąd");
                            alert.setHeaderText("Nie można edytować danych tego użytkownika.");
                            alert.show();
                        }

                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Uwaga");
                        alert.setHeaderText("Sprawdź poprawność danych");
                        alert.show();
                    }
                });
            });
        }

    }
}