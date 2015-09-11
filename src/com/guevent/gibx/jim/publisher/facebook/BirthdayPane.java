package com.guevent.gibx.jim.publisher.facebook;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.apache.commons.lang3.text.WordUtils;

import com.guevent.gibx.jim.utils.BirthdayUtils;


public class BirthdayPane extends JPanel {

	private static final long serialVersionUID = 1L;
	public static SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, Y");
	
	public static String getDate(){ return formatter.format(new Date()); }
	
	public BirthdayPane(){
		bdate = getDate();
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
	
	public void setCelebrant(String name){
		names.add(WordUtils.capitalize(new String(name).toLowerCase()));
		loadImage();
	}

	private BufferedImage bufferedImage;
	
	private List<String> names = new ArrayList<String>();
	private String bdate;
	
	public void loadImage(){
		try {
			bufferedImage = ImageIO.read(new File(BirthdayUtils.getBirthdayTemplate()));
			Graphics2D g2 = bufferedImage.createGraphics();
			Font font = new Font("Serif", Font.PLAIN, 30);
			
			int x = 230;
			int num =0;
			for(String name : names){
				if(name.length() >= 15) font = new Font("Serif", Font.PLAIN, 25);
				g2.setColor(Color.WHITE);
				g2.setFont(font);
				x += 30;
				g2.drawString(++num + ". " + name, 20, x);
			}
			
			g2.setColor(Color.WHITE);
			font = new Font("Century Gothic", Font.PLAIN, 30);
			g2.setFont(font);
			g2.drawString(bdate, 30, 50);
			
		} catch (Exception e) {
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
