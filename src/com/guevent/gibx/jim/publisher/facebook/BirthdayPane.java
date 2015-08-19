package com.guevent.gibx.jim.publisher.facebook;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class BirthdayPane extends JPanel {

	private static final long serialVersionUID = 1L;
	
	public BirthdayPane(){
		name = "[PRESS THE BIRTHDATE BUTTON]";
		bdate = "---------";
		loadImage(); //paint the image
	}
	
	public void saveToFile(String fileName){
		try{
			ImageIO.write(bufferedImage, "PNG", new File(fileName));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void repaintPane(){
		repaint();
	}
	
	private String name, bdate;
	
	public void setCelebrant(String name, String bdate){
		this.name = name;
		this.bdate = bdate;
		loadImage();
	}

	private BufferedImage bufferedImage;
	
	public void loadImage(){
		try {
			bufferedImage = ImageIO.read(new File("J:/bday.jpg"));
			Graphics2D g2 = bufferedImage.createGraphics();
			Font font = new Font("Serif", Font.PLAIN, 30);
			if(name.length() >= 15) font = new Font("Serif", Font.PLAIN, 25);
			g2.setColor(Color.WHITE);
			g2.setFont(font);
			g2.drawString(name, 10, 320);
			g2.drawString(name, 10, 320);
			g2.setColor(Color.GRAY);
			g2.drawString(bdate, 10, 350);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.drawImage(bufferedImage, 0, 0, 200, 200, null);
	}
}
