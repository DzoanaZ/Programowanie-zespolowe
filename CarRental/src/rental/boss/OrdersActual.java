package rental.boss;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import rental.User;
import rental.resources.db.ConnectionDB;

public class OrdersActual extends AnchorPane {

    @FXML
    private ListView listActualOrders;

    private ObservableList<String> statusItems;
    private Map<String, ComboBox> statusmap;

    public OrdersActual() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("bossOrdersActual.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        statusItems = FXCollections.observableArrayList();
        statusmap = new HashMap();
        String sql = "SELECT * from statusy WHERE status <> 'Zatwierdzone'";

        try (Connection conn = ConnectionDB.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                statusItems.add(rs.getString("status"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void prepareData(User user) {
        ObservableList<OneOrder> items = FXCollections.observableArrayList();
        if (user.getId() != null && !user.getId().equals("")) {
            String sql = "SELECT * FROM v_pracownik_aktualne_zamowienia";

            try (Connection conn = ConnectionDB.connect();
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {

                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    BigDecimal cena = rs.getBigDecimal("cena");
                    DecimalFormat df = new DecimalFormat("0.00");

                    OneOrder temp = new OneOrder();
                    temp.setAllStatus(statusItems);
                    temp.setOrderData(rs.getString("marka") + " " + rs.getString("model"),
                            rs.getString("data_wypozyczenia"),
                            rs.getString("planowana_data_zwrotu"),
                            df.format(cena) + " zł",
                            rs.getString("status"),
                            rs.getString("miasto"),
                            rs.getString("wypozyczenie_id")
                    );
                    items.add(temp);
                    statusmap.put(temp.getOrderId(), temp.getStatus());
                }
                listActualOrders.setItems(items);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        for (ComboBox combo : statusmap.values()) {
            combo.valueProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                    
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Wymagane potwierdzenie");
                    alert.setHeaderText("Czy na pewno chcesz zmienić status zamowienia?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK){
                        
                    String sql = "UPDATE wypozyczenia SET status = ? WHERE wypozyczenie_id = ?";
                    String status = combo.getSelectionModel().getSelectedItem().toString();
                    String wypozyczenie_id = (getKeysByValue(statusmap, combo).toArray())[0].toString();

                    try (Connection conn = ConnectionDB.connect();
                            PreparedStatement pstmt = conn.prepareStatement(sql)) {

                        pstmt.setString(1, status);
                        pstmt.setString(2, wypozyczenie_id);
                        pstmt.execute();

                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                        
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Błąd zmiany statusu!");
                        alert.setHeaderText("Spróbuj ponownie później.");
                        alert.show();
                    }
     
                    } else {
                       combo.selectionModelProperty().setValue(oldValue);
                    }
                    
                    prepareData(user);
                }
            });
        }
    }

    public static <T, E> Set<T> getKeysByValue(Map<T, E> map, E value) {

        Set<T> keys = new HashSet<>();

        for (Map.Entry<T, E> entry : map.entrySet()) {

            if (value.equals(entry.getValue())) {

                keys.add(entry.getKey());

            }
        }
        return keys;
    }
}
