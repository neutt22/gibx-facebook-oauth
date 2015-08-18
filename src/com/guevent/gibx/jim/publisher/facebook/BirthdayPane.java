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
		name = "Jim Paulo Asuncion Ovejera";
		bdate = "August 14, 2015";
		loadImage();
		try{
			ImageIO.write(bufferedImage, "PNG", new File("J:/bday2.png"));
		}catch(Exception e){
			
		}
	}
	
	private String name, bdate;
	
	private BufferedImage bufferedImage;
	
	public void loadImage(){
		try {
			bufferedImage = ImageIO.read(new File("J:/bday.jpg"));
			Graphics2D g2 = bufferedImage.createGraphics();
			Font font = new Font("Serif", Font.PLAIN, 30);
			g2.setColor(Color.WHITE);
			g2.setFont(font);
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
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2.drawImage(bufferedImage, 0, 0, 200, 200, null);
	}
}
