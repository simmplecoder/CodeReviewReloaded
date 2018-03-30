package dreamteam;

import java.sql.Connection;
import java.sql.SQLException;

import com.jcraft.jsch.JSchException;

public class Amsterdam {
	
	private static Connection conn;
	
	public static Connection getConn() throws ClassNotFoundException, JSchException, SQLException {
		if (conn == null) {
			conn = new Conn().getConnection();
		}
		return conn;
	}
}
