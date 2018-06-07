package rental.client;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import rental.User;
import rental.resources.db.ConnectionDB;

public class HistoryOrders extends AnchorPane{
    @FXML
    private ListView listHistoryOrders;
    
    public HistoryOrders(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("clientHistoryOrders.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    public void prepareData(User user) {

        ObservableList<OneOrder> items = FXCollections.observableArrayList();
        if (user.getId() != null && !user.getId().equals("")) {
            String sql = "SELECT marka, model, data_wypozyczenia, planowana_data_zwrotu, samochody.cena, status, miasto FROM wypozyczenia "
                    + "INNER JOIN uzytkownicy ON uzytkownicy.user_id = wypozyczenia.uzytkownik_id "
                    + "INNER JOIN samochody ON samochody.samochod_id = wypozyczenia.samochod_id "
                    + "WHERE uzytkownik_id = " + user.getId()+" AND (status = 'Zakończone' OR status = 'Anulowane') ORDER BY data_wypozyczenia DESC";

            try (Connection conn = ConnectionDB.connect();
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {

                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    BigDecimal cena = rs.getBigDecimal("cena");
                    DecimalFormat df = new DecimalFormat("0.00");
                    
                    OneOrder temp = new OneOrder();
                    temp.setDateReturnLabel("Data zwrotu:");
                    temp.setOrderData(rs.getString("marka") + " " + rs.getString("model"),
                            rs.getString("data_wypozyczenia"),
                            rs.getString("planowana_data_zwrotu"),
                            df.format(cena) + " zł",
                            rs.getString("status"),
                            rs.getString("miasto"));
                    items.add(temp);
                }
                listHistoryOrders.setItems(items);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    
}
