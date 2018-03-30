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

public class Conn {

	String sshUser = "root";
	String sshPass = "rocketman";
	String sshHost = "188.166.115.189";
	int sshPort = 22;
	
	String remoteHost = "127.0.0.1";
	int localPort = 3306;
	int remotePort = 3306;
	String remoteUser = "root";
	String remotePass = "rocketman";
	
	Connection conn = null;
	
	public void sshTunnel() throws JSchException {
		JSch jsch = new JSch();
		Session session = jsch.getSession(sshUser	, sshHost, sshPort);
		session.setPassword(sshPass);
		
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		
		session.connect();
		session.setPortForwardingL(localPort, remoteHost, remotePort);
	}
	
	public Connection getConnection() throws JSchException, ClassNotFoundException, SQLException {
		sshTunnel();
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mysql://localhost:"+localPort, remoteUser, remotePass);
		System.out.println("Connection established.");
		return conn;
	}
}
