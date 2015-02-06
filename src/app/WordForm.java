package app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class WordForm extends JFrame{
	private static final long serialVersionUID = 1L;
	JLabel lblHira;
	JLabel lbInfo;
	JButton btnClose;
	JButton btnShowAll;
	JButton btnPause;
	JButton btnNext;
	JButton btnPrev;
	JFrame parent;
	String textAll;
	private ImageIcon iconPlay, iconPause;
	public WordForm(JFrame parent) {
		this.parent = parent;
		this.textAll = "";
		this.lblHira = new JLabel("test");
		Font labelFont = lblHira.getFont();
		lblHira.setFont(new Font(labelFont.getName(), Font.PLAIN, 50));
		this.setFont(labelFont);
		this.lblHira.setSize(100, 200);
		
		lbInfo = new JLabel("Info");

		this.setSize(500, 200);
		this.setTitle("Word");
		
		Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/app/icon.png"));
		this.setIconImage(image);
		btnClose = new JButton("Close");
		btnShowAll = new JButton("Show all");
		btnPause = new JButton("Pause");
		btnNext = new JButton("Next");
		btnPrev = new JButton("Previous");
		
		BufferedImage buttonIcon;
		try {
			
			iconPause = new ImageIcon(ImageIO.read(getClass().getResource("/app/pause.png")));
			iconPlay = new ImageIcon(ImageIO.read(getClass().getResource("/app/play.png")));
			
			buttonIcon = ImageIO.read(getClass().getResource("/app/close.png"));
			btnClose.setIcon(new ImageIcon(buttonIcon));
			
			buttonIcon = ImageIO.read(getClass().getResource("/app/all.png"));
			btnShowAll.setIcon(new ImageIcon(buttonIcon));
			
			buttonIcon = ImageIO.read(getClass().getResource("/app/pause.png"));
			btnPause.setIcon(new ImageIcon(buttonIcon));
			
			buttonIcon = ImageIO.read(getClass().getResource("/app/next.png"));
			btnNext.setIcon(new ImageIcon(buttonIcon));
			
			buttonIcon = ImageIO.read(getClass().getResource("/app/prev.png"));
			btnPrev.setIcon(new ImageIcon(buttonIcon));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		
		btnClose.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				
				closeFrame();
			}
		});
		
		btnShowAll.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, getTextAll());
				
			}
		});
		
		btnPause.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(getParent().isPause) {
					getParent().isPause = false;
					btnPause.setText("Pause");
					btnPause.setIcon(iconPause);
				} else {
					getParent().isPause = true;
					btnPause.setText("Resume");
					btnPause.setIcon(iconPlay);
				}
			}
		});
			
		btnNext.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (getParent().lstWord.size() >= getParent().currentIndex) {
					getParent().currentIndex++;
					getParent().showText();
				}
			}
		});
		
		btnPrev.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (getParent().currentIndex > 0) {
					getParent().currentIndex--;
					getParent().showText();
				}
			}
		});
		
		BorderLayout layout = new BorderLayout();
		layout.setHgap(1);
		layout.setVgap(1);
		this.setLayout(layout);
		
		JPanel groupButton = new JPanel();
		groupButton.setLayout(new GridLayout());
		groupButton.add(btnClose);
		groupButton.add(btnShowAll);
		groupButton.add(btnPause);
		groupButton.add(btnPrev);
		groupButton.add(btnNext);
		
		this.add(lblHira, BorderLayout.CENTER);
		this.add(groupButton, BorderLayout.SOUTH);
		this.add(lbInfo, BorderLayout.NORTH);
		
		Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		
		setLocation(screenSize.width - this.getWidth(), screenSize.height - this.getHeight());
	}
	
	public void closeFrame(){
	    //super.dispose();
		//parent.setVisible(true);
	}
	
	public void setHiraText(String hira) {
		lblHira.setText(hira);
	}
	
	public MainLayout getParent(){
		return (MainLayout) this.parent;
	}
	public String getTextAll(){
		return this.textAll;
	}
	
	public void setTextAll(String text){
		this.textAll = text;
	}
	
	public void setInfo(String info) {
		lbInfo.setText(info);
	}
}
