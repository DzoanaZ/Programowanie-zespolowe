package rental.resources.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    private static ConnectionDB INSTANCE;
    private static Connection conn;
 
    private ConnectionDB(){
        conn = null;
    }
 
    public static ConnectionDB getInstance(){
        if(INSTANCE==null)
            INSTANCE = new ConnectionDB();
        return INSTANCE;
    }
   
    
    public static Connection connect() {
        String url = "jdbc:sqlite:src/rental/resources/db/database.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;         
    }
}
