package com.guevent.gibx.jim.utils;

import java.io.File;

import javax.swing.JTextField;
import javax.swing.text.StyledDocument;

import org.ini4j.Wini;

import com.guevent.gibx.jim.MainView;


public class Utils {
	
	public static String[] getSearchCat(){
		return new String[]{"POLICY", "COC", "PIN CODE", "FIRST NAME", "MIDDLE NAME", "LAST NAME", "OCCUPATION", "RELATIONSHIP", "ADDRESS", "PHONE"
				, "BDATE", "AGE", "ACTIVATION", "PLAN", "BNFRY NAME", "BNFRY REL", "COVER", "TYPE"};
	}
	
	public static String getDbField(String field){
		if(field.equals("POLICY")) return "policy";
		if(field.equals("COC")) return "coc";
		if(field.equals("PIN CODE")) return "pin_code";
		if(field.equals("FIRST NAME")) return "first_name";
		if(field.equals("MIDDLE NAME")) return "middle_name";
		if(field.equals("LAST NAME")) return "last_name";
		if(field.equals("OCCUPATION")) return "occupation";
		if(field.equals("RELATIONSHIP")) return "relationship";
		if(field.equals("ADDRESS")) return "address";
		if(field.equals("PHONE")) return "phone";
		if(field.equals("BDATE")) return "bdate";
		if(field.equals("AGE")) return "age";
		if(field.equals("ACTIVATION")) return "activation_plan";
		if(field.equals("PLAN")) return "plan";
		if(field.equals("BNFRY NAME")) return "beneficiary_name";
		if(field.equals("BNFRY REL")) return "beneficiary_relationship";
		if(field.equals("COVER")) return "cover";
		if(field.equals("TYPE")) return "type";
		return null;
	}
	
	public static void updateToken(String token, StyledDocument txtBuffer){
		try {
			txtBuffer.insertString(txtBuffer.getLength(), "Wring to: " + MainView.APPDATA + MainView.INI_NAME + "\n", null);
			Wini ini = new Wini(new File(MainView.APPDATA + MainView.INI_NAME));
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
			Wini ini = new Wini(new File(MainView.APPDATA + MainView.INI_NAME));
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
