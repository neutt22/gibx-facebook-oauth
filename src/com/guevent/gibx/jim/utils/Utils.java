package com.guevent.gibx.jim.utils;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.ini4j.Wini;

import com.guevent.gibx.jim.MainView;


public class Utils {
	
	private static String ext = "";
	
	public static String getTableData (final TableModel tableModel, final JProgressBar bar) {
		
		final JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("J:/"));
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.addChoosableFileFilter(new FileNameExtensionFilter("Excel Files", "xlsx"));
		int res = chooser.showSaveDialog(null);
		
		if(res != JFileChooser.APPROVE_OPTION) return null;
		
		SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>(){
			
			protected Void doInBackground(){
				//**
				try {
					if(!chooser.getSelectedFile().getAbsolutePath().contains(".xlsx")) ext = ".xlsx";
					FileOutputStream fos = new FileOutputStream(chooser.getSelectedFile() + ext);
					XSSFWorkbook workbook = new XSSFWorkbook();
					XSSFSheet sheet = workbook.createSheet("F360 Export");
					
				    TableModel dtm = tableModel;
				    int nRow = dtm.getRowCount(), nCol = dtm.getColumnCount();
				    
				    XSSFRow colHeader = sheet.createRow(0);
				    XSSFCell c = colHeader.createCell(0);
				    c.setCellValue("ID");
				    for(int i = 0; i < getSearchCat().length; i++){
				    	XSSFCell cell = colHeader.createCell(i + 1);
				    	cell.setCellValue(getSearchCat()[i]);
				    }
				    
				    bar.setMaximum(nRow);
				    int prg = 0;
				    for (int i = 0 ; i < nRow ; i++){
				    	XSSFRow row = sheet.createRow(i + 1);
				    	prg += 1;
				    	publish(prg - 1);
				    	Thread.sleep(10);
				        for (int j = 0 ; j < nCol ; j++){
				        	XSSFCell cell = row.createCell(j);
				        	cell.setCellValue(dtm.getValueAt(i, j).toString());
				        }
				    }
				    
				    workbook.write(fos);
				    fos.flush();
					fos.close();
					Desktop.getDesktop().open(new File(chooser.getSelectedFile().getAbsolutePath() + ext));
					publish(100);
					Thread.sleep(5000);
					bar.setValue(0);
				    return null;
				} catch (Exception e) {
					e.printStackTrace();
				}
				//**
				return null;
			}
			
			protected void process(List<Integer> val){
				for(Integer i : val){
					bar.setValue(i);
				}
			}
		};
		worker.execute();
		
		return chooser.getSelectedFile().getAbsolutePath() + ext;
	}
	
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
