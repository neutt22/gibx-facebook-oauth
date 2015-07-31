package com.guevent.gibx.jim.fboauth.view;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;

public class FBPublisher extends JDialog implements ActionListener, MouseListener{

	private static final long serialVersionUID = 1L;
	
	private JTextArea txtFeed = new JTextArea();
	private JButton btnPost = new JButton("Post");
	private JButton btnCancel;
	private JLabel lblImage = new JLabel("Not specified.");
	
	private String feed;
	
	public String getFeed() { return feed; }
	public String getImage() { return lblImage.getText(); }

	public FBPublisher(){
		btnCancel = new JButton("Cancel");
		setTitle("GIBX Publisher");
		JPanel pane = new JPanel();
		pane.setLayout(new MigLayout("", "[grow]", "[grow]"));
		pane.add(new JLabel("Publish a post:"), "wrap");
		pane.add(new JScrollPane(txtFeed), "w 400, h 300, grow, wrap");
		pane.add(new JLabel("Image:"), "split");
		pane.add(lblImage, "wrap");
		pane.add(btnPost, "w 120, split, right");
		pane.add(btnCancel, "w 120, wrap");
		add(pane);
		pack();
		setLocationRelativeTo(null);
		btnCancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				setVisible(false);
			}
		});
		btnPost.addActionListener(this);
		lblImage.setCursor(new Cursor(Cursor.HAND_CURSOR));
		lblImage.addMouseListener(this);
		setModal(true);
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent ae){
		feed = txtFeed.getText();
		setVisible(false);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		JFileChooser fileChooser = new JFileChooser("Choose a picture");
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Images", "jpg", "png"));
		int res = fileChooser.showOpenDialog(null);
		if(res == JFileChooser.APPROVE_OPTION){
			lblImage.setText(fileChooser.getSelectedFile().toString());
		}
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
