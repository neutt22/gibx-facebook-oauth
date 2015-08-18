package com.guevent.gibx.jim.f360;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class MemberSearcher {
	
	private static Connection con;
	private static MemberSearcher memberSeacher;
	
	private MemberSearcher(){
		con = new F360DbConnection().connect(F360DbConnection.DB_URL);
	}
	
	static{
		memberSeacher = new MemberSearcher();
	}
	
	public static MemberSearcher getInstance(){
		if(memberSeacher == null) memberSeacher = new MemberSearcher();
		return memberSeacher;
	}
	
	public void search(String query, DefaultTableModel tableModel, int maxRow){
		String SQL = query;
		try{
			PreparedStatement ps = con.prepareStatement(SQL);
			ps.setMaxRows(maxRow);
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columns = rsmd.getColumnCount();
			while(rs.next()){
				Vector<Object> row = new Vector<Object>(columns);
				for(int i = 1; i <= columns; i++){
					row.addElement(rs.getObject(i));
				}
				tableModel.addRow(row);
			}
			rs.close();
			ps.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void search(String query, String field, DefaultTableModel tableModel, int maxRow){
		String SQL = "SELECT * FROM members WHERE " + field + " like ?";
		try{
			PreparedStatement ps = con.prepareStatement(SQL);
			ps.setString(1, "%" + query + "%");
			System.out.println(ps.executeQuery().toString());
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columns = rsmd.getColumnCount();
			while(rs.next()){
				Vector<Object> row = new Vector<Object>(columns);
				for(int i = 1; i <= columns; i++){
					row.addElement(rs.getObject(i));
				}
				tableModel.addRow(row);
			}
			rs.close();
			ps.close();
			System.out.println("lol");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
