package com.guevent.gibx.jim;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FacebookApi;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.guevent.gibx.jim.controller.Controller;

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
		  .scope("manage_pages,publish_actions,publish_pages,user_managed_groups")
		  .build();
	}
	
	public String getAuthUrl(){
		return service.getAuthorizationUrl(EMPTY_TOKEN);
	}
	
	public Token getAccessToken(Verifier verifier){
		
		return service.getAccessToken(EMPTY_TOKEN, verifier);
	}
	
	
	public String readURL(String url, String username, String password, StyledDocument txtBuffer){
		final WebClient web = new WebClient();
		HtmlPage page;
		try {
			page = web.getPage(url);
			HtmlForm form = (HtmlForm) page.getElementById("login_form");
			HtmlTextInput txtEmail = form.getInputByName("email");
			txtEmail.setValueAttribute(username);
			HtmlPasswordInput txtPass = form.getInputByName("pass");
			txtPass.setValueAttribute(password);
			HtmlSubmitInput button = (HtmlSubmitInput) form.getInputsByValue("Log In").get(0);
			HtmlPage page2 = button.click();
			String urls[] = page2.getUrl().toString().split("=");
			web.close();
			return urls[1];
		} catch (FailingHttpStatusCodeException e) {
			try {
				txtBuffer.insertString(txtBuffer.getLength(), "FAILING HTTP ERROR: " + e.getMessage() + "\n", Controller.STYLE_FAILED);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (MalformedURLException e) {
			try {
				txtBuffer.insertString(txtBuffer.getLength(), "MALFORMED URL ERROR: " + e.getMessage() + "\n", Controller.STYLE_FAILED);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (IOException e) {
			try {
				txtBuffer.insertString(txtBuffer.getLength(), "IO ERROR: " + e.getMessage() + "\n", Controller.STYLE_FAILED);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return null;
	}

}