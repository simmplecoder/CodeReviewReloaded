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
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/CodeReviewTool",
                    "root",
                    "crt2018");
        } catch (SQLException e) {
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
