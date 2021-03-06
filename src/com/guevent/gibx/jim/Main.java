package com.guevent.gibx.jim;
import static java.lang.System.out;

import java.util.List;

import com.guevent.gibx.jim.utils.PhotoBytes;
import com.restfb.BinaryAttachment;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.json.JsonObject;
import com.restfb.types.Account;
import com.restfb.types.FacebookType;
import com.restfb.types.Group;
import com.restfb.types.Page;
import com.restfb.types.Post;
import com.restfb.types.User;


public class Main {
	
	public static String USERNAME, PASSWORD, VERSION;
	
	public static final String APP_ID = "504745223013550";
	public static final String APP_SECRET = "40ac485643dc8675ff2cbcb8434022b2";
	
	private FacebookClient facebookClient;
	
	public Main(){
		String accessToken = "CAAHLECe4RK4BAEIfqJWCTGich3CwxR5CdZBwh0JhtwzwjET2I1jZBf9JbgUS0H2I2ZAo3L9QFOpWPstDUOG3PBIZBcBZBtvgxF4uhgyjdVXV98imLYZBjPwKa0rgoMQ7yKIrzkDbUQC1VNAyxZCSZCgPGceuvCZC6vhoTa15OiFPyetRFAl6ryZADjQ0E0TzHZAtRFNUA1u3UdtDnp55uWcfxrV";
		
		facebookClient = new DefaultFacebookClient(accessToken, Version.VERSION_2_4);
		Connection<Group> groups = facebookClient.fetchConnection("me/groups", Group.class);
		out.println("Group: " + groups.getData().get(0));
		//postFeed("Attention:", groups.getData().get(0).getId(), facebookClient);
		
//		String claimsAppToken = accounts.getData().get(0).getAccessToken();
//		out.println("Claims Token: " + claimsAppToken);
//		FacebookClient claimsFB = new DefaultFacebookClient(claimsAppToken, Version.VERSION_2_4);
//		Connection<Post> posts = claimsFB.fetchConnection("me/feed", Post.class);
//		out.println("My Post: " + posts.getData().get(0));
//		
//		postFeedWithPhoto("Scribe with manage_accounts, publish_pages, and publish_actions permissions", "me", "J:/Chrysanthemum.png", claimsFB);
		
	}
	
	public static void main(String args[]) throws Exception{
		USERNAME = args[0];
		PASSWORD = args[1];
		VERSION = "1.0";
		new Main();
	}
	
	public void postFeed(String feed, String page, FacebookClient fbClient){
		FacebookType pubMsgResponse = fbClient.publish(
				page + "/feed", 
				FacebookType.class,
				Parameter.with("message", feed)
		);
		out.println(pubMsgResponse.getId());
	}
	
	public void postFeedWithPhoto(String feed, String page, String imageLocation, FacebookClient fbClient){
		FacebookType pubPhotoResponse = fbClient.publish(
				page + "/photos",
				FacebookType.class,
				BinaryAttachment.with("cat.png", PhotoBytes.fetchBytesFromImage(imageLocation), "image/png"),
				Parameter.with("message", feed)
		);
		out.println(pubPhotoResponse.getId());
	}

}
