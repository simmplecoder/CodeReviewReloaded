package services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {
    private static int localPort = 3306;
    private static Connection conn;

    private static MySQLConnection instance;

    private static final Logger logger = LogManager.getLogger(MySQLConnection.class);

    private MySQLConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:" + localPort + "/CodeReviewTool",
                    "root",
                    "crt2018");
            logger.info("Connected to MySQL database");
        } catch (Exception e) {
            logger.error("Failed to setup connection to MySQL database");
            logger.error("Exception message: " + e);
        }
    }

    public static Connection connect() {
        try {
            if (instance == null || instance.conn.isClosed() == true)
                instance = new MySQLConnection();

            if (instance.conn.isClosed() == true)
                logger.info("Mysql connection was closed.");

            logger.error("Created mysql connection.");
        } catch (SQLException e) {
            logger.error("Failed to initialize mysql with exception: " + e);
        }
        return instance.conn;
    }
}
