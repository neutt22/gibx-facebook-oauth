package com.guevent.gibx.jim;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultCaret;
import javax.swing.text.StyledDocument;

import net.miginfocom.swing.MigLayout;

import org.ini4j.Wini;

import com.guevent.gibx.jim.controller.Controller;
import com.guevent.gibx.jim.controller.DBController;
import com.guevent.gibx.jim.excel.birthday.BirthdayChecker;
import com.guevent.gibx.jim.f360.F360Member;
import com.guevent.gibx.jim.utils.Utils;

public class MainView extends JFrame{
	
	private static final long serialVersionUID = 1L;
	public static String USERNAME, PASSWORD;
	private GIBXAccount gibxAccount;
	public static String APPDATA = System.getenv("APPDATA") + "\\";
	public static String INI_NAME = "gibx_conf.ini";

	public static void main(String args[]){
		USERNAME = args[0];
		PASSWORD = args[1];
		//new MainView(new BirthdayChecker().readExcel());
		new MainView(null);
	}
	
	private List<F360Member> members;
	public MainView(List<F360Member> m){
		super("GIBX Facebook Group Updater v." + Main.VERSION);
		setLayout(new MigLayout("fill"));
		setupMenu();
		
		JTabbedPane tabPane = new JTabbedPane();
		JPanel fbPane = new JPanel(new MigLayout("", "[grow]", "[grow]"));
		fbPane.add(setupHead(), "wrap, top");
		//fbPane.add(setupBuffer(), "wrap");
		tabPane.add("Publish", fbPane);
		
		JPanel dbPane = new JPanel(new MigLayout("", "[grow]", "[grow]"));
		dbPane.add(getDbHeadPane(), "wrap, top");
		tabPane.add("F360", dbPane);
		
		add(tabPane, "top, wrap");
		add(setupBuffer());
		setupListener();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setupSkin();
		pack();
		init();
		setLocationRelativeTo(null);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);
		members = m;
	}
	
	private Controller controller;
	private DBController dbController; 
	private void setupListener(){
		btnCheckToken.setActionCommand("get_user_token");
		btnCheckToken.addActionListener(controller);
		
		btnClear.setActionCommand("clear_buff");
		btnClear.addActionListener(controller);
		
		btnUpdateToken.setActionCommand("update_token");
		btnUpdateToken.addActionListener(controller);
		
		btnPublishText.setActionCommand("publish_text");
		btnPublishText.addActionListener(controller);
		
		btnPublishPhoto.setActionCommand("publish_photo");
		btnPublishPhoto.addActionListener(controller);
		
		
		mnuToolsDbLoad.setActionCommand("db_load");
		mnuToolsDbLoad.addActionListener(dbController);
		
		mnuToolsRecursive.setActionCommand("db_recursive");
		mnuToolsRecursive.addActionListener(dbController);
		
		btnDbSearch.setActionCommand("db_search");
		btnDbSearch.addActionListener(dbController);		
	}
	
	private void init(){
		String iniFile = APPDATA + INI_NAME;
		try{
			File file = new File(iniFile);
			if(!file.exists()){
				file.createNewFile();
				Wini ini = new Wini(new File(iniFile));
				ini.put("main", "user_token", "Please generate your API token");
				ini.store();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		Utils.showToken(txtUserToken);
	}
	
	private JTextPane txtBuffer = new JTextPane();
	private StyledDocument docBuff = (StyledDocument) txtBuffer.getDocument();
	private JProgressBar prgBuffer = new JProgressBar(0, 100);
	private JButton btnApply = new JButton("Apply");
	private JButton btnClear = new JButton("Clear");
	private Cursor textCursor = new Cursor(Cursor.TEXT_CURSOR);
	private JPanel setupBuffer(){
		JPanel pane = new JPanel(new MigLayout("", "[grow]", "[grow]"));
		txtBuffer.setCursor(textCursor);
		txtBuffer.setEditable(false);
		pane.add(new JLabel("Buffer:"), "span, split");
		pane.add(new JSeparator(JSeparator.HORIZONTAL), "w 1280, wrap");
		pane.add(new JScrollPane(txtBuffer), "grow, h 800, wrap");
		pane.add(prgBuffer, "grow, split");
		pane.add(btnApply);
		pane.add(btnClear);
		gibxAccount = new GIBXAccount(txtUsername, txtPassword);
		controller = new Controller(docBuff, txtUserToken, gibxAccount, prgBuffer);
		txtBuffer.setForeground(Color.WHITE);
		txtBuffer.setBackground(Color.decode("#383838"));
		controller.setBufferPane(txtBuffer);
		((DefaultCaret)txtBuffer.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		return pane;
	}
	
	//ToDo: Timer when is the next update
	private JTextField txtUsername = new JTextField(20);
	private JPasswordField txtPassword = new JPasswordField(20);
	private JCheckBox chkBirthday = new JCheckBox("Post Birthday Card", true);
	private JCheckBox chkRenewal = new JCheckBox("Post Renewal Card", true);
	private JCheckBox chkCongrats = new JCheckBox("Post Congratulations Card", true);
	private JButton btnPublishText = new JButton("Publish Text");
	private JButton btnPublishPhoto = new JButton("Publish Photo");
	private JTextField txtUserToken = new JTextField("Please specify your API token");
	private JButton btnCheckToken = new JButton("Get Token");
	private JButton btnUpdateToken = new JButton("Update");
	private JPanel setupHead(){
		JPanel pane = new JPanel(new MigLayout("", "[grow]", "[grow]"));
		pane.add(new JLabel("Username:"), "split 2");
		pane.add(txtUsername, "grow");
		pane.add(new JLabel("Password:"), "gapleft 40, split 2");
		pane.add(txtPassword, "grow");
		pane.add(btnCheckToken, "wrap");
		pane.add(chkBirthday);
		pane.add(btnPublishText, "w 120, split 2, center");
		pane.add(btnPublishPhoto, "w 120, wrap");
		pane.add(chkRenewal, "wrap");
		pane.add(chkCongrats, "wrap");
		pane.add(new JLabel("User Token:"), "split 3, span");
		pane.add(txtUserToken, "growx");
		pane.add(btnUpdateToken, "wrap");
		pane.add(new JSeparator(), "growx, span");
		return pane;
	}
	
	private JMenuBar mnuBar = new JMenuBar();
	private JMenu mnuFile = new JMenu("File");
	private JMenu mnuPublish = new JMenu("Publish");
	private JMenu mnuGroup = new JMenu("Group");
	private JMenu mnuAbout = new JMenu("About");
	private JMenu mnuAccounts = new JMenu("Accounts");
	private JMenu mnuRemote = new JMenu("Remote");
	private JMenu mnuTools = new JMenu("Tools");
	private JMenuItem mnuToolsDbLoad = new JMenuItem("Load");
	private JMenuItem mnuToolsRecursive = new JMenuItem("Recursive");
	private void setupMenu(){
		mnuFile.setMnemonic(KeyEvent.VK_F);
		mnuPublish.setMnemonic(KeyEvent.VK_P);
		mnuGroup.setMnemonic(KeyEvent.VK_G);
		mnuAbout.setMnemonic(KeyEvent.VK_X);
		mnuAccounts.setMnemonic(KeyEvent.VK_A);
		mnuRemote.setMnemonic(KeyEvent.VK_R);
		mnuTools.setMnemonic(KeyEvent.VK_T);
		mnuBar.add(mnuFile);
		mnuBar.add(mnuPublish);
		mnuBar.add(mnuGroup);
		mnuBar.add(mnuAccounts);
		mnuBar.add(mnuRemote);
		mnuTools.add(mnuToolsDbLoad);
		mnuTools.add(mnuToolsRecursive);
		mnuBar.add(mnuTools);
		mnuBar.add(mnuAbout);
		setJMenuBar(mnuBar);
	}
	
	private void setupSkin(){
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SwingUtilities.updateComponentTreeUI(this);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
	

	////////////////////////////////////////////// DB VIEW ////////////////////////////////////////////////////////
	
	private JTextField txtDbSearch = new JTextField();
	private JButton btnDbSearch = new JButton("Search");
	private JTable dbTable;
	private DefaultTableModel dbTableModel;
	private String[] columns = {"POLICY", "COC", "PIN CODE", "FIRST NAME", "MIDDLE NAME", "LAST NAME", "OCCUPATION",
			"RELATIONSHIP", "ADDRESS", "PHONE", "BDATE", "AGE", "ACTIVATION PLAN", "PLAN", "BNFRY NME", "BNFRY REL", "COVER"
			, "TYPE"};
	private JButton btnDbRefresh = new JButton("Refresh");
	private JPanel getDbHeadPane(){
		dbTableModel = new DefaultTableModel(
				new Object[][]{},
				columns
		){
			private static final long serialVersionUID = 1L; 
			public boolean isCellEditable(int row, int col){
				return false;
			}
		};
		dbTable = new JTable(dbTableModel);
		JPanel pane = new JPanel(new MigLayout("", "[grow]", "[grow]"));
		pane.add(new JLabel("Search:"), "split");
		pane.add(txtDbSearch, "grow");
		pane.add(btnDbSearch, "wrap");
		pane.add(new JScrollPane(dbTable), "wrap, grow, span, w 1280, h 700");
		pane.add(btnDbRefresh);
		dbController = new DBController(docBuff, txtBuffer);
		return pane;
	}
}