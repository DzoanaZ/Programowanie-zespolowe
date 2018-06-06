package rental.employee;

import java.io.IOException;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class OneOrder extends AnchorPane{
    
    @FXML
    Label nameOrder;
    @FXML
    Label dateOfRent;
    @FXML
    Label dateOfReturn;
    @FXML
    ComboBox status;
    @FXML
    Label totalCost;
    @FXML
    Label cityRent;
    
    public OneOrder(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("employeeOneOrder.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public Label getNameOrder() {
        return nameOrder;
    }

    public void setNameOrder(String nameOrder) {
        this.nameOrder.setText(nameOrder);
    }

    public Label getDateOfRent() {
        return dateOfRent;
    }

    public void setDateOfRent(String dateOfRent) {
        this.dateOfRent.setText(dateOfRent);
    }

    public Label getDateOfReturn() {
        return dateOfReturn;
    }

    public void setDateOfReturn(String dateofReturn) {
        this.dateOfReturn.setText(dateofReturn);
    }

    public Label getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost.setText(totalCost);
    }

    public Label getCityRent() {
        return cityRent;
    }

    public void setCityRent(String cityRent) {
        this.cityRent.setText(cityRent.toUpperCase());
    }

    public String getSelectedStatus() {
        return status.getSelectionModel().getSelectedItem().toString();
    }

    public ComboBox getStatus(){
        return status;
    }
    
    public void setAllStatus(ObservableList<String> statusItems){
        this.status.getItems().setAll(statusItems);
    }
    
    public void setSelectStatus(String status) {
        this.status.getSelectionModel().select(status);
        this.status.setValue(status);
        //this.status.getSelectionModel().select(1);
    }
    
    
    public void setOrderData(String nameOrder, String dateOfRent, String dateOfReturn, 
                            String totalCost, String status, String cityRent){
        this.setNameOrder(nameOrder);
        this.setDateOfRent(dateOfRent);
        this.setDateOfReturn(dateOfReturn);
        this.setTotalCost(totalCost);
        this.setSelectStatus(status);
        this.setCityRent(cityRent);
    }
}
