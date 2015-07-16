package common;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.filechooser.FileFilter;

import bean.JListItem;
import bean.Kotoba;
import bean.Word;

public class Common {
	public static String getFileNameFromPath(String path) {
		if(path == null || path == "") {
			return path;
		}
		
		int indexOfSlash = path.lastIndexOf(File.separator);
		if(indexOfSlash < 0) {
			return path;
		}
		
		String fileName = "";
		fileName = path.substring(indexOfSlash + 1);
		return fileName;
	}
	public Date parse(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date d = null;
		try {
			d = sdf.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return d;
	}
	public static FileFilter createFileFilter(final String ext) {
		return new FileFilter() {
			@Override
			public boolean accept(File file) {
				int indexOfDot = file.getName().lastIndexOf(".");
				String extendFileName = file.getName().substring(indexOfDot + 1);
				if (file.isFile() && !extendFileName.toLowerCase().equals(ext)) {
					return false;
				}
				return true;
			}

			@Override
			public String getDescription() {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}
	
	public static List<Word> convertWordFromKotoba(List<Kotoba> lstKotoba) {
		List<Word> lstWord = new ArrayList<Word>();
		for(Kotoba ktb:lstKotoba) {
			Word w =  new Word(ktb.getJapanese(), ktb.getKanji(), ktb.getMean());
			
			lstWord.add(w);
		}
		return lstWord;
	}
	
	public static JListItem[] convertJListItemFromKotoba(List<Kotoba> lstKotoba) {
		JListItem[] itemArray = new JListItem[lstKotoba.size()];
		int i = 0;
		for(Kotoba ktb:lstKotoba) {
			JListItem item = new JListItem();
			item.setName( getFileNameFromPath(ktb.getAudio()));
			item.setPath(ktb.getAudio());
			
			itemArray[i] = item;
			i++;
		}
		return itemArray;
	}
}
