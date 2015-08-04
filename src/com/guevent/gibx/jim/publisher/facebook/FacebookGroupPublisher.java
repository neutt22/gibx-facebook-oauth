package com.guevent.gibx.jim.publisher.facebook;

import com.guevent.gibx.jim.utils.PhotoBytes;
import com.restfb.BinaryAttachment;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.FacebookType;
import com.restfb.types.Group;

public class FacebookGroupPublisher {
	
	private String F360_PROTECT_FORUM_ID;
	private FacebookClient fbClient;
	
	public FacebookGroupPublisher(String accessToken){
		fbClient = new DefaultFacebookClient(accessToken, Version.VERSION_2_4);
		Connection<Group> groups = fbClient.fetchConnection("me/groups", Group.class);
		F360_PROTECT_FORUM_ID = groups.getData().get(0).getId();
	}
	
	public String postGroupFeed(String feed){
		FacebookType pubMsgResponse = fbClient.publish(
				F360_PROTECT_FORUM_ID + "/feed", 
				FacebookType.class,
				Parameter.with("message", feed)
		);
		return pubMsgResponse.getId();
	}
	
	public String postGroupFeedWithPhoto(String feed, String imageLocation){
		FacebookType pubPhotoResponse = fbClient.publish(
				F360_PROTECT_FORUM_ID + "/photos",
				FacebookType.class,
				BinaryAttachment.with("cat.png", PhotoBytes.fetchBytesFromImage(imageLocation)),
				Parameter.with("message", feed)
		);
		return pubPhotoResponse.getId();
	}
	
	public String getGroupId(){
		return F360_PROTECT_FORUM_ID;
	}

}
