package com.guevent.gibx.jim.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.guevent.gibx.jim.f360.F360DbConnection;
import com.guevent.gibx.jim.f360.F360Member;
import com.guevent.gibx.jim.f360.MemberAdder;
import com.guevent.gibx.jim.utils.BufferStyle;

public class DBController implements ActionListener {
	
	private StyledDocument txtBuffer;
	private JTextPane bufferPane;
	
	public DBController(StyledDocument txtBuffer, JTextPane bufferPane){
		this.txtBuffer = txtBuffer;
		this.bufferPane = bufferPane;
	}
	
	public void actionPerformed(ActionEvent ae){
		if(ae.getActionCommand().equals("db_search")){
			System.out.println("searching");
		}
		if(ae.getActionCommand().equals("db_load")){
			new UIWorker().executeDbMemberAdder();
		}
		if(ae.getActionCommand().equals("db_recursive")){
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File("J:/"));
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int res = chooser.showOpenDialog(null);
			if(res == JFileChooser.APPROVE_OPTION){
				getFiles(chooser.getSelectedFile());
			}
			//new UIWorker().executeDbRecursiveMemberAdder();
		}
	}
	
	public void getFiles(File f){
        File files[];
        String file_;
        if(f.isFile()){
        	file_ = f.getAbsolutePath();
        	if(file_.contains("Insurance Report for")){
        		new UIWorker().executeDbRecursiveMemberAdder(f);
        		System.out.println(f.getAbsolutePath());
        	}
        }else{
            files = f.listFiles();
            for (int i = 0; i < files.length; i++) {
                getFiles(files[i]);
            }
        }
    }

	private class UIWorker{
		
		public void executeDbMemberAdder(){
			adderWorker.execute();
		}
		
		private File file;
		public void executeDbRecursiveMemberAdder(File file){
			this.file = file;
			adderRecursiveWorker.execute();
		}
		
		private SwingWorker<Boolean, BufferStyle> adderWorker = new SwingWorker<Boolean, BufferStyle>(){
			
			protected Boolean doInBackground() throws Exception{
				JFileChooser chooser = new JFileChooser();
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.addChoosableFileFilter(new FileNameExtensionFilter("Excel Files", "xlsx"));
				int res = chooser.showOpenDialog(null);
				if(res == JFileChooser.APPROVE_OPTION){
					try{
						FileInputStream fis = new FileInputStream(chooser.getSelectedFile());
						XSSFWorkbook workBook = new XSSFWorkbook(fis);
						XSSFSheet sheet = workBook.getSheetAt(0);
						Iterator<Row> rows = sheet.iterator();
						rows.next();
						MemberAdder memberAdder = new MemberAdder();
						new F360DbConnection().clearTableData("members");
						int count = 0;
						while(rows.hasNext()){
							Row row = rows.next();
							F360Member member = new F360Member();
							Cell _0 = row.getCell(0);
							Cell _1 = row.getCell(1);
							Cell _2 = row.getCell(2);
							Cell _3 = row.getCell(3);
							Cell _4 = row.getCell(4);
							Cell _5 = row.getCell(5);
							Cell _6 = row.getCell(6);
							Cell _7 = row.getCell(7);
							Cell _8 = row.getCell(8);
							Cell _9 = row.getCell(9);
							Cell _10 = row.getCell(10);
							Cell _11 = row.getCell(11);
							Cell _12 = row.getCell(12);
							Cell _13 = row.getCell(13);
							Cell _14 = row.getCell(14);
							Cell _15 = row.getCell(15);
							Cell _16 = row.getCell(16);
							Cell _17 = row.getCell(17);
							Cell _18 = row.getCell(18);
							Cell _19 = row.getCell(19);
							_0.setCellType(1);
							_1.setCellType(1);
							_2.setCellType(1);
							_3.setCellType(1);
							_4.setCellType(1);
							_5.setCellType(1);
							_6.setCellType(1);
							_7.setCellType(1);
							_8.setCellType(1);
							_9.setCellType(1);
							_10.setCellType(1);
							_11.setCellType(1);
							_12.setCellType(1);
							_13.setCellType(1);
							_14.setCellType(1);
							_15.setCellType(1);
							_16.setCellType(1);
							_17.setCellType(1);
							_18.setCellType(1);
							_19.setCellType(1);
							String policy = _0.getStringCellValue();
							String coc = _1.getStringCellValue();
							String pin_code = _2.getStringCellValue();
							String first_name = _3.getStringCellValue();
							String middle_name = _4.getStringCellValue();
							String last_name = _5.getStringCellValue();
							String occupation = _6.getStringCellValue();
							String relationship = _7.getStringCellValue();
							String address = _8.getStringCellValue();
							address += _9.getStringCellValue();
							address += _10.getStringCellValue();
							String phone = _11.getStringCellValue();
							String bdate = _12.getStringCellValue();
							String age = _13.getStringCellValue();
							String activation_plan = _14.getStringCellValue();
							String plan = _15.getStringCellValue();
							String beneficiary_name = _16.getStringCellValue();
							String beneficiary_relation = _17.getStringCellValue();
							String cover = _18.getStringCellValue();
							String type = _19.getStringCellValue();
							
							member.setPolicy(policy);
							member.setCoc(coc);
							member.setPinCode(pin_code);
							member.setFirstName(first_name);
							member.setMiddleName(middle_name);
							member.setLastName(last_name);
							member.setOccupation(occupation);
							member.setRelationship(relationship);
							member.setAddress(address);
							member.setPhone(phone);
							member.setBirthdate(bdate);
							member.setAge(age);
							member.setActivationPlan(activation_plan);
							member.setPlan(plan);
							member.setBeneficiaryName(beneficiary_name);
							member.setBeneficiaryRelationship(beneficiary_relation);
							member.setCover(cover);
							member.setType(type);
							
							memberAdder.add(member);
							publish(new BufferStyle("Member with COC: ", null));
							publish(new BufferStyle(member.getCoc(), Controller.STYLE_SUCCESS));
							publish(new BufferStyle(" has been added.\n", null));
							count += 1;
							Thread.sleep(40);
						}
						memberAdder.closeConnection();
						publish(new BufferStyle("Total members added: ", Controller.STYLE_SUCCESS));
						publish(new BufferStyle(String.valueOf(count) + "\n", Controller.STYLE_SUCCESS));
						publish(new BufferStyle(Controller.DASH, null));
						fis.close();
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				return null;
			}
			
			protected void process(List<BufferStyle> msgs){
				try {
					for(BufferStyle msg : msgs){
						txtBuffer.insertString(txtBuffer.getLength(), msg.getMessage(), msg.getStyle());
					}
					bufferPane.setCaretPosition(bufferPane.getDocument().getLength());
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		};//worker
		
		private SwingWorker<Boolean, BufferStyle> adderRecursiveWorker = new SwingWorker<Boolean, BufferStyle>(){
			
			protected Boolean doInBackground() throws Exception{
					try{
						FileInputStream fis = new FileInputStream(file);
						XSSFWorkbook workBook = new XSSFWorkbook(fis);
						XSSFSheet sheet = workBook.getSheetAt(0);
						Iterator<Row> rows = sheet.iterator();
						rows.next();
						MemberAdder memberAdder = new MemberAdder();
						new F360DbConnection().clearTableData("members");
						int count = 0;
						while(rows.hasNext()){
							Row row = rows.next();
							F360Member member = new F360Member();
							Cell _0 = row.getCell(0);
							Cell _1 = row.getCell(1);
							Cell _2 = row.getCell(2);
							Cell _3 = row.getCell(3);
							Cell _4 = row.getCell(4);
							Cell _5 = row.getCell(5);
							Cell _6 = row.getCell(6);
							Cell _7 = row.getCell(7);
							Cell _8 = row.getCell(8);
							Cell _9 = row.getCell(9);
							Cell _10 = row.getCell(10);
							Cell _11 = row.getCell(11);
							Cell _12 = row.getCell(12);
							Cell _13 = row.getCell(13);
							Cell _14 = row.getCell(14);
							Cell _15 = row.getCell(15);
							Cell _16 = row.getCell(16);
							Cell _17 = row.getCell(17);
							Cell _18 = row.getCell(18);
							Cell _19 = row.getCell(19);
							_0.setCellType(1);
							_1.setCellType(1);
							_2.setCellType(1);
							_3.setCellType(1);
							_4.setCellType(1);
							_5.setCellType(1);
							_6.setCellType(1);
							_7.setCellType(1);
							_8.setCellType(1);
							_9.setCellType(1);
							_10.setCellType(1);
							_11.setCellType(1);
							_12.setCellType(1);
							_13.setCellType(1);
							_14.setCellType(1);
							_15.setCellType(1);
							_16.setCellType(1);
							_17.setCellType(1);
							_18.setCellType(1);
							_19.setCellType(1);
							String policy = _0.getStringCellValue();
							String coc = _1.getStringCellValue();
							String pin_code = _2.getStringCellValue();
							String first_name = _3.getStringCellValue();
							String middle_name = _4.getStringCellValue();
							String last_name = _5.getStringCellValue();
							String occupation = _6.getStringCellValue();
							String relationship = _7.getStringCellValue();
							String address = _8.getStringCellValue();
							address += _9.getStringCellValue();
							address += _10.getStringCellValue();
							String phone = _11.getStringCellValue();
							String bdate = _12.getStringCellValue();
							String age = _13.getStringCellValue();
							String activation_plan = _14.getStringCellValue();
							String plan = _15.getStringCellValue();
							String beneficiary_name = _16.getStringCellValue();
							String beneficiary_relation = _17.getStringCellValue();
							String cover = _18.getStringCellValue();
							String type = _19.getStringCellValue();
							
							member.setPolicy(policy);
							member.setCoc(coc);
							member.setPinCode(pin_code);
							member.setFirstName(first_name);
							member.setMiddleName(middle_name);
							member.setLastName(last_name);
							member.setOccupation(occupation);
							member.setRelationship(relationship);
							member.setAddress(address);
							member.setPhone(phone);
							member.setBirthdate(bdate);
							member.setAge(age);
							member.setActivationPlan(activation_plan);
							member.setPlan(plan);
							member.setBeneficiaryName(beneficiary_name);
							member.setBeneficiaryRelationship(beneficiary_relation);
							member.setCover(cover);
							member.setType(type);
							
							memberAdder.add(member);
							publish(new BufferStyle("Member with COC: ", null));
							publish(new BufferStyle(member.getCoc(), Controller.STYLE_SUCCESS));
							publish(new BufferStyle(" has been added.\n", null));
							count += 1;
							Thread.sleep(40);
						}
						memberAdder.closeConnection();
						publish(new BufferStyle("Total members added: ", Controller.STYLE_SUCCESS));
						publish(new BufferStyle(String.valueOf(count) + "\n", Controller.STYLE_SUCCESS));
						publish(new BufferStyle(Controller.DASH, null));
						fis.close();
					}catch(Exception e){
						e.printStackTrace();
					}
				return null;
			}
			
			protected void process(List<BufferStyle> msgs){
				try {
					for(BufferStyle msg : msgs){
						txtBuffer.insertString(txtBuffer.getLength(), msg.getMessage(), msg.getStyle());
					}
					bufferPane.setCaretPosition(bufferPane.getDocument().getLength());
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		};//worker
	
	}

}
