package com.guevent.gibx.jim.controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import org.scribe.model.Verifier;

import com.guevent.gibx.jim.FacebookToken;
import com.guevent.gibx.jim.GIBXAccount;
import com.guevent.gibx.jim.MainView;
import com.guevent.gibx.jim.excel.BirthdayChecker;
import com.guevent.gibx.jim.excel.RenewalChecker;
import com.guevent.gibx.jim.f360.F360Member;
import com.guevent.gibx.jim.fboauth.view.FBViewPublisher;
import com.guevent.gibx.jim.publisher.facebook.BirthdayPane;
import com.guevent.gibx.jim.publisher.facebook.FacebookAccountPublisher;
import com.guevent.gibx.jim.publisher.facebook.FacebookGroupPublisher;
import com.guevent.gibx.jim.publisher.facebook.RenewalPane;
import com.guevent.gibx.jim.utils.BirthdayUtils;
import com.guevent.gibx.jim.utils.BufferStyle;
import com.guevent.gibx.jim.utils.Utils;


public class Controller implements ActionListener{
	
	private StyledDocument txtBuffer;
	private JTextField txtUserToken;
	private GIBXAccount gibxAccount;
	private JTextPane bufferPane;
	private JProgressBar barProgress;
	
	
	private BirthdayPane bdayPane;
	private RenewalPane renewalPane;
	
	public void setBdayPane(BirthdayPane bPane){
		bdayPane = bPane;
	}
	
	public void setRenewalPane(RenewalPane rPane){
		renewalPane = rPane;
	}
	
	public void setBufferPane(JTextPane bufferPane) { this.bufferPane = bufferPane; }
	
	
	public static final String DASH = "------------------------------------------------------\n\n";
	
	private Style defaultStyle = StyleContext.getDefaultStyleContext ().getStyle (StyleContext.DEFAULT_STYLE);
	public static Style STYLE_SUCCESS, STYLE_ITALIC, STYLE_FAILED;
	
	public Controller(StyledDocument txtBuffer, JTextField txtUserToken, GIBXAccount gibxAccount, JProgressBar prg){
		this.txtBuffer = txtBuffer; this.gibxAccount = gibxAccount; this.txtUserToken = txtUserToken; barProgress = prg;
		
		STYLE_SUCCESS = txtBuffer.addStyle("STYLE_SUCCESS", defaultStyle);
		StyleConstants.setForeground(STYLE_SUCCESS, Color.decode("#00CC33"));
		StyleConstants.setBold(STYLE_SUCCESS, true);
		
		STYLE_ITALIC = txtBuffer.addStyle("STYLE_ITALIC", defaultStyle);
		StyleConstants.setForeground(STYLE_ITALIC, Color.decode("#FF6600"));
		StyleConstants.setItalic(STYLE_ITALIC, true);
		
		
		STYLE_FAILED = txtBuffer.addStyle("STYLE_ITALIC", defaultStyle);
		StyleConstants.setForeground(STYLE_FAILED, Color.RED);
		StyleConstants.setBold(STYLE_FAILED, true);
	}
	
	//recursive method
	private void getFiles(final File f){
        File files[];
        String file_;
        if(f.isFile()){
        	file_ = f.getAbsolutePath();
        	if(file_.contains("Insurance Report for")){
				List<F360Member> members = new BirthdayChecker(f.getAbsolutePath()).readExcel();
				for(F360Member member : members){
					if(member.getRelationship().equalsIgnoreCase("principal")){
						bdayPane.setCelebrant(member.getFullName());
					}
				}
        	}
        }else{
            files = f.listFiles();
            for (int i = 0; i < files.length; i++) {
                getFiles(files[i]);
            }
        }
    }
	
	//recursive method
	private void getRenewalFiles(final File f){
        File files[];
        String file_;
        if(f.isFile()){
        	file_ = f.getAbsolutePath();
        	if(file_.contains("Insurance Report for")){
        		int len = file_.split("\\\\").length;
        		String toCheck = file_.split("\\\\")[len - 1].replace("Insurance Report for ", "").replace(".xlsx", "");
//        		toCheck = new DateTime(toCheck).plusDays(RenewalChecker.WAITING_PERIOD).toString("yyyy-MM-dd");
        		
        		String toCheckYear = toCheck.substring(0, 4); //get the year of the excel
        		toCheck = toCheck.substring(5);               //get the date of the excel
        		
    			int year_today = Integer.parseInt(RenewalChecker.TODAY_YEAR) - 1;
    			int year_toCheck = Integer.parseInt(toCheckYear);
    			if(year_today != year_toCheck) return;
    			
    			if(toCheck.equalsIgnoreCase(RenewalChecker.TODAY_DATE)){ //the excel file is matched today
    				System.out.println(file_);
					List<F360Member> members = new RenewalChecker(f.getAbsolutePath()).readExcel();
					for(F360Member member : members){
						if(member.getRelationship().equalsIgnoreCase("principal")){
							renewalPane.addRenewal(member.getFullName() + " - " + member.getPolicy() + member.getCoc());
						}
					}
    			}
        	}
        }else{
            files = f.listFiles();
            for (int i = 0; i < files.length; i++) {
            	getRenewalFiles(files[i]);
            }
        }
    }
	
	@Override
	public void actionPerformed(ActionEvent ae){
		if(ae.getActionCommand().equals("check_birthdates")){
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File("J:/"));
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int res = chooser.showOpenDialog(null);
			if(res == JFileChooser.APPROVE_OPTION){
				getFiles(chooser.getSelectedFile());
				String fileName = MainView.APPDATA + BirthdayPane.getDate() + "_birthdays.png";
				System.out.println(fileName);
				bdayPane.repaintPane();
				bdayPane.saveToFile(fileName);
				new UIWorker().executePublishPhotoWorker(BirthdayUtils.getBirthdayQuotes(), fileName);
			}
		}
		if(ae.getActionCommand().equals("check_renewal")){
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File("J:/"));
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int res = chooser.showOpenDialog(null);
			if(res == JFileChooser.APPROVE_OPTION){
				getRenewalFiles(chooser.getSelectedFile());
				String fileName = MainView.APPDATA + RenewalPane.getDate() + "_renewals.png";
				System.out.println(fileName);
				renewalPane.repaintPane();
				renewalPane.saveToFile(fileName);
				System.out.println("Uploading.. charot");
				new UIWorker().executePublishPhotoWorker("Should you have any questions or comments, please feel free to contact us at (02) 470 9283.", fileName);
			}
		}
		if(ae.getActionCommand().equals("get_user_token")){
			new UIWorker().executeUserTokenWorker();
		}
		if(ae.getActionCommand().equals("publish_text")){
			FBViewPublisher fb = new FBViewPublisher();
			if(fb.getFeed() != null) new UIWorker().executePublishTextWorker(fb.getFeed());
		}
		if(ae.getActionCommand().equals("publish_photo")){
			FBViewPublisher fb = new FBViewPublisher();
			if(fb.getFeed() != null) new UIWorker().executePublishPhotoWorker(fb.getFeed(), fb.getImage());
		}
		if(ae.getActionCommand().equals("update_token")){
			new UIWorker().executeUpdateTokenWorker();
		}
		if(ae.getActionCommand().equals("clear_buff")){
			new UIWorker().executeClearWorker();
		}
	}
	
	private class UIWorker{
		
		private String feed, image;
		
		public void executeUserTokenWorker(){
			userTokenWorker.execute();
		}
		
		public void executePublishTextWorker(String feed){
			this.feed = feed;
			image = "";
			publishWorker.execute();
		}
		
		public void executePublishPhotoWorker(String feed, String image){
			this.feed = feed;
			this.image = image;
			publishWorker.execute();
		}
		
		public void executeClearWorker(){
			clearWorker.execute();
		}
		
		public void executeUpdateTokenWorker(){
			updateTokenWorker.execute();
		}
		
		private SwingWorker<Void, Integer> clearWorker = new SwingWorker<Void, Integer>(){
			protected Void doInBackground() throws Exception{
				for(int x = 0; x <= 100; x++){
					publish(x);
					Thread.sleep(1);
				}
				publish(0);
				bufferPane.setText("");
				return null;
			}
			
			protected void process(List<Integer> val){
				for(Integer i : val){
					barProgress.setValue(i);
				}
				
			}
		};
		
		private SwingWorker<Void, Integer> updateTokenWorker = new SwingWorker<Void, Integer>(){
			protected Void doInBackground() throws Exception{
				for(int x = 0; x <= 100; x++){
					publish(x);
					Thread.sleep(1);
				}
				publish(0);
				Utils.updateToken(txtUserToken.getText(), txtBuffer);
				bufferPane.setCaretPosition(bufferPane.getDocument().getLength());
				return null;
			}
			
			protected void process(List<Integer> val){
				for(Integer i : val){
					barProgress.setValue(i);
				}
				
			}
		};

		
		private SwingWorker<Boolean, BufferStyle> publishWorker = new SwingWorker<Boolean, BufferStyle>(){
			
			protected Boolean doInBackground() throws Exception {
				publish(new BufferStyle("Initiating... ", null));
				barProgress.setValue(30);
				publish(new BufferStyle("(If it's taking too long, update your user token)\n", STYLE_ITALIC));
				FacebookAccountPublisher pub = new FacebookAccountPublisher(txtUserToken.getText());
				FacebookGroupPublisher gPub = new FacebookGroupPublisher(txtUserToken.getText());
				publish(new BufferStyle("Publishing a post... \n\n", null));
				String id;
				String gId;
				if(image.length() == 0){
					id = pub.postFeed(0, feed);
					gId = gPub.postGroupFeed(feed);
				}else{
					id = pub.postPhotoFeed(0, feed, image);
					gId = gPub.postGroupFeedWithPhoto(feed, image);
				}
				barProgress.setValue(100);
				String link = "www.facebook.com/" + pub.getAccountName() + "/posts/" + id;
				String gLink = "www.facebook.com/" + gPub.getGroupId() + "/posts/" + gId;
				publish(new BufferStyle("Successfully published a post! " , STYLE_SUCCESS));
				publish(new BufferStyle("Check it out on:\n", null));
				publish(new BufferStyle(link + "\n", null));
				publish(new BufferStyle(gLink + "\n", null));
				publish(new BufferStyle(DASH, null));
				bufferPane.setCaretPosition(bufferPane.getDocument().getLength());
				Thread.sleep(300);
				barProgress.setValue(0);
				return true;
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
		};
		
		private SwingWorker<Boolean, BufferStyle> userTokenWorker = new SwingWorker<Boolean, BufferStyle>(){
			
			protected Boolean doInBackground() throws Exception{
				FacebookToken token = new FacebookToken();Thread.sleep(500);
				barProgress.setValue(10);
				publish(new BufferStyle("Initializing OAuthService builder...\n", null));
				token.buildOAuthService();Thread.sleep(500);
				barProgress.setValue(20);
				publish(new BufferStyle("Building done. Retrieving request token at:\n", null));Thread.sleep(500);
				publish(new BufferStyle(token.getAuthUrl() + "\n\n", null));
				gibxAccount.fillUp();
				publish(new BufferStyle("Please check your credentials carefully below:\n", null));Thread.sleep(500);
				barProgress.setValue(30);
				publish(new BufferStyle("Username: ", null));
				publish(new BufferStyle(gibxAccount.getUsername() + "\n", STYLE_SUCCESS));Thread.sleep(200);
				barProgress.setValue(40);
				publish(new BufferStyle("Password: ", null));
				publish(new BufferStyle(Utils.getPasswordInStar(gibxAccount.getPassword()) + "\n\n", STYLE_SUCCESS));Thread.sleep(500);
				publish(new BufferStyle("Please wait...\n", null));Thread.sleep(500);
				barProgress.setValue(60);
				String code = token.readURL(token.getAuthUrl(), gibxAccount.getUsername(), gibxAccount.getPassword(), txtBuffer);Thread.sleep(500);
				if(code == null){
					publish(new BufferStyle("Failed retrieving request token. Please try again.\n", STYLE_FAILED));
					publish(new BufferStyle(DASH, null));
					barProgress.setValue(0);
					return false;
				}
				publish(new BufferStyle("Request Token: " + code + "\n", null));
				Verifier v = new Verifier(code);
				publish(new BufferStyle("Trading code for access token... ", null));
				publish(new BufferStyle("(If it's taking too long, check your credentials/permissions and try again)\n", STYLE_ITALIC));Thread.sleep(500);
				publish(new BufferStyle("...\n", null));
				barProgress.setValue(70);
				String accessToken = token.getAccessToken(v).getToken();
				publish(new BufferStyle("ACCESS TOKEN ACQUIRED:\n", STYLE_SUCCESS));
				publish(new BufferStyle(accessToken + "\n", null));
				txtUserToken.setText(accessToken + "\n");
				publish(new BufferStyle(DASH, null));
				barProgress.setValue(100);
				Thread.sleep(300);
				barProgress.setValue(0);
				return true;
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
			
		};
	}

}
