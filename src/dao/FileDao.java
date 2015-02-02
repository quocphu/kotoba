package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import bean.JListItem;
import bean.Word;


public class FileDao {
	public static ArrayList<String> readFile(String path) throws IOException {
		ArrayList<String> data = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(path));
	    try {
	        String line = br.readLine();

	        while (line != null) {
	        	data.add(line);
	            line = br.readLine();
	        }
	    } finally {
	        br.close();
	    }
		
		return data;
	}
	
	public static ArrayList<Word> readWord(String path) throws IOException {
		ArrayList<Word> lstWords = new ArrayList<Word>();
		ArrayList<String> fileData = readFile(path);
		String line = "";
		String kanji, hira, mean;
		int index1, index2;

		for (int i = 0; i < fileData.size() - 1; i++) {
			// Don't read 4 first line
			if(i < 4){continue;}
			line = fileData.get(i);
			index1 = line.indexOf("[");
			index2 = line.lastIndexOf("]");
			kanji = line.substring(0, index1 - 1).trim();
			hira = line.substring(index1, index2).replace("[", "").trim();
			mean = line.substring(index2, line.length()).replace("]", "").replace("/", "").trim();
			Word word = new Word(hira, kanji, mean);

			lstWords.add(word);
		}
		return lstWords;
	}
//	public static String[] getListFileNames(String path) {
//		File f = new File(path);
//		File[] listfiles = f.listFiles();
//		ArrayList<String> lstFileName = new ArrayList<String>();
//		for (int i = 0; i < listfiles.length; i++) {
//			
//			lstFileName.add(listfiles[i].getAbsolutePath());
//		}
//
//		return lstFileName.toArray(new String[lstFileName.size()]);
//	}
	public static JListItem[] getListFileNames(String path) {
		File f = new File(path);
		File[] listfiles = f.listFiles();
		ArrayList<JListItem> lstFileName = new ArrayList<JListItem>();
		for (int i = 0; i < listfiles.length; i++) {
			JListItem item = new JListItem();
			item.setName(listfiles[i].getName() );
			item.setPath(listfiles[i].getAbsolutePath());
			lstFileName.add(item);
		}

		return lstFileName.toArray(new JListItem[lstFileName.size()]);
	}
}
