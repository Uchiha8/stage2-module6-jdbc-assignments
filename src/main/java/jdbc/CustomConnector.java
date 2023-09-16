package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CustomConnector {
    private final ResourceBundle rb = ResourceBundle.getBundle("app");

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(rb.getString("postgres.driver"));
        return DriverManager.getConnection(rb.getString("postgres.url"),
                rb.getString("postgres.name"), rb.getString("postgres.password"));
    }

    public Connection getConnection(String url, String user, String password)
            throws ClassNotFoundException, SQLException {
        Class.forName(rb.getString("postgres.driver"));
        return DriverManager.getConnection(url, user, password);
    }
}
