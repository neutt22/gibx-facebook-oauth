package com.guevent.gibx.jim.remote;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;

public class JRemoteButton extends JButton implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	private static JFrame viewPane = new JFrame("GIBX Spy v.0.1");
	
	
	private F360RemoteServer remoteServer;
	private String name;
	
	public JRemoteButton(String text, F360RemoteServer rServer){
		super(text);
		name = text;
		remoteServer = rServer;
		addActionListener(this);
		
		viewPane.add(rServer.getRemotePane());
		viewPane.setAlwaysOnTop(true);
		viewPane.setSize(1000, 700);
		viewPane.setLocationRelativeTo(null);
	}
	
	public void actionPerformed(ActionEvent ae){
		List<ClientInfo> connections = remoteServer.getConnections();
		JButton btn = (JButton)ae.getSource();
		viewPane.setTitle("GIBX Spy v.0.1 | Unreachable: " + btn.getText());
		
		if(btn.getText().equalsIgnoreCase("DISCONNECT")){
			turnOffAll();
			BufferedImage bufferedImage = new BufferedImage(600, 400, BufferedImage.TYPE_INT_ARGB);
			remoteServer.getRemotePane().setBufferedImage(bufferedImage);
			return;
		}
		for(ClientInfo connection : connections){
			ClientInfo msg = new ClientInfo();
			if(connection.getPcName().equalsIgnoreCase(btn.getText())){
				msg.setMessage("turn_on");
				connection.getConnection().sendTCP(msg);
				viewPane.setTitle("GIBX Spy v.0.1 | Viewing: " + connection.getPcName() + " | Size: " + (remoteServer.getBufferedSize().length / 1024) + "kB");
				viewPane.setVisible(true);
				//11361927
			}else{
				msg.setMessage("turn_off");
				connection.getConnection().sendTCP(msg);
			}
		}
		
		
	}
	
	private void turnOffAll(){
		List<ClientInfo> connections = remoteServer.getConnections();
		for(ClientInfo clientInfo : connections){
			ClientInfo msg = new ClientInfo();
			msg.setMessage("turn_off");
			clientInfo.getConnection().sendTCP(msg);
		}
	}

}
