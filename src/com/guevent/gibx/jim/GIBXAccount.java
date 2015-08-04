package com.guevent.gibx.jim;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class GIBXAccount {
	private String username, password;
	
	public String getUsername(){ return username; }
	public String getPassword(){ return password; }
	
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	
	public GIBXAccount(JTextField username, JPasswordField password){
		txtUsername = username; txtPassword = password;
	}
	
	@SuppressWarnings("deprecation")
	public void fillUp(){
		username = txtUsername.getText();
		password = txtPassword.getText();
	}
}
