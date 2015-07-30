import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.scribe.model.Verifier;


public class Controller implements ActionListener{
	
	private StyledDocument txtBuffer;
	private JTextField txtUserToken;
	private GIBXAccount gibxAccount;
	
	public static final String DASH = "------------------------------------------------------\n\n";

	public Controller(StyledDocument txtBuffer, JTextField txtUserToken, GIBXAccount gibxAccount){
		this.txtBuffer = txtBuffer; this.gibxAccount = gibxAccount; this.txtUserToken = txtUserToken;
	}
	
	@Override
	public void actionPerformed(ActionEvent ae){
		if(ae.getActionCommand().equals("get_user_token")){
			try {
				FacebookToken token = new FacebookToken();
				txtBuffer.insertString(txtBuffer.getLength(), "Retrieving request token, initializing OAuthService builder...\n", null);
				token.buildOAuthService();
				txtBuffer.insertString(txtBuffer.getLength(), "Building done. Retrieving request token at:\n", null);
				txtBuffer.insertString(txtBuffer.getLength(), token.getAuthUrl() + "\n\n", null);
				gibxAccount.fillUp();
				String code = token.readURL(token.getAuthUrl(), gibxAccount.getUsername(), gibxAccount.getPassword());
				txtBuffer.insertString(txtBuffer.getLength(), "Request Token: " + code + "\n", null);
				Verifier v = new Verifier(code);
				txtBuffer.insertString(txtBuffer.getLength(), "Trading code for access token...\n", null);
				String accessToken = token.getAccessToken(v).getToken();
				txtBuffer.insertString(txtBuffer.getLength(), "CCESS TOKEN ACQUIRED:\n", new SimpleAttributeSet());
				txtBuffer.insertString(txtBuffer.getLength(), accessToken + "\n", null);
				txtUserToken.setText(accessToken);
				txtBuffer.insertString(txtBuffer.getLength(), DASH, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(ae.getActionCommand().equals("clear_buff")){
			try {
				txtBuffer.insertString(0, "", null);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
	}

}
