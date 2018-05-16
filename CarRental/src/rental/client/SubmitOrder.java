package rental.client;

import java.io.IOException;
import java.time.LocalDate;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;


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
    
    public SubmitOrder(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("clientSubmitOrder.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
         try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        initializeComponent();
    }
    
    void initializeComponent(){
        //RadioButtons
        transfer.setToggleGroup(group);
        card.setToggleGroup(group);
        cash.setToggleGroup(group);

        //Error informations
        errorLabel.setVisible(false);
        
        //Text limit for code field
        addTextLimiter(code,6);
        
        //Validate date range
        addRentalDateValidate(this.dateOfRent, this.dateOfRent, this.dateOfReturn);
        addRentalDateValidate(this.dateOfReturn, this.dateOfRent, this.dateOfReturn);
        
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
        {
        @Override
        public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1)
            {
            RadioButton chk = (RadioButton)t1.getToggleGroup().getSelectedToggle(); // Cast object to radio button
            System.out.println("Selected Radio Button - "+chk.getText());
            }
        });
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
    private void addRentalDateValidate(final DatePicker sender, final DatePicker picker1, final DatePicker picker2){
        sender.valueProperty().addListener((ov, oldValue, newValue) -> {
            LocalDate date1 = picker1.getValue();
            LocalDate date2 = picker2.getValue();
            
            LocalDate today = LocalDate.now();
            
            if(date1 != null && today.compareTo(date1)>0){
                errorLabel.setText("Niepoprawna data wypożyczenia!");
                errorLabel.setVisible(true);
                errorType = ErrorType.DateRange;
            }
            else if(date2 != null && today.compareTo(date2)>0){
                errorLabel.setText("Niepoprawna data zwrotu!");
                errorLabel.setVisible(true);
                errorType = ErrorType.DateRange;
            }
            else       
            if(date1 != null && date2 != null){
                if(date1.compareTo(date2)>0){
                    String error = sender.hashCode() == dateOfRent.hashCode() ? "wypożyczenia!" : "planowanego zwrotu!";
                    errorLabel.setText("Niepoprawna data "+error);
                    errorLabel.setVisible(true);
                    errorType = ErrorType.DateRange;
                }
                else{
                    if(errorType == ErrorType.DateRange){
                        errorType = null;
                        errorLabel.setText("");
                        errorLabel.setVisible(false);
                    }
                }
                    
            }
        });
    }
    
    enum ErrorType{
        DateRange,
        CarData,
        Payment,
        Code   
    }
}
