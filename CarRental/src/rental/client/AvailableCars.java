package rental.client;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import rental.resources.db.ConnectionDB;

public class AvailableCars extends AnchorPane {

    @FXML
    private VBox carsVBox;
    @FXML
    private Label title;

    private String miasto;

    public AvailableCars(String miasto) {
        this.miasto = miasto;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("clientAvailableCars.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        } finally {
            title.setText("Dostępne samochody - " + this.miasto);
        }
        prepareFields();

    }

    public void prepareFields() {
        String sql = "Select * FROM samochody WHERE miasto LIKE ?";
        ObservableList<OneAvailableCar> items = FXCollections.observableArrayList();
        DecimalFormat df = new DecimalFormat("0.00");
        
        try (Connection conn = ConnectionDB.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, this.miasto);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                OneAvailableCar oac = new OneAvailableCar();
                oac.setCarData(rs.getString("marka") + " " + rs.getString("model"),
                         rs.getString("silnik"),
                         rs.getString("drzwi"),
                         df.format(rs.getBigDecimal("cena")) + " zł",
                         rs.getInt("dostepne") + " / " + rs.getInt("ilosc"),
                         rs.getString("miasto"));
                items.add(oac);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            carsVBox.getChildren().setAll(items);
        }
    }
}
