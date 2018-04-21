package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class Conn {
    private static int localPort = 3306;
    private static Connection conn;

    private static void sshTunnel() throws JSchException {
        JSch jsch = new JSch();
        String sshHost = "188.166.115.189";
        int sshPort = 22;
        String sshUser = "root";
        Session session = jsch.getSession(sshUser, sshHost, sshPort);
        String sshPass = "rocketman";
        session.setPassword(sshPass);

        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);

        session.connect();
        String remoteHost = "127.0.0.1";
        int remotePort = 3306;
        session.setPortForwardingL(localPort, remoteHost, remotePort);
    }

    public static Connection getConnection() throws JSchException, ClassNotFoundException, SQLException {
        if (conn == null) {
            sshTunnel();
            Class.forName("com.mysql.jdbc.Driver");
            String remoteUser = "root";
            String remotePass = "rocketman";
            conn = DriverManager.getConnection("jdbc:mysql://localhost:" + localPort, remoteUser, remotePass);
            System.out.println("Connection established.");
        }
        return conn;
    }
}
