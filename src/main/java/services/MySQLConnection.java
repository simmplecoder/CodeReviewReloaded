package services;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySQLConnection {
    private static int localPort = 3306;
    private static Connection conn;

    private static MySQLConnection instance;

    private MySQLConnection() {
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
            instance = new MySQLConnection();
        }
        return instance.conn;
    }
}
