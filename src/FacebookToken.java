
import javax.swing.JTextArea;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FacebookApi;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class FacebookToken {
	
	private String accessToken;
	public String getAccessToken(){ return accessToken; }

	private static final Token EMPTY_TOKEN = null;
	
	private OAuthService service;
	
	public void buildOAuthService(){
		service = new ServiceBuilder()
		  .provider(FacebookApi.class)
		  .apiKey(Main.APP_ID)
		  .apiSecret(Main.APP_SECRET)
		  .callback("http://localhost/callback")
		  .scope("manage_pages,publish_actions,publish_pages")
		  .build();
	}
	
	public String getAuthUrl(){
		return service.getAuthorizationUrl(EMPTY_TOKEN);
	}
	
	public Token getAccessToken(Verifier verifier){
		return service.getAccessToken(EMPTY_TOKEN, verifier);
	}
	
	
	public String readURL(String url, String username, String password) throws Exception{
		final WebClient web = new WebClient();
		HtmlPage page = web.getPage(url);
		HtmlForm form = (HtmlForm) page.getElementById("login_form");
		HtmlTextInput txtEmail = form.getInputByName("email");
		txtEmail.setValueAttribute(username);
		HtmlPasswordInput txtPass = form.getInputByName("pass");
		txtPass.setValueAttribute(password);
		HtmlSubmitInput button = (HtmlSubmitInput) form.getInputsByValue("Log In").get(0);
		HtmlPage page2 = button.click();
		String urls[] = page2.getUrl().toString().split("=");
		return urls[1];
	}

}