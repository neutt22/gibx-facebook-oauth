package com.guevent.gibx.jim.f360;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class MemberAdder {
	
	private static Connection con;
	private static MemberAdder memberAdder;
	
	private MemberAdder(){
		con = new F360DbConnection().connect(F360DbConnection.DB_URL);
	}
	
	static{
		memberAdder = new MemberAdder();
	}
	
	public static MemberAdder getInstance(){
		if(memberAdder == null) memberAdder = new MemberAdder();
		return memberAdder;
	}
	
	public void add(F360Member member){
		String SQL = "INSERT INTO members (" +
				"policy, coc, pin_code, first_name, middle_name, last_name, occupation," +
				"relationship,address, phone, bdate, age, activation_plan, plan," +
				"beneficiary_name,beneficiary_relationship,cover,type) " +
				"VALUES " +
				"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			PreparedStatement ps = con.prepareStatement(SQL);
			ps.setString(1, member.getPolicy());
			ps.setString(2, member.getCoc());
			ps.setString(3, member.getPinCode());
			ps.setString(4, member.getFirstName());
			ps.setString(5, member.getMiddleName());
			ps.setString(6, member.getLastName());
			ps.setString(7, member.getOccupation());
			ps.setString(8, member.getRelationship());
			ps.setString(9, member.getAddress());
			ps.setString(10, member.getPhone());
			ps.setString(11, member.getBirthdate());
			ps.setString(12, member.getAge());
			ps.setString(13, member.getActivationPlan());
			ps.setString(14, member.getPlan());
			ps.setString(15, member.getBeneficiaryName());
			ps.setString(16, member.getBeneficiaryRelationship());
			ps.setString(17, member.getCover());
			ps.setString(18, member.getType());
			ps.executeUpdate();
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
