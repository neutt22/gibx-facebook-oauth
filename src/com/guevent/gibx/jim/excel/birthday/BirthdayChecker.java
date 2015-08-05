package com.guevent.gibx.jim.excel.birthday;

import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.guevent.gibx.jim.f360.F360Member;

public class BirthdayChecker {
	
	public static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	
	private List<F360Member> members = new ArrayList<F360Member>();
	
	private boolean isDateMatched(String today, String birthdate){
		return today.equals(birthdate);
	}
	
	
	
	public List<F360Member> readExcel(){
		try{
			String excelFile = "C:/Insurance Report for 2015-06-05.xlsx";
			FileInputStream fis = new FileInputStream(new File(excelFile));
			XSSFWorkbook workBook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workBook.getSheetAt(0);
			
			String fullDate = dateFormat.format(new Date());
			String date = fullDate.substring(0, fullDate.length() - 5);
			//date = "08/03";
			
			Iterator<Row> rowIterator = sheet.iterator();
			while(rowIterator.hasNext()){
				Row row = rowIterator.next();
				String firstName = row.getCell(3).getStringCellValue();
				String middleName = row.getCell(4).getStringCellValue();
				String lastName = row.getCell(5).getStringCellValue();
				String birthday = row.getCell(12).getStringCellValue();
				F360Member member = new F360Member();
				member.setFirstName(firstName);
				member.setMiddleName(middleName);
				member.setLastName(lastName);
				member.setBirthdate(birthday);
				
				//System.out.println("Fullname: " + member.getFullName() + " Birthdate: " + member.getBirthdate());
				if(isDateMatched(date, member.getBirthdate())){
					members.add(member);
				}
			}
			fis.close();
			return members;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
