package com.guevent.gibx.jim.f360;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class F360DbConnection {
	
	public static String DB_URL = "C:/GED/f360.accdb";
	
	static{
		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Connection connect(String url){
		try{
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			return DriverManager.getConnection("jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};Dbq=" + url);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean clearTableData(String table){
		Connection con = null;
		Statement st = null;
		try{
			con = new F360DbConnection().connect(DB_URL);
			st = con.createStatement();
			st.executeUpdate("DELETE from " + table);
			
			con.close();
			st.close();
			return true;
		}catch(Exception e){
			try {
				con.close();
				st.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return false;
	}

	
	
}
