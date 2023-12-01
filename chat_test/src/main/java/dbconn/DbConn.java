package dbconn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConn {
	
	private String url ="jdbc:mysql://192.168.0.132:3306/gunchat?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
	
	private String user="root";
	private String password="1234";
	
	public Connection getConnection() {
		Connection conn = null;
		
		try {
			Class clz = Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {			
			e.printStackTrace(); 
		}		
		
		return conn;
	}
	
	
	
	
	
	

}
