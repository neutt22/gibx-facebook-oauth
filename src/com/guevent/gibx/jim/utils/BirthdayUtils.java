package com.guevent.gibx.jim.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BirthdayUtils {
	
	private static List<String> birthdayQuotes = new ArrayList<String>();
	private static Random random = new Random();
	private static boolean loaded = false;
	private static final String SIGNATURE = "\n\n - from F360 Protect Family :)";
	
	private static void load(){
		try {
			BufferedReader reader = new BufferedReader(new FileReader("J:/birthday_quotes.txt"));
			String line = reader.readLine();
			while(line != null){
				birthdayQuotes.add(line);
				line = reader.readLine();
			}
			loaded = true;
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		loaded = false;
	}
	
	public static String getBirthdayQuotes(){
		if(!loaded) load();
		
		int randomQuoteIndex = random.nextInt(birthdayQuotes.size());
		return birthdayQuotes.get(randomQuoteIndex) + SIGNATURE;
	}
	
	public static String getBirthdayTemplate(){
		File files[] = new File("J:/birthday templates").listFiles();
		return "J:/birthday templates/bday_template_" + random.nextInt(files.length) + ".png";
	}

}
