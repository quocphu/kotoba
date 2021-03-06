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
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.UIManager;

import audio.AudioPlayer;
import bean.JListItem;
import bean.Word;
import dao.FileDao;

public class MainLayout extends JFrame {

	private static final long serialVersionUID = 1L;
	// private JFrame this;
	private JPanel top;
	private JPanel bottom;

	private JScrollPane scrollPane;
	private JList<JListItem> listPane;

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
	public int currentIndex = -1;
	public int currentLessonIndex = 0;
	private static long  waitTime = Constant.WAIT_TIME;
	private String hiraMenuText ="Hiragana";
	private String kanjiMenuText ="Kanji";
	private String englishMenuText ="English";
	private String starMenuText = " (*)";
	private String displayText = "";
	public String textAll = "";
	public ArrayList<Word> lstWord;
	public JListItem[] lstAudioFileName;
	public boolean isPlaySound = true;
	
	private String currentFileName = "";
	
	public boolean isRandom = false;
	public ArrayList<Integer> randomIndex;
	
	public MainLayout() {
		setTitle("ことば");
		listPane = new JList<JListItem>();
		listPane.setSize(400, 200);

		scrollPane = new JScrollPane(listPane);
		scrollPane.setSize(400, 200);

		top = new JPanel();
		top.setSize(400, 200);
		top.setLayout(new GridLayout(1,1));
		top.add(scrollPane);

		btnStart = new JButton("Start");
		btnReload = new JButton("Show current");
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
		
		btnReload.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(wordForm == null) return;
				wordForm.setVisible(true);
			}
		});
		
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

	public void setListData(ListModel<JListItem> model) {
		this.listPane.setModel(model);
	}

	private void setButtonStopEvent() {
		this.btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isRunning = false;
				currentIndex = 0;
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

				String textPath = App.textFilePath + listPane.getSelectedValue().toString();
				String audioDirPath = App.audioFilePath + addZeroNumber(listPane.getSelectedIndex() + 1) + File.separator;
				start(textPath, audioDirPath);
				
			}
		});
	}

	private void closeEvent(){
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		       // isRunning = false;
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
					isRunning = false;
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
			
			// Create menu on/off play sound
			final MenuItem playSoundItem = new MenuItem("Play sound (*)");
			playSoundItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(isPlaySound) {
						playSoundItem.setLabel("Play sound");
						isPlaySound = false;
					} else {
						playSoundItem.setLabel("Play sound (*)");
						isPlaySound = true;
					}
				}
			});
			popup.add(playSoundItem);
			
			// Create menu on/off play sound
			final MenuItem randomItem = new MenuItem("Random");
			randomItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(isRandom) {
						playSoundItem.setLabel("Random");
						isRandom = false;
						FileDao.saveProperties(Constant.RANDOM, isRandom + "");
					} else {
						playSoundItem.setLabel("Random (*)");
						isRandom = true;
						FileDao.saveProperties(Constant.RANDOM, isRandom + "");
					}
				}
			});
			popup.add(randomItem);
			
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
						wordForm.setTitle(getWaitTime() + " minutes");
						FileDao.saveProperties(Constant.TIME_INTERVAL, time+"");
					}
				});
				subTime.add(menuItem);
			}
			
			popup.add(subPopup);
			popup.add(subTime);
			trayIcon = new TrayIcon(image, "ことば", popup);
			trayIcon.setImageAutoSize(true);
		} else {
			System.out.println("system tray not supported");
		}
		
		try {
			if (tray.getTrayIcons() == null || tray.getTrayIcons().length <= 0) {
				tray.add(trayIcon);
			}
		} catch (AWTException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		addWindowStateListener(new WindowStateListener() {
			public void windowStateChanged(WindowEvent e) {
				if (e.getNewState() == ICONIFIED) {
					try {
//						tray.add(trayIcon);
						setVisible(false);
						System.out.println("added to SystemTray");
					} catch (Exception ex) {
						System.out.println("unable to add to tray");
					}
				}
				if (e.getNewState() == 7) {
					try {
//						tray.add(trayIcon);
						setVisible(false);
						System.out.println("added to SystemTray");
					} catch (Exception ex) {
						System.out.println("unable to add to system tray");
					}
				}
				if (e.getNewState() == MAXIMIZED_BOTH) {
//					tray.remove(trayIcon);
					setVisible(true);
					System.out.println("Tray icon removed");
				}
				if (e.getNewState() == NORMAL) {
//					tray.remove(trayIcon);
					setVisible(true);
					System.out.println("Tray icon removed");
				}
			}
		});
		
		setIconImage(Toolkit.getDefaultToolkit().getImage("icon.png"));
	}
	
	public void showText() {
		if(App.displayText.equals(App.EN)) {
			displayText = lstWord.get(getCurrentIndex()).getEnglish();
		} else if(App.displayText.equals(App.KANJI)) {
			displayText = lstWord.get(getCurrentIndex()).getKanji();
		} else {
			displayText = lstWord.get(getCurrentIndex()).getHira();
		}

		textAll = "";
		textAll += lstWord.get(getCurrentIndex()).getKanji() + "\n";
		textAll += lstWord.get(getCurrentIndex()).getHira() + "\n";
		textAll += lstWord.get(getCurrentIndex()).getEnglish() ;

		wordForm.setHiraText(displayText);
		wordForm.setTextAll(textAll);
		wordForm.setVisible(true);
		wordForm.setAlwaysOnTop(true); 
		wordForm.setAlwaysOnTop(false);
		wordForm.setTitle(getWaitTime() + " minutes");
		wordForm.setInfo(getRealIndex() + 1 + "/" + lstWord.size() + "[" + currentFileName + "]");

		if(isPlaySound) {
			playSound();
		}
	}
	
	public void playSound() {
		try {
			AudioPlayer.play(lstAudioFileName[getCurrentIndex()].getPath());
			System.out.println("Play sound: " + lstAudioFileName[getCurrentIndex()].getPath());
		} catch(Exception e) {
			System.out.println("Can't play audio");
			e.printStackTrace();
		}
	}
	
	public String addZeroNumber(Integer num){
		return num < 10 ? "0" + num : "" + num;
	}
	
	public void start(String textPath, String audioPath) {
		currentFileName = textPath;
	
		try {
			// Read text
			lstWord = FileDao.readWord(textPath);
			len = lstWord.size();
			
			if (isRandom) {
				randomIndex = randomIndex(len);
			}
			// Read all audio file
			lstAudioFileName = FileDao.getListFileNames(audioPath, "mp3");
			
			// Write information to configuration file
			FileDao.saveProperties(Constant.LAST_TEXT_FILE, textPath);
			FileDao.saveProperties(Constant.LAST_AUDIO_FILE, audioPath);
			
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
			
			currentIndex = -1;
			isRunning = true;
			isPause = false;
			
			thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					while (isRunning) {
						if (isPause) continue;
						
						currentIndex++;
						
						if(currentIndex >= len) {
							currentIndex = -1;
						}
						showText();
						try {
							System.out.print("time: " + waitTime);
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
	private ArrayList<Integer> randomIndex(int len) {
		ArrayList<Integer> result = new ArrayList<Integer>(len);
		
		for(int i = 0; i < len; i++) {
			result.add(i);
		}
		Collections.shuffle(result);
		return result;
	}
	
	private Integer getCurrentIndex() {
		if (randomIndex == null) {
			return currentIndex;
		}
		return randomIndex.get(currentIndex);
	}
	public void resetRandom() {
		randomIndex = null;
	}
	public void createRandom() {
		randomIndex = randomIndex(len);
	}
	public Integer getRealIndex() {
		return currentIndex;
	}
}
