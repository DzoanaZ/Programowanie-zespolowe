package rental.boss;

import javafx.scene.control.TextField;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Optional;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import rental.resources.db.ConnectionDB;

public class NewCar extends AnchorPane {

    @FXML
    Label titleLabel;
    @FXML
    TextField brandField;
    @FXML
    TextField engineField;
    @FXML
    TextField doorsField;
    @FXML
    TextField priceField;
    @FXML
    TextField numberField;
    @FXML
    TextField cityField;
    @FXML
    TextField modelField;
    @FXML
    TextField availabilityField;
    @FXML
    Label backLabel;
    @FXML
    Button button;

    private String samochod_id;
    private ObservableList<Node> children;
    private AnchorPane contentBox;

    public String getSamochod_id() {
        return samochod_id;
    }

    public void prepareDataById(String samochod_id) {
        this.samochod_id = samochod_id;

        String sql = "SELECT * FROM samochody WHERE samochod_id = " + this.samochod_id;

        try (Connection conn = ConnectionDB.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                OneAvailableCar oac = new OneAvailableCar();
                brandField.setText(rs.getString("marka"));
                modelField.setText(rs.getString("model"));
                engineField.setText(rs.getString("silnik"));
                doorsField.setText(rs.getString("drzwi"));
                numberField.setText(rs.getString("ilosc"));
                availabilityField.setText(rs.getString("dostepne"));
                priceField.setText(new DecimalFormat("#0.##").format(rs.getBigDecimal("cena")));
                cityField.setText(rs.getString("miasto"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {

        }
    }

    public NewCar() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("bossNewCar.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        titleLabel.setText("Edytuj dane pojazdu");
        backLabel.visibleProperty().set(true);
        button.setText("Zatwierdź");

        backLabel.setOnMouseClicked((event) -> {
            contentBox.getChildren().setAll(children);
            ((AvailableCars) contentBox).prepareFields();
        });

        button.setOnMouseClicked((event) -> {

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Wymagane potwierdzenie");
            alert.setHeaderText("Czy na pewno chcesz zaktualizować pojazd?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {

                String sql = "UPDATE samochody SET marka = ?, model = ?, silnik = ?, drzwi = ?, "
                        + "ilosc = ?, dostepne = ?, cena = ?, miasto = ? "
                        + "WHERE samochod_id = " + this.samochod_id;

                try (Connection conn = ConnectionDB.connect();
                        PreparedStatement pstmt = conn.prepareStatement(sql)) {

                    pstmt.setString(1, this.brandField.getText());
                    pstmt.setString(2, this.modelField.getText());
                    pstmt.setString(3, this.engineField.getText());
                    pstmt.setString(4, this.doorsField.getText());
                    pstmt.setString(5, this.numberField.getText());
                    pstmt.setString(6, this.availabilityField.getText());
                    pstmt.setString(7, this.priceField.getText());
                    pstmt.setString(8, this.cityField.getText());
                    pstmt.execute();
                    
                    contentBox.getChildren().setAll(children);
                    ((AvailableCars) contentBox).prepareFields();

                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Błąd");
                    alert.setHeaderText("Nie udało się zapisać nowych danych. Spróbuj ponownie później.");
                    alert.show();
                }
            }
        });
    }

    public void setChildren(ObservableList<Node> children, AnchorPane contentBox) {
        this.children = children;
        this.contentBox = contentBox;
    }
}
