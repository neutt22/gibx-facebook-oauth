import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import org.scribe.model.Verifier;

import com.guevent.gibx.jim.fboauth.view.FBPublisher;


public class Controller implements ActionListener{
	
	private StyledDocument txtBuffer;
	private JTextField txtUserToken;
	private GIBXAccount gibxAccount;
	private JTextPane bufferPane;
	public void setBufferPane(JTextPane bufferPane) { this.bufferPane = bufferPane; }
	
	
	public static final String DASH = "------------------------------------------------------\n\n";
	
	private Style defaultStyle = StyleContext.getDefaultStyleContext ().getStyle (StyleContext.DEFAULT_STYLE);
	public static Style STYLE_SUCCESS, STYLE_ITALIC, STYLE_FAILED;
	
	public Controller(StyledDocument txtBuffer, JTextField txtUserToken, GIBXAccount gibxAccount){
		this.txtBuffer = txtBuffer; this.gibxAccount = gibxAccount; this.txtUserToken = txtUserToken;
		
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
	
	
	
	@Override
	public void actionPerformed(ActionEvent ae){
		if(ae.getActionCommand().equals("get_user_token")){
			new UIWorker().executeUserTokenWorker();
		}
		if(ae.getActionCommand().equals("publish_text")){
			FBPublisher fb = new FBPublisher();
			if(fb.getFeed() != null) new UIWorker().executePublishTextWorker(fb.getFeed());
		}
		if(ae.getActionCommand().equals("publish_photo")){
			FBPublisher fb = new FBPublisher();
			if(fb.getFeed() != null) new UIWorker().executePublishPhotoWorker(fb.getFeed(), fb.getImage());
		}
		if(ae.getActionCommand().equals("update_token")){
			Utils.updateToken(txtUserToken.getText(), txtBuffer);
			bufferPane.setCaretPosition(bufferPane.getDocument().getLength());
		}
		if(ae.getActionCommand().equals("clear_buff")){
			bufferPane.setText("");
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
		
		private SwingWorker<Boolean, BufferStyle> publishWorker = new SwingWorker<Boolean, BufferStyle>(){
			
			protected Boolean doInBackground() throws Exception {
				publish(new BufferStyle("Publishing a post... ", null));
				publish(new BufferStyle("(If it's taking too long, update your user token)\n\n", STYLE_ITALIC));
				FacebookPublisher pub = new FacebookPublisher(txtUserToken.getText());
				String id;
				if(image.length() == 0){
					id = pub.postFeed(0, feed);
				}else{
					id = pub.postPhotoFeed(0, feed, image);
				}
				String link = "www.facebook.com/" + pub.getAccountName() + "/posts/" + id;
				publish(new BufferStyle("Successfully published a post! " , STYLE_SUCCESS));
				publish(new BufferStyle("Check it out on:\n", null));
				publish(new BufferStyle(link + "\n", null));
				publish(new BufferStyle(DASH, null));
				bufferPane.setCaretPosition(bufferPane.getDocument().getLength());
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
				publish(new BufferStyle("Initializing OAuthService builder...\n", null));
				token.buildOAuthService();Thread.sleep(500);
				publish(new BufferStyle("Building done. Retrieving request token at:\n", null));Thread.sleep(500);
				publish(new BufferStyle(token.getAuthUrl() + "\n\n", null));
				gibxAccount.fillUp();
				publish(new BufferStyle("Please check your credentials carefully below:\n", null));Thread.sleep(500);
				publish(new BufferStyle("Username: ", null));
				publish(new BufferStyle(gibxAccount.getUsername() + "\n", STYLE_SUCCESS));Thread.sleep(200);
				publish(new BufferStyle("Password: ", null));
				publish(new BufferStyle(Utils.getPasswordInStar(gibxAccount.getPassword()) + "\n\n", STYLE_SUCCESS));Thread.sleep(500);
				publish(new BufferStyle("Please wait...\n", null));Thread.sleep(500);
				String code = token.readURL(token.getAuthUrl(), gibxAccount.getUsername(), gibxAccount.getPassword(), txtBuffer);Thread.sleep(500);
				if(code == null){
					publish(new BufferStyle("Failed retrieving request token. Please try again.\n", STYLE_FAILED));
					publish(new BufferStyle(DASH, null));
					return false;
				}
				publish(new BufferStyle("Request Token: " + code + "\n", null));
				Verifier v = new Verifier(code);
				publish(new BufferStyle("Trading code for access token... ", null));
				publish(new BufferStyle("(If it's taking too long, check your credentials/permissions and try again)\n", STYLE_ITALIC));Thread.sleep(500);
				publish(new BufferStyle("...\n", null));
				String accessToken = token.getAccessToken(v).getToken();
				publish(new BufferStyle("ACCESS TOKEN ACQUIRED:\n", STYLE_SUCCESS));
				publish(new BufferStyle(accessToken + "\n", null));
				txtUserToken.setText(accessToken + "\n");
				publish(new BufferStyle(DASH, null));
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
