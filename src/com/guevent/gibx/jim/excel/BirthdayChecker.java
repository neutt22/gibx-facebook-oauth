package com.guevent.gibx.jim.excel;

import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
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
	
	private String excelFile;
	public BirthdayChecker(String excelFile){
		this.excelFile = excelFile;
	}
	
	/**
	 * Check the specified Excel file on the object
	 * to match any member with same day as today.
	 * Populates into a List and return.
	 * {@link #isDateMatched(String, String)}
	 * @return A List of members with member today
	 */
	public List<F360Member> readExcel(){
		try{
			FileInputStream fis = new FileInputStream(new File(excelFile));
			XSSFWorkbook workBook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workBook.getSheetAt(0);
			
			String fullDate = dateFormat.format(new Date());
			String date = fullDate.substring(0, fullDate.length() - 5);
//			date = "12/10";
			
			Iterator<Row> rowIterator = sheet.iterator();
			while(rowIterator.hasNext()){
				Row row = rowIterator.next();
				Cell cell3 = row.getCell(3);
				Cell cell4 = row.getCell(4);
				Cell cell5 = row.getCell(5);
				Cell cell7 = row.getCell(7);
				if(cell3 != null) cell3.setCellType(1);
				if(cell4 != null) cell4.setCellType(1);
				if(cell5 != null) cell5.setCellType(1);
				if(cell7 != null) cell7.setCellType(1);
				
				String firstName = cell3 == null ? "NO NAME" : cell3.getStringCellValue();
				String middleName = cell4 == null ? "NO NAME" : cell4.getStringCellValue();
				String lastName = cell5 == null ? "NO NAME" : cell5.getStringCellValue();
				String relationship = cell7 == null ? "NO REL" : cell7.getStringCellValue();
				
				Cell bdayCell = row.getCell(12);
				if(bdayCell != null) bdayCell.setCellType(1);
				String birthday = bdayCell == null ? "BAD_DATE" : bdayCell.getStringCellValue();
				
				F360Member member = new F360Member();
				member.setFirstName(firstName);
				member.setMiddleName(middleName);
				member.setLastName(lastName);
				member.setBirthdate(birthday);
				member.setRelationship(relationship);
				
				if(isDateMatched(date, member.getBirthdate())){
//					System.out.println("Matching : " + date + " vs " + member.getBirthdate());
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
