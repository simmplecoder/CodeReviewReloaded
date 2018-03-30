package dreamteam;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class Dao {

	static String sshUser = "root";
	static String sshPass = "rocketman";
	static String sshHost = "188.166.115.189";
	static int sshPort = 22;
	
	static String remoteHost = "127.0.0.1";
	static int localPort = 3306;
	static int remotePort = 3306;
	static String remoteUser = "root";
	static String remotePass = "rocketman";
	
	static Connection conn = null;
	
	public static void sshTunnel() throws JSchException {
		JSch jsch = new JSch();
		Session session = jsch.getSession(sshUser	, sshHost, sshPort);
		session.setPassword(sshPass);
		
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		
		session.connect();
		session.setPortForwardingL(localPort, remoteHost, remotePort);
	}
	
	public static Connection getConnection() throws JSchException, ClassNotFoundException, SQLException {
		sshTunnel();
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mysql://localhost:"+localPort, remoteUser, remotePass);
		System.out.println("Connection established.");
		return conn;
	}
	
	public void closeConnection() throws SQLException {
		conn.close();
	}
}
