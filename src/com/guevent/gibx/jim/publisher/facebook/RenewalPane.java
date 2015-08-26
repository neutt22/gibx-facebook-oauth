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
import org.joda.time.DateTime;

import com.guevent.gibx.jim.excel.RenewalChecker;


public class RenewalPane extends JPanel {

	private static final long serialVersionUID = 1L;
	public static SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, Y");
	
	public final static String RENEWAL_LINE = "We wish to inform you that your F360 Protect insurance coverage is due " +
			"to renewal on " + new DateTime(new Date()).plusDays(RenewalChecker.WAITING_PERIOD).toString("MMMM dd, Y") + ".";
	public final static String RENEWAL_LINE_2 = "As such, kindly give us your renewal instructions before the expiry of your policy. Thank you.";
	
	public static String getDate(){ return formatter.format(new Date()); }
	
	public RenewalPane(){
		bdate = getDate();
		loadImage(); //paint the image
		saveToFile("J://lol.png");
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
	
	public void addRenewal(String name){
		names.add(WordUtils.capitalize(new String(name).toLowerCase()));
		loadImage();
	}

	private BufferedImage bufferedImage;
	
	private List<String> names = new ArrayList<String>();
	private String bdate;
	
	public void loadImage(){
		try {
			bufferedImage = ImageIO.read(new File("J:/renewal_templates/renewal_template_0.png"));
			Graphics2D g2 = bufferedImage.createGraphics();
			Font font = new Font("Times New Roman", Font.PLAIN, 15);
			g2.setColor(Color.BLACK);
			
			int x = 200;
			int num =0;
			for(String name : names){
				g2.setColor(Color.BLACK);
				g2.setFont(font);
				x += 20;
				g2.drawString(++num + ". " + name, 15, x);
			}
			
			font = new Font("Arial", Font.PLAIN, 15);
			g2.setFont(font);
			g2.drawString(bdate, 25, 25);
			
			font = new Font("Arial", Font.PLAIN, 10);
			g2.setFont(font);
			g2.drawString(RENEWAL_LINE, 50, 180);
			g2.drawString(RENEWAL_LINE_2, 50, 190);
			
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
