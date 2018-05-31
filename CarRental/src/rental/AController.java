package rental;

public abstract class AController {
    protected User user;

    public void initData(User user){
        //TODO: log4j implemented
        System.out.println("Login: " + user.getEmail() + " user.");
        this.user = user;
    }
}
