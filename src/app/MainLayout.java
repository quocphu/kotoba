package app;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.UIManager;

import bean.Word;
import dao.FileDao;

public class MainLayout extends JFrame {

	private static final long serialVersionUID = 1L;
	// private JFrame this;
	private JPanel top;
	private JPanel bottom;

	private JScrollPane scrollPane;
	private JList listPane;

	private JButton btnStart;
	private JButton btnReload;
	private JButton btnStop;

	TrayIcon trayIcon;
	SystemTray tray;
	private WordForm wordForm;
	Thread thread;
	public Boolean isRunning = true;
	public Boolean isPause = false;
	int len = 0;
	public int currentIndex = 0;
	private static long  waitTime = 1 * 60 * 1000;
	private String hiraMenuText ="Hiragana";
	private String kanjiMenuText ="Kanji";
	private String englishMenuText ="English";
	private String starMenuText = " (*)";
	private String displayText = "";
	public String textAll = "";
	public ArrayList<Word> lstWord;
	
	public MainLayout() {
		setTitle("ことば");
		listPane = new JList();
		listPane.setSize(400, 200);

		scrollPane = new JScrollPane(listPane);
		scrollPane.setSize(400, 200);

		top = new JPanel();
		top.setSize(400, 200);
		top.setLayout(new GridLayout(1,1));
		top.add(scrollPane);

		btnStart = new JButton("Start");
		btnReload = new JButton("Reload");
		btnStop = new JButton("Stop");
		
		bottom = new JPanel();
		bottom.setLayout(new GridLayout(0, 3));
		bottom.add(btnStart);
		bottom.add(btnReload);
		bottom.add(btnStop);
		
		BorderLayout layout = new BorderLayout(2, 0);
		layout.setHgap(10);
		layout.setVgap(10);
		
		this.setLayout(layout);

		this.add(top, BorderLayout.CENTER);
		this.add(bottom, BorderLayout.SOUTH);

		this.setSize(400, 250);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				setExtendedState(JFrame.ICONIFIED);
				//System.exit(0);
			}
		});
	
		setButtonStartEvent();

		wordForm = new WordForm(this);
		wordForm.setVisible(false);
		
		closeEvent();
		setButtonStopEvent();
		createSystemTray();
		
		// Set position on right bottom of screen
		Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(screenSize.width - this.getWidth(), screenSize.height - this.getHeight());
	}

	public void display() {
		this.setVisible(true);
		this.setState(JFrame.NORMAL);
	}

	public void minimize(){
//		this.setVisible(true);
		this.setState(JFrame.ICONIFIED);
	}

	public void setListData(ListModel model) {
		this.listPane.setModel(model);
	}

	private void setButtonStopEvent() {
		this.btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isRunning = false;
				wordForm.setVisible(false);
			}
		});
	}
	
	private void setButtonStartEvent() {
		this.btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (listPane.getSelectedValue() == null) {
					JOptionPane.showMessageDialog(null, "Please select file!");
					return;
				}

				if (!wordForm.isVisible()) {
					minimize();
					wordForm.setVisible(true);
				}

				String path = App.filePath + File.separator + listPane.getSelectedValue().toString();
				
				try {
					lstWord = FileDao.readWord(path);
					len = lstWord.size();
					
					if (thread != null && thread.isAlive()) {
						currentIndex = 0;
						isRunning = false;
						try {
							//thread.join();
							thread.stop();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
					
					isRunning = true;
					
					thread = new Thread(new Runnable() {
						
						@Override
						public void run() {
							while (isRunning) {
								if (isPause) continue;
								
								showText();
								
								currentIndex++;
								
								if(currentIndex >= len) {
									currentIndex = 0;
								}
								
								try {
									System.out.print("time: "+waitTime);
									Thread.sleep(waitTime);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}
					});
					thread.start();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	private void closeEvent(){
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        isRunning = false;
		    }
		});
	}
	
	/**
	 * Time as minute
	 * @param time
	 */
	public static void setWaitTime(long time) {
		waitTime = time * 60 * 1000;
	}
	
	/**
	 * Return waiting time as minutes
	 * @return
	 */
	public static long getWaitTime() {
		return waitTime / 60 / 1000;
	}
	private void createSystemTray(){
		System.out.println("creating instance");
		try {
			System.out.println("setting look and feel");
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Unable to set LookAndFeel");
		}
		if (SystemTray.isSupported()) {
			System.out.println("system tray supported");
			tray = SystemTray.getSystemTray();
			Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/app/icon.png"));
			ActionListener exitListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.out.println("Exiting....");
					System.exit(0);
				}
			};

			PopupMenu popup = new PopupMenu();
			MenuItem defaultItem = new MenuItem("Exit");
			defaultItem.addActionListener(exitListener);
			popup.add(defaultItem);
			defaultItem = new MenuItem("Open");
			defaultItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setVisible(true);
					setExtendedState(JFrame.NORMAL);
				}
			});
			popup.add(defaultItem);
			
			// Create sub menu display text
			Menu subPopup = new Menu("Display text");
			final MenuItem hira = new MenuItem(hiraMenuText + starMenuText);
			final MenuItem kanji = new MenuItem(kanjiMenuText);
			final MenuItem english = new MenuItem(englishMenuText);
			ActionListener selectTextListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String cmd = e.getActionCommand();
					App.displayText = cmd;
					switch (cmd) {
						case App.KANJI:
							kanji.setLabel(kanjiMenuText + starMenuText);
							hira.setLabel(hiraMenuText);
							english.setLabel(englishMenuText);
							break;
						case App.EN:
							kanji.setLabel(kanjiMenuText);
							hira.setLabel(hiraMenuText);
							english.setLabel(englishMenuText + starMenuText);
							break;
						default:
							kanji.setLabel(kanjiMenuText);
							hira.setLabel(hiraMenuText + starMenuText);
							english.setLabel(englishMenuText);
							break;
					}
				}
			};
			hira.addActionListener(selectTextListener);
			kanji.addActionListener(selectTextListener);
			english.addActionListener(selectTextListener);
			subPopup.add(hira);
			subPopup.add(kanji);
			subPopup.add(english);
			
			hira.setActionCommand(App.HIRA);
			kanji.setActionCommand(App.KANJI);
			english.setActionCommand(App.EN);
			
			// Create sub menu time interval
			Menu subTime = new Menu("Time interval");
			
			for(int i = 1; i <= 10; i++) {
				final MenuItem menuItem = new MenuItem(i+" minutes");
				menuItem.setActionCommand(i + "");
				menuItem.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						int time = Integer.parseInt(e.getActionCommand());
						MainLayout.setWaitTime(time);
						wordForm.setTitle(getWaitTime() + "minutes");
					}
				});
				subTime.add(menuItem);
			}
			
			popup.add(subPopup);
			popup.add(subTime);
			trayIcon = new TrayIcon(image, "SystemTray Demo", popup);
			trayIcon.setImageAutoSize(true);
		} else {
			System.out.println("system tray not supported");
		}

		addWindowStateListener(new WindowStateListener() {
			public void windowStateChanged(WindowEvent e) {
				if (e.getNewState() == ICONIFIED) {
					try {
						tray.add(trayIcon);
						setVisible(false);
						System.out.println("added to SystemTray");
					} catch (AWTException ex) {
						System.out.println("unable to add to tray");
					}
				}
				if (e.getNewState() == 7) {
					try {
						tray.add(trayIcon);
						setVisible(false);
						System.out.println("added to SystemTray");
					} catch (AWTException ex) {
						System.out.println("unable to add to system tray");
					}
				}
				if (e.getNewState() == MAXIMIZED_BOTH) {
					tray.remove(trayIcon);
					setVisible(true);
					System.out.println("Tray icon removed");
				}
				if (e.getNewState() == NORMAL) {
					tray.remove(trayIcon);
					setVisible(true);
					System.out.println("Tray icon removed");
				}
			}
		});
		
		setIconImage(Toolkit.getDefaultToolkit().getImage("icon.png"));
	}
	
	public void showText() {
		if(App.displayText.equals(App.EN)) {
			displayText = lstWord.get(currentIndex).getEnglish();
		} else if(App.displayText.equals(App.KANJI)) {
			displayText = lstWord.get(currentIndex).getKanji();
		} else {
			displayText = lstWord.get(currentIndex).getHira();
		}

		textAll = "";
		textAll += lstWord.get(currentIndex).getKanji() + "\n";
		textAll += lstWord.get(currentIndex).getHira() + "\n";
		textAll += lstWord.get(currentIndex).getEnglish() ;

		wordForm.setHiraText(displayText);
		wordForm.setTextAll(textAll);
		wordForm.setVisible(true);
		wordForm.setAlwaysOnTop(true); 
		wordForm.setAlwaysOnTop(false);
		wordForm.setTitle(getWaitTime() + " minutes");
		wordForm.setInfo(currentIndex + "/" + lstWord.size());
	}
}
