package app;

import java.io.File;
import java.util.Properties;

import javax.swing.DefaultListModel;

import bean.JListItem;
import dao.FileDao;

public class App {
	public static String textFilePath = "data" + File.separator + "text" + File.separator;
	public static String audioFilePath = "data" + File.separator + "audio" + File.separator;
	public static WordForm wordForm;
	public static MainLayout gui;
	public static String displayText = "HIRA";
	public static final String HIRA = "HIRA";
	public static final String KANJI = "KANJI";
	public static final String EN = "EN";
	public static void main(String[] args) throws Exception {
		
		// Get list *.txt file's name
		final JListItem fileNames[] = FileDao.getListFileNames(textFilePath, "txt");
		DefaultListModel<JListItem> model = new DefaultListModel<JListItem>();
		
		for (JListItem name:fileNames) {
			model.addElement(name);
		}
		
//		gui.setWaitTime(time);
		gui = new MainLayout();
		gui.setListData(model);
		gui.display();
		try {
			Properties props = FileDao.readProperties();
			long timeInterval = Long.parseLong(props.getProperty(Constant.TIME_INTERVAL, Constant.WAIT_TIME + ""));
			MainLayout.setWaitTime(timeInterval);
			gui.isRandom = Boolean.valueOf(props.getProperty(Constant.RANDOM, "false"));
			gui.start(props.getProperty(Constant.LAST_TEXT_FILE), props.getProperty(Constant.LAST_AUDIO_FILE));
		} catch(Exception e) {
			System.out.println("Can not load last information.");
			e.printStackTrace();
		}
//		gui.minimize();
	}

}
