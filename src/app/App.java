package app;

import java.io.File;

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
//		gui.minimize();
	}

}
