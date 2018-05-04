package rental.client;


import javafx.scene.input.MouseEvent;
import rental.AController;


public class ClientController extends AController {

    public ClientController() {
        //TODO: log4j implemented
        System.out.println("---ClientController opened");
    }

    public void myProfilTab_Clicked(MouseEvent mouseEvent) {
        System.out.println("Login: " + login);
    }


}
