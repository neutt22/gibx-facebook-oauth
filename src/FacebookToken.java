
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
  
  public FacebookToken(){
  	OAuthService service = new ServiceBuilder()
	  .provider(FacebookApi.class)
	  .apiKey(Main.APP_ID)
	  .apiSecret(Main.APP_SECRET)
	  .callback("http://localhost/callback")
	  .scope("manage_pages,publish_actions,publish_pages")
	  .build();
	
	// Obtain the Authorization URL
	System.out.println("Fetching the request token...");
	String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
	System.out.println(authorizationUrl);
	String u = null;
	try {
		u = readURL(authorizationUrl);
	} catch (Exception e) {
		e.printStackTrace();
	}
	Verifier verifier = new Verifier(u);
	System.out.println();
	
	// Trade the Request Token and Verfier for the Access Token
	System.out.println("Trading the Request Token for an Access Token...");
	Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);
	System.out.println("Got the Access Token!");
	this.accessToken = accessToken.getToken();
	System.out.println(this.accessToken);
  }
  
  private String readURL(String url) throws Exception{
	  final WebClient web = new WebClient();
	  HtmlPage page = web.getPage(url);
	  HtmlForm form = (HtmlForm) page.getElementById("login_form");
	  HtmlTextInput txtEmail = form.getInputByName("email");
	  txtEmail.setValueAttribute(Main.USERNAME);
	  HtmlPasswordInput txtPass = form.getInputByName("pass");
	  txtPass.setValueAttribute(Main.PASSWORD);
	  HtmlSubmitInput button = (HtmlSubmitInput) form.getInputsByValue("Log In").get(0);
	  HtmlPage page2 = button.click();
	  System.out.println("Request Token/Code: " + page2.getUrl().toString());
	  String urls[] = page2.getUrl().toString().split("=");
	  return urls[1];
  }

}