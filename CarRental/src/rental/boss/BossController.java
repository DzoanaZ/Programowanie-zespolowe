package rental.boss;

import javafx.scene.input.MouseEvent;
import rental.AController;

public class BossController extends AController {

    public BossController() {
        //TODO: log4j implemented
        System.out.println("---BossController opened");
    }

    public void myProfilTab_Clicked(MouseEvent mouseEvent) {
        System.out.println("Login: " + login);
    }
}
