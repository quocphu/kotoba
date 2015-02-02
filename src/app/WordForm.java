package app;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class WordForm extends JFrame{
	private static final long serialVersionUID = 1L;
	JLabel lblHira;
	JButton btnClose;
	JFrame parent;
	public WordForm(JFrame parent) {
		this.parent = parent;
		this.lblHira = new JLabel("test");
		Font labelFont = lblHira.getFont();
		lblHira.setFont(new Font(labelFont.getName(), Font.PLAIN, 50));
		this.setFont(labelFont);
		this.lblHira.setSize(100, 200);
		this.add(lblHira);
		this.setSize(300, 200);
		this.setTitle("chi.ld");
		
		btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				
				closeFrame();
			}
		});
		
		add(btnClose);
		
		FlowLayout layout = new FlowLayout();
		layout.setHgap(1);
		layout.setVgap(1);
		this.setLayout(layout);
		
		Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		
		setLocation(screenSize.width - this.getWidth(), screenSize.height - this.getHeight());
	}
	
	public void closeFrame(){
	    //super.dispose();
		parent.setVisible(true);
	}
	
	public void setHiraText(String hira) {
		lblHira.setText(hira);
	}
	
}
