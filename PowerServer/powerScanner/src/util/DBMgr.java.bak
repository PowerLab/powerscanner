package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBMgr {

	static DBMgr dbm;
	Connection con;
	Statement stmt;
	ResultSet rs;
	
	String url = "jdbc:mysql://localhost/powerscanner";
	String user = "root";
	String pass = "tntn";

	public static DBMgr getInstance() {

		if (dbm == null)
			dbm = new DBMgr();

		return dbm;

	}
	
	private DBMgr(){//»ý¼ºÀÚ
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Connection getConnection() {
		try {
			con = DriverManager.getConnection(url, user, pass);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			con = null;
			e.printStackTrace();
		}
		return con;
	}

	public void close(Connection con, Statement stmt) {
			try {
				if(stmt != null)
				stmt.close();
				if(con != null)
					con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public void close(Connection con, Statement stmt, ResultSet rs) {
		try {
			if(rs != null)
				rs.close();
			if(stmt != null)
			stmt.close();
			if(con != null)
				con.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close(Connection con, PreparedStatement stmt) {
		try {
			
			if(stmt != null)
			stmt.close();
			if(con != null)
				con.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close(Connection con, PreparedStatement stmt, ResultSet rs) {
		try {
			if(rs != null)
				rs.close();
			if(stmt != null)
			stmt.close();
			if(con != null)
				con.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
