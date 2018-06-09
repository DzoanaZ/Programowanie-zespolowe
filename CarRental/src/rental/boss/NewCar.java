package rental.boss;

import javafx.scene.control.TextField;
import java.io.IOException;
import java.math.BigDecimal;
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
import javafx.scene.control.ComboBox;
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
    ComboBox cityField;
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
                cityField.getSelectionModel().select(rs.getString("miasto"));
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
        titleLabel.setText("Wprowadź nowy pojazd");
        backLabel.visibleProperty().set(true);
        button.setText("Dodaj");
        backLabel.setVisible(false);

        cityField.getItems().setAll("Dynów", "Jasło", "Rzeszów", "Stalowa Wola");

        button.setOnMouseClicked((event) -> {

            if (this.brandField.getText() != null && !this.brandField.getText().equals("")
                    && this.modelField.getText() != null && !this.modelField.getText().equals("")
                    && this.engineField.getText() != null && !this.engineField.getText().equals("")
                    && this.numberField.getText() != null && !this.numberField.getText().equals("")
                    && this.availabilityField.getText() != null && !this.availabilityField.getText().equals("")
                    && this.priceField.getText() != null && !this.priceField.getText().equals("")
                    && this.cityField.getSelectionModel().getSelectedItem().toString() != null
                    && !this.cityField.getSelectionModel().getSelectedItem().toString().equals("")) {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Wymagane potwierdzenie");
                alert.setHeaderText("Czy na pewno chcesz dodać nowy pojazd?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {

                    String sql = "INSERT INTO samochody (marka, model, silnik, drzwi, "
                            + "ilosc, dostepne, cena, miasto) VALUES (?,?,?,?,?,?,?,?)";

                    try (Connection conn = ConnectionDB.connect();
                            PreparedStatement pstmt = conn.prepareStatement(sql)) {

                        pstmt.setString(1, removeSpaceOfEnd(this.brandField.getText()));
                        pstmt.setString(2, removeSpaceOfEnd(this.modelField.getText()));
                        pstmt.setString(3, removeSpaceOfEnd(this.engineField.getText()));
                        pstmt.setString(4, removeSpaceOfEnd(this.doorsField.getText()));
                        pstmt.setString(5, removeSpaceOfEnd(this.numberField.getText()));
                        pstmt.setString(6, removeSpaceOfEnd(this.availabilityField.getText()));
                        pstmt.setBigDecimal(7, new BigDecimal(removeSpaceOfEnd(this.priceField.getText().replaceAll(",", "."))));
                        pstmt.setString(8, removeSpaceOfEnd(this.cityField.getSelectionModel().getSelectedItem().toString()));
                        pstmt.execute();

                        clearFields();

                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                        alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Błąd");
                        alert.setHeaderText("Nie udało się dodać nowego pojazdu. Spróbuj ponownie później.");
                        alert.show();
                    }
                }
            }else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Błąd");
                alert.setHeaderText("Sprawdź poprawność danych i spróbuj ponownie.");
                alert.show();
            }
        });
    }

    private void clearFields() {
        brandField.clear();
        modelField.clear();
        engineField.clear();
        doorsField.clear();
        numberField.clear();
        availabilityField.clear();
        priceField.clear();
        cityField.getSelectionModel().clearSelection();
    }
    
   private static String removeSpaceOfEnd(String word) {
        char[] tab = word.toCharArray();
        String noneSpace="";
        boolean someLetter = false;
        final int LENGTH = tab.length-1;
        for(int i=0; i<=LENGTH; i++){
            if(tab[i]!=' '){
                someLetter=true;
                noneSpace+=tab[i];
                continue;
            }
            else if(i<LENGTH && tab[i+1]!=' ' && someLetter)
            {
                noneSpace+=tab[i];
                continue;
            }
        }

        return noneSpace;
    }
}
