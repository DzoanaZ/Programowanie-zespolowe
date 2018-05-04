package rental;

public abstract class AController {
    protected String login;

    public void initData(String customer){
        //TODO: log4j implemented
        System.out.println("Login: " + customer + " user.");
        this.login = customer;
    }
}
