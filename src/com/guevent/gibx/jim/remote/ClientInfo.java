package com.guevent.gibx.jim.remote;

import java.awt.image.BufferedImage;

import com.esotericsoftware.kryonet.Connection;

public class ClientInfo {
	private String pcName;
	private String ipAddress;
	private String message;
	private Connection connection;
	private BufferedImage bufferedImage;
	private String stringBufferedImage;
	
	public ClientInfo(){
		
	}

	public void setBufferedImage(BufferedImage bImage){ bufferedImage = bImage; }
	public void setBufferedImage(String bImage){ stringBufferedImage = bImage; }
	public void setConnection(Connection con){ connection = con; }
	public void setPcName(String pcName){ this.pcName = pcName; }
	public void setIpAddress(String ipAddress){ this.ipAddress = ipAddress; }
	public void setMessage(String message){ this.message = message; }
	
	public BufferedImage getBufferedImage(){ return bufferedImage; }
	public String getStringBufferedImage(){ return stringBufferedImage; }
	public Connection getConnection(){ return connection; }
	public String getPcName(){ return pcName; }
	public String getIpAddress(){ return ipAddress; }
	public String getMessage(){ return message; }
}
