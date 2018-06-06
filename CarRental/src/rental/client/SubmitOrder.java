package rental.client;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import rental.User;
import rental.resources.db.ConnectionDB;

public class SubmitOrder extends AnchorPane {

    //Car data
    @FXML
    private ComboBox brand;
    @FXML
    private ComboBox model;
    @FXML
    private ComboBox engine;
    @FXML
    private ComboBox city;
    //END Car data

    //Payment
    @FXML
    private RadioButton transfer;
    @FXML
    private RadioButton cash;
    @FXML
    private RadioButton card;
    //END Payment

    //Date range
    @FXML
    private DatePicker dateOfRent;
    @FXML
    private DatePicker dateOfReturn;
    //END Date range

    //Others
    @FXML
    private Label price;

    @FXML
    private TextField code;
    @FXML
    private Button confirmCode;

    @FXML
    private Button submit;

    @FXML
    private Label errorLabel;
    //END others

    private final ToggleGroup group = new ToggleGroup();
    private ErrorType errorType = null;

    private User user;
    private int selectedCarID;
    private String payment;

    public SubmitOrder() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("clientSubmitOrder.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
    }

    void initializeComponent(User user) {
        if(user!=null)
            this.user = user;
        //Group RadioButtons
        transfer.setToggleGroup(group);
        card.setToggleGroup(group);
        cash.setToggleGroup(group);

        //Error informations
        errorLabel.setVisible(false);

        //Text limit for rabate code field
        addTextLimiter(code, 6);

        //Validate date range
        addRentalDateValidate(this.dateOfRent, this.dateOfRent, this.dateOfReturn);
        addRentalDateValidate(this.dateOfReturn, this.dateOfRent, this.dateOfReturn);

        //Events Listeners
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) {
                RadioButton chk = (RadioButton) t1.getToggleGroup().getSelectedToggle(); // Cast object to radio button

                switch (chk.getText().toLowerCase()) {
                    case "przelew":
                        payment = "Przelew";
                        break;
                    case "gotówka":
                        payment = "Gotówka";
                        break;
                    case "karta płatnicza":
                        payment = "Karta";
                        break;
                    default:
                        payment = null;
                        break;
                }
                System.out.println("Selected Radio Button - " + payment);
            }
        });

        city.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                cityChanged();
            }
        });

        brand.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                brandChanged();
            }
        });

        model.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                modelChanged();
            }
        });

        engine.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                engineChanged();
            }
        });
        
        
        prepareFields();
    }

    private void addTextLimiter(final TextField tf, final int maxLength) {
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (tf.getText().length() > maxLength) {
                    String temp = tf.getText().substring(0, maxLength);
                    tf.setText(temp);
                }
            }
        });
    }

    private void addRentalDateValidate(final DatePicker sender, final DatePicker picker1, final DatePicker picker2) {
        sender.valueProperty().addListener((ov, oldValue, newValue) -> {
            LocalDate date1 = picker1.getValue();
            LocalDate date2 = picker2.getValue();

            LocalDate today = LocalDate.now();

            if (date1 != null && today.compareTo(date1) > 0) {
                errorLabel.setText("Niepoprawna data wypożyczenia!");
                errorLabel.setVisible(true);
                errorType = ErrorType.DateRange;
            } else if (date2 != null && today.compareTo(date2) > 0) {
                errorLabel.setText("Niepoprawna data zwrotu!");
                errorLabel.setVisible(true);
                errorType = ErrorType.DateRange;
            } else if (date1 != null && date2 != null) {
                if (date1.compareTo(date2) > 0) {
                    String error = sender.hashCode() == dateOfRent.hashCode() ? "wypożyczenia!" : "planowanego zwrotu!";
                    errorLabel.setText("Niepoprawna data " + error);
                    errorLabel.setVisible(true);
                    errorType = ErrorType.DateRange;
                } else {
                    if (errorType == ErrorType.DateRange) {
                        errorType = null;
                        errorLabel.setText("");
                        errorLabel.setVisible(false);
                    }
                }

            }
        });
    }

    enum ErrorType {
        DateRange,
        CarData,
        Payment,
        Code
    }

    private void prepareFields() {
        ObservableList<String> items = FXCollections.observableArrayList();
        String sql = "Select * FROM miasta ORDER BY miasto ASC";

        try (Connection conn = ConnectionDB.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                items.add(rs.getString("miasto"));

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            city.getItems().setAll(items);
        }

    }

    private void cityChanged() {
        ObservableList<String> items = FXCollections.observableArrayList();
        String sql = "Select * FROM samochody WHERE miasto = ? AND dostepne > 0 ORDER BY marka ASC";

        try (Connection conn = ConnectionDB.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, city.getSelectionModel().getSelectedItem().toString());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                items.add(rs.getString("marka"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            brand.getItems().setAll(items);
        }
    }

    private void brandChanged() {
        ObservableList<String> items = FXCollections.observableArrayList();
        String sql = "Select * FROM samochody WHERE miasto = ? AND marka = ? AND dostepne > 0  ORDER BY model ASC";

        try (Connection conn = ConnectionDB.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, city.getSelectionModel().getSelectedItem().toString());
            pstmt.setString(2, brand.getSelectionModel().getSelectedItem().toString());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                items.add(rs.getString("model"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            model.getItems().setAll(items);
        }
    }

    private void modelChanged() {
        ObservableList<String> items = FXCollections.observableArrayList();
        String sql = "Select * FROM samochody WHERE miasto = ? AND marka = ? AND model = ? AND dostepne > 0 ORDER BY silnik ASC";

        try (Connection conn = ConnectionDB.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, city.getSelectionModel().getSelectedItem().toString());
            pstmt.setString(2, brand.getSelectionModel().getSelectedItem().toString());
            pstmt.setString(3, model.getSelectionModel().getSelectedItem().toString());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                items.add(rs.getString("silnik"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            engine.getItems().setAll(items);
        }
    }

    private void engineChanged() {
        ObservableList<String> items = FXCollections.observableArrayList();
        String sql = "Select samochod_id, cena FROM samochody WHERE miasto = ? AND marka = ? AND model = ? AND silnik = ? AND dostepne > 0  ORDER BY silnik ASC";
        int tempID = -1;
        BigDecimal cena = new BigDecimal(0);
        try (Connection conn = ConnectionDB.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, city.getSelectionModel().getSelectedItem().toString());
            pstmt.setString(2, brand.getSelectionModel().getSelectedItem().toString());
            pstmt.setString(3, model.getSelectionModel().getSelectedItem().toString());
            pstmt.setString(4, engine.getSelectionModel().getSelectedItem().toString());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                tempID = rs.getInt("samochod_id");
                cena = rs.getBigDecimal("cena");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            this.selectedCarID = tempID;

            if (this.selectedCarID > -1) {
                DecimalFormat df = new DecimalFormat("0.00");
                this.price.setText(df.format(cena) + " zł");
            }
        }
    }

    @FXML
    public void submit_Clicked() {
        if ((this.errorLabel.isVisible() || !this.errorLabel.getText().equals(""))
                && (payment == null || payment.equals(""))
                && (selectedCarID < 0)) {
            return;
        }
        
        String sql = "INSERT INTO wypozyczenia (samochod_id, uzytkownik_id, data_wypozyczenia, planowana_data_zwrotu, data_zamowienia, metoda_platnosci)"
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionDB.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, this.selectedCarID);
            pstmt.setInt(2, Integer.parseInt(user.getId()));
            pstmt.setObject(3, dateOfRent.getValue());
            pstmt.setObject(4, dateOfReturn.getValue());
            LocalDate today = LocalDate.now();
            pstmt.setObject(5, today);
//pstmt.setObject(5, today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            pstmt.setString(6, payment);
            
            pstmt.execute();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Zamówienie");
            alert.setHeaderText("Nowe zamówienie zostało wysłane!");
            alert.show();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd zamówienia!");
            alert.setHeaderText("Sprawdź poprawność wprowadzonych danych.");
            alert.show();
        } 
    }
}
