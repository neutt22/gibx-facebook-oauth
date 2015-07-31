import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.StyledDocument;

import net.miginfocom.swing.MigLayout;

import org.ini4j.Wini;

public class FBView extends JFrame{
	
	private static final long serialVersionUID = 1L;
	public static String USERNAME, PASSWORD;
	private GIBXAccount gibxAccount;
	public static String APPDATA = System.getenv("APPDATA") + "\\";
	public static String INI_NAME = "gibx_conf.ini";

	public static void main(String args[]){
		USERNAME = args[0];
		PASSWORD = args[1];
		new FBView();
	}
	
	public FBView(){
		super("GIBX Facebook Group Updater v." + Main.VERSION);
		setLayout(new MigLayout("", "[grow]", "[grow]"));
		setupMenu();
		add(setupHead(), "wrap");
		add(setupBuffer(), "wrap");
		setupListener();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setupSkin();
		pack();
		init();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private Controller controller;
	private void setupListener(){
		btnCheckToken.setActionCommand("get_user_token");
		btnCheckToken.addActionListener(controller);
		
		btnClear.setActionCommand("clear_buff");
		btnClear.addActionListener(controller);
		
		btnUpdateToken.setActionCommand("update_token");
		btnUpdateToken.addActionListener(controller);
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
	private StyledDocument docBuff = txtBuffer.getStyledDocument();
	private JProgressBar prgBuffer = new JProgressBar(0, 100);
	private JButton btnApply = new JButton("Apply");
	private JButton btnClear = new JButton("Clear");
	private Cursor textCursor = new Cursor(Cursor.TEXT_CURSOR);
	private JButton btnKill = new JButton("Kill");
	private JPanel setupBuffer(){
		JPanel pane = new JPanel(new MigLayout("", "[grow]", "[grow]"));
		txtBuffer.setCursor(textCursor);
		txtBuffer.setEditable(false);
		pane.add(new JLabel("Buffer:"), "split");
		pane.add(new JSeparator(), "growx");
		pane.add(btnKill, "wrap");
		pane.add(new JScrollPane(txtBuffer), "grow, h 400, wrap, width 530");
		pane.add(prgBuffer, "grow, split");
		pane.add(btnApply);
		pane.add(btnClear);
		gibxAccount = new GIBXAccount(txtUsername, txtPassword);
		controller = new Controller(docBuff, txtUserToken, gibxAccount);
		txtBuffer.setForeground(Color.WHITE);
		txtBuffer.setBackground(Color.decode("#383838"));
		controller.setBufferPane(txtBuffer);
		((DefaultCaret)txtBuffer.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		return pane;
	}
	
	//ToDo: Timer when is the next update
	private JTextField txtUsername = new JTextField(USERNAME);
	private JPasswordField txtPassword = new JPasswordField(PASSWORD);
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
		pane.add(txtUsername, "w 150, grow");
		pane.add(new JLabel("Password:"), "gapleft 30, split 3");
		pane.add(txtPassword, "w 150, grow");
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
	
	private JMenuBar mnuBar = new JMenuBar();
	private JMenu mnuFile = new JMenu("File");
	private JMenu mnuPublish = new JMenu("Publish");
	private JMenu mnuGroup = new JMenu("Group");
	private JMenu mnuAbout = new JMenu("About");
	private JMenu mnuAccounts = new JMenu("Accounts");
	private JMenu mnuRemote = new JMenu("Remote");
	private JMenu mnuTools = new JMenu("Tools");

}
