package com.guevent.gibx.jim.f360;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class MemberAdder {
	
	private Connection con;
	
	public MemberAdder(){
		con = new F360DbConnection().connect("J:\\F360 Daily Reports\\f360.accdb");
		System.out.println("connected");
	}
	
	public void add(F360Member member){
		String SQL = "INSERT INTO members (policy, coc) VALUES ('" + member.getPolicy() + "','" + member.getCoc() + "')";
		try {
			Statement st = con.createStatement();
			st.executeUpdate(SQL);
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void closeConnection(){
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
