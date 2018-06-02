package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Conn {
    private static int localPort = 3306;
    private static Connection conn;

    private static Conn instance;

    private Conn() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:" + localPort + "/CodeReviewTool",
                    "root",
                    "crt2018A");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection connect() {
        if (instance == null) {
            instance = new Conn();
        }
        return instance.conn;
    }
}
