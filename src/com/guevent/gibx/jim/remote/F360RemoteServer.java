package com.guevent.gibx.jim.remote;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.google.common.io.BaseEncoding;


public class F360RemoteServer {
	
	private List<ClientInfo> connections = new ArrayList<ClientInfo>();
	private Server server;
	private byte bytes[];
	
	public List<ClientInfo> getConnections(){ return connections; }
	public Server getServer(){ return server; }
	
	public byte[] getBufferedSize(){ 
		if(bytes == null) return new byte[0];
		return bytes; 
	}
	
	private RemotePane remotePane;
	public void setRemotePane(RemotePane rPane){
		remotePane = rPane;
	}
	
	public RemotePane getRemotePane(){ return remotePane; }
	
	private OnlineCallBack olCallBack;
	public F360RemoteServer(final OnlineCallBack olCallback){
		this.olCallBack = olCallback;
		server = new Server(5000000, 5000000);
		Kryo kryo = server.getKryo();
	    kryo.register(com.guevent.gibx.jim.remote.ClientInfo.class);
	    server.start();
		System.out.println("Started. Server listening to ports 1604/5...");
		try {
			server.bind(1604, 1605);
		} catch (IOException e) {
			e.printStackTrace();
		}
		server.addListener(new Listener(){
			@Override
			public void disconnected(Connection con){
				System.out.println("Someone has disconnected: " + con.getRemoteAddressTCP());
				for(ClientInfo clientInfo : connections){
					if(clientInfo.getConnection().getID() == con.getID()){
						connections.remove(clientInfo);
						olCallback.connections(connections.size());
						break;
					}
				}
			}
			
			@Override
			public void connected(Connection con){
				System.out.println("Someone has connected: " + con.getRemoteAddressTCP());
			}
			
			@Override
			public void received(Connection con, Object obj){
				if(obj instanceof ClientInfo){
					ClientInfo c = (ClientInfo)obj;
					String command = c.getMessage();
					if(command.equals("new")){
						ClientInfo bot = new ClientInfo();
						bot.setConnection(con);
						bot.setPcName(c.getPcName());
						bot.setIpAddress(c.getIpAddress());
						connections.add(bot);
						olCallback.connections(connections.size());
					}else if(command.equals("screenshot")){
						bytes = BaseEncoding.base64().decode(c.getStringBufferedImage());
						ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
						try {
							BufferedImage bImage = ImageIO.read(bis);
							remotePane.setBufferedImage(bImage);
							bis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
	}
}
