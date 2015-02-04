package app;

import java.io.IOException;

import javax.swing.DefaultListModel;

import bean.JListItem;
import dao.FileDao;

public class App {
	public static String filePath = "data";
	public static WordForm wordForm;
	public static MainLayout gui;
	public static String displayText = "HIRA";
	public static final String HIRA = "HIRA";
	public static final String KANJI = "KANJI";
	public static final String EN = "EN";
	public static void main(String[] args) throws IOException {
		
		final JListItem fileNames[] = FileDao.getListFileNames(filePath);
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
