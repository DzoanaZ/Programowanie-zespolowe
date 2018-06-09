package rental.employee;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import rental.User;
import rental.resources.db.ConnectionDB;

public class OrdersExpect extends AnchorPane {

    @FXML
    private ListView listProcessOrders;

    private ObservableList<MenuItem> statusItems;
    private Map<String, MenuButton> statusmap;
    private ObservableList<OneOrderExpect> oneOrderExpect;

    public OrdersExpect() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("employeeOrdersExpect.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        statusmap = new HashMap();
        oneOrderExpect = FXCollections.observableArrayList();
    }

    public void prepareData(User user) {
        ObservableList<rental.employee.OneOrderExpect> items = FXCollections.observableArrayList();
        if (user.getId() != null && !user.getId().equals("")) {

            String sql = "SELECT * FROM v_pracownik_nowe_zamowienia";

            try (Connection conn = ConnectionDB.connect();
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {

                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {

                    sql = "SELECT * FROM statusy WHERE status<>'Nowe'";

                    statusItems = FXCollections.observableArrayList();

                    PreparedStatement pstmta = conn.prepareStatement(sql);
                    ResultSet rsa = pstmta.executeQuery();

                    while (rsa.next()) {
                        statusItems.add(new MenuItem(rsa.getString("status")));
                    }

                    BigDecimal cena = rs.getBigDecimal("cena");
                    DecimalFormat df = new DecimalFormat("0.00");

                    OneOrderExpect temp = new OneOrderExpect();

                    temp.setOrderData(rs.getString("marka") + " " + rs.getString("model"),
                            rs.getString("data_wypozyczenia"),
                            rs.getString("planowana_data_zwrotu"),
                            df.format(cena) + " zł",
                            rs.getString("status"),
                            rs.getString("miasto"),
                            rs.getString("wypozyczenie_id"));
                    temp.getMenuStatus().getItems().setAll(statusItems);
                    items.add(temp);
                }
                listProcessOrders.setItems(items);
                oneOrderExpect.setAll(items);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        for (OneOrderExpect order : oneOrderExpect) {
            for (MenuItem item : order.getMenuStatus().getItems()) {
                item.setOnAction(changeMenu(order, user));
            }
        }
    }

    private EventHandler<ActionEvent> changeMenu(OneOrderExpect order, User user) {
        return new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {

                if (event.getSource() instanceof MenuItem) {

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Wymagane potwierdzenie");
                    alert.setHeaderText("Czy na pewno chcesz zmienić status zamowienia?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {

                        String sql = "UPDATE wypozyczenia SET status = ? WHERE wypozyczenie_id = ?";
                        MenuItem temp = (MenuItem) event.getSource();

                        String status = temp.getText();
                        String wypozyczenie_id = order.getOrderId();

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

                    }
                    prepareData(user);
                }
            }
        };
    }
}
