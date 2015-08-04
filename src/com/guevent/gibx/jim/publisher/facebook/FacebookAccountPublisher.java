package com.guevent.gibx.jim.publisher.facebook;

import com.guevent.gibx.jim.utils.PhotoBytes;
import com.restfb.BinaryAttachment;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.Account;
import com.restfb.types.FacebookType;


public class FacebookAccountPublisher {
	
	private String accessToken;
	private String accountName;
	private FacebookClient myFb;
	private Connection<Account> accounts;
	
	public FacebookAccountPublisher(String token){
		accessToken = token;
		myFb = new DefaultFacebookClient(accessToken, Version.VERSION_2_4);
		accounts = myFb.fetchConnection("me/accounts", Account.class);
		
	}
	
	public String postFeed(int account_index, String feed){
		String accountToken = accounts.getData().get(account_index).getAccessToken();
		accountName = accounts.getData().get(account_index).getId();
		FacebookClient account = new DefaultFacebookClient(accountToken, Version.VERSION_2_4);
		FacebookType pubAcctMsg = account.publish(
				"me/feed", 
				FacebookType.class,
				Parameter.with("message", feed)
		);
		return pubAcctMsg.getId().split("_")[1];
	}
	
	public String postPhotoFeed(int account_index, String feed, String image){
		String accountToken = accounts.getData().get(account_index).getAccessToken();
		accountName = accounts.getData().get(account_index).getId();
		FacebookClient account = new DefaultFacebookClient(accountToken, Version.VERSION_2_4);
		FacebookType pubAcctMsg = account.publish(
				//F360 FORUM = 1401856530050018
				"me/photos",
				FacebookType.class,
				BinaryAttachment.with("cat.png", PhotoBytes.fetchBytesFromImage(image)),
				Parameter.with("message", feed)
		);
		return pubAcctMsg.getId();
	}
	
	public String getAccountName(){ return accountName; }

}
