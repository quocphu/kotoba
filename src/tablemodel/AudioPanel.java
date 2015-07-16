package tablemodel;

import java.awt.GridBagLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class AudioPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	public static String BTN_BROWSER_TEXT = "Browser";
	JButton btnBrowser;
	JButton btnPlay;
	String path;
	
	public AudioPanel(String text) {
		 setLayout(new GridBagLayout());
		path = text;
		btnBrowser = new JButton(BTN_BROWSER_TEXT);
		btnPlay = new JButton(path);
	
		add(btnBrowser);
		add(btnPlay);
	}
	
	 public void addActionListener(ActionListener listener) {
		 btnBrowser.addActionListener(listener);
     }
	 public String getPath() {
		 return this.path;
	 }
}
