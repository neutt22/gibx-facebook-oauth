package com.guevent.gibx.jim.utils;

import java.io.File;

import javax.swing.JTextField;
import javax.swing.text.StyledDocument;

import org.ini4j.Wini;

import com.guevent.gibx.jim.FBView;


public class Utils {
	
	public static void updateToken(String token, StyledDocument txtBuffer){
		try {
			txtBuffer.insertString(txtBuffer.getLength(), "Wring to: " + FBView.APPDATA + FBView.INI_NAME + "\n", null);
			Wini ini = new Wini(new File(FBView.APPDATA + FBView.INI_NAME));
			ini.put("main", "user_token", token);
			ini.store();
			txtBuffer.insertString(txtBuffer.getLength(), "Done. User token update to:\n", null);
			txtBuffer.insertString(txtBuffer.getLength(), token + "\n\n", null);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static void showToken(JTextField txtToken){
		try {
			Wini ini = new Wini(new File(FBView.APPDATA + FBView.INI_NAME));
			String user_token = ini.get("main", "user_token");
			txtToken.setText(user_token);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static String getPasswordInStar(String pass){
		String star = "";
		for(int i = 0; i < pass.length(); i++){
			star += "*";
		}	
		return star;
	}

}
