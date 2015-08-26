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
import org.joda.time.DateTime;

import com.guevent.gibx.jim.f360.F360Member;

public class RenewalChecker {
	public final static int WAITING_PERIOD = 7;
	public static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	
	public static DateFormat dateFileFormat = new SimpleDateFormat("yyyy-MM-dd");
	public static String TODAY_DATE = new DateTime(new Date()).plusDays(WAITING_PERIOD).toString("yyyy-MM-dd").substring(5);
	public static String TODAY_YEAR = dateFileFormat.format(new Date()).substring(0, 4);
	
	
	private List<F360Member> members = new ArrayList<F360Member>();
	
	private String excelFile;
	public RenewalChecker(String excelFile){
		this.excelFile = excelFile;
	}
	
	static String file = "J:/F360 Daily Reports/2015/May/1st week/a/Insurance Report for 2013-05-27.xlsx";
	public static void main(String args[]){
		int len = file.split("/").length;
		
		String dateToCheck = file.split("/")[len - 1].replace("Insurance Report for ", "").replace(".xlsx", "");
		
		
		DateTime dTime = new DateTime(new Date());
		System.out.print(TODAY_DATE);
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
			
			Iterator<Row> rowIterator = sheet.iterator();
			while(rowIterator.hasNext()){
				Row row = rowIterator.next();
				Cell cell0 = row.getCell(0);
				Cell cell1 = row.getCell(1);
				Cell cell3 = row.getCell(3);
				Cell cell4 = row.getCell(4);
				Cell cell5 = row.getCell(5);
				Cell cell7 = row.getCell(7);
				if(cell0 != null) cell0.setCellType(1);
				if(cell1 != null) cell1.setCellType(1);
				if(cell3 != null) cell3.setCellType(1);
				if(cell4 != null) cell4.setCellType(1);
				if(cell5 != null) cell5.setCellType(1);
				if(cell7 != null) cell7.setCellType(1);
				
				String policy = cell0 == null ? "NO POLICY" : cell0.getStringCellValue();
				String coc = cell1 == null ? "NO COC" : cell1.getStringCellValue();
				String firstName = cell3 == null ? "NO NAME" : cell3.getStringCellValue();
				String middleName = cell4 == null ? "NO NAME" : cell4.getStringCellValue();
				String lastName = cell5 == null ? "NO NAME" : cell5.getStringCellValue();
				String relationship = cell7 == null ? "NO REL" : cell7.getStringCellValue();
				
				F360Member member = new F360Member();
				member.setPolicy(policy);
				member.setCoc(coc);
				member.setFirstName(firstName);
				member.setMiddleName(middleName);
				member.setLastName(lastName);
				member.setRelationship(relationship);
				
				members.add(member);
			}
			fis.close();
			return members;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
