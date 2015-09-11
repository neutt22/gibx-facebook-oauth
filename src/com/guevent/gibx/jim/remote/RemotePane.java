package com.guevent.gibx.jim.remote;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class RemotePane extends JPanel{

	private static final long serialVersionUID = 1L;

	private BufferedImage bufferedImage;
	
	public void setBufferedImage(BufferedImage bufferedImage){
		this.bufferedImage = bufferedImage;
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(bufferedImage, 0, 0, this.getSize().width, this.getSize().height, null);
	}

}
