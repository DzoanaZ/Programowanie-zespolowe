package rental.boss;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import rental.User;
import rental.resources.db.ConnectionDB;

public class OneUser extends AnchorPane {

    @FXML
    private Label nameLabel;
    @FXML
    private Label telLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private ImageView edit;
    @FXML
    private ImageView delete;
    @FXML
    private ImageView star;

    private String user_id;
    private String email;
    private String firstName;
    private String surname;
    private String tel;
    private String type;
    
    private User logonUser;

    public ImageView getStar() {
        return star;
    }

    public void setStar(ImageView star) {
        this.star = star;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public User getLogonUser() {
        return logonUser;
    }

    public void setLogonUser(User logonUser) {
        this.logonUser = logonUser;
    }

    public Label getNameLabel() {
        return nameLabel;
    }

    public void setNameLabel(Label nameLabel) {
        this.nameLabel = nameLabel;
    }

    public Label getTelLabel() {
        return telLabel;
    }

    public void setTelLabel(Label telLabel) {
        this.telLabel = telLabel;
    }

    public Label getEmailLabel() {
        return emailLabel;
    }

    public void setEmailLabel(Label emailLabel) {
        this.emailLabel = emailLabel;
    }

    public ImageView getEdit() {
        return edit;
    }

    public void setEdit(ImageView edit) {
        this.edit = edit;
    }

    public ImageView getDelete() {
        return delete;
    }

    public void setDelete(ImageView delete) {
        this.delete = delete;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public OneUser(User user) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("bossOneUser.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        logonUser = user;
    }

    public void setData(String user_id, String email, String firstName,
            String surname, String tel, String type) {
        this.user_id = user_id;
        this.email = email;
        this.firstName = firstName;
        this.surname = surname;
        this.tel = tel;
        this.type = type;
        if (type.equalsIgnoreCase("S")) {
            this.star.visibleProperty().setValue(Boolean.TRUE);
        } else {
            this.star.visibleProperty().setValue(Boolean.FALSE);
        }
        
        if(logonUser.getId().equals(this.user_id))
        {
            this.edit.disableProperty().setValue(Boolean.TRUE);
            this.edit.setImage(new Image("/rental/resources/images/edit_disabled.png"));
            
            this.delete.disableProperty().setValue(Boolean.TRUE);
            this.delete.setImage(new Image("/rental/resources/images/delete_disabled.png"));
        }
        
    }

    public void setAndPrepareData(String user_id, String email, String firstName,
            String surname, String tel, String type) {
        setData(user_id, email, firstName, surname, tel, type);
        prepareData();
    }

    public void prepareData() {
        this.getNameLabel().setText(this.firstName + " " + this.surname);
        this.getEmailLabel().setText(this.email);
        this.getTelLabel().setText(this.tel);
    }
}
