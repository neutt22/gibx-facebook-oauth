import java.awt.event.KeyEvent;

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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.miginfocom.swing.MigLayout;




public class FBView extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private static String USERNAME, PASSWORD;

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
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setupSkin();
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private JTextArea txtBuffer = new JTextArea();
	private JProgressBar prgBuffer = new JProgressBar(0, 100);
	private JButton btnApply = new JButton("Apply");
	private JPanel setupBuffer(){
		JPanel pane = new JPanel(new MigLayout("", "[grow]", "[grow]"));
		pane.add(new JScrollPane(txtBuffer), "w 450, h 300, grow, wrap");
		pane.add(prgBuffer, "grow, split");
		pane.add(btnApply);
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
	private JPanel setupHead(){
		JPanel pane = new JPanel(new MigLayout("", "[grow]", "[grow]"));
		pane.add(new JLabel("Username:"), "split 2");
		pane.add(txtUsername, "w 150, grow");
		pane.add(new JLabel("Password:"), "gapleft 30");
		pane.add(txtPassword, "w 150, grow, wrap");
		pane.add(chkBirthday, "span 2");
		pane.add(btnPublishText, "w 120, wrap, right");
		pane.add(chkRenewal, "span 2");
		pane.add(btnPublishPhoto, "w 120, wrap, right");
		pane.add(chkCongrats, "wrap");
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
