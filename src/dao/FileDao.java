package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import bean.JListItem;
import bean.Kotoba;
import bean.Word;

import com.opencsv.CSVReader;
import common.Constant;

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
			if (i < 4) {
				continue;
			}
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

	// public static String[] getListFileNames(String path) {
	// File f = new File(path);
	// File[] listfiles = f.listFiles();
	// ArrayList<String> lstFileName = new ArrayList<String>();
	// for (int i = 0; i < listfiles.length; i++) {
	//
	// lstFileName.add(listfiles[i].getAbsolutePath());
	// }
	//
	// return lstFileName.toArray(new String[lstFileName.size()]);
	// }
	public static JListItem[] getListFileNames(String path, String extName) {
		File f = new File(path);

		final String extendName = extName;
		FilenameFilter filter = new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				if (name.endsWith(extendName)) {
					return true;
				}

				return false;
			}
		};

		File[] listfiles = f.listFiles(filter);

		ArrayList<JListItem> lstFileName = new ArrayList<JListItem>();
		for (int i = 0; i < listfiles.length; i++) {
			JListItem item = new JListItem();
			item.setName(listfiles[i].getName());
			item.setPath(listfiles[i].getAbsolutePath());
			lstFileName.add(item);
		}

		return lstFileName.toArray(new JListItem[lstFileName.size()]);
	}

	public static Properties readProperties() {
		Properties props = new Properties();
		try {
			File configFile = new File("config.properties");

			FileReader reader = new FileReader(configFile);

			// Load the properties file:
			props.load(reader);
		} catch (Exception e) {
			System.out.println("Can not load " + Constant.CONFIG_FILE);
			e.printStackTrace();
		}
		return props;
	}

	public static int saveProperties(String key, String value) {
		try {
			Properties props = readProperties();
			props.setProperty(key, value);

			File configFile = new File(Constant.CONFIG_FILE);
			FileWriter writer = new FileWriter(configFile);

			props.store(writer, "Save properties: " + key + "=" + value);

			writer.flush();
			writer.close();

		} catch (Exception e) {
			System.out.println("Can not save properties.");
			e.printStackTrace();
		}
		return -1;
	}

	public static List<String[]> readCsvFile(String path, String cvsSplitBy) {
		BufferedReader br = null;
		List<String[]> data = null;
		try {
			data = new ArrayList<String[]>();
			CSVReader reader = new CSVReader(new FileReader(path));
			String[] nextLine;
			while ((nextLine = reader.readNext()) != null) {
				data.add(nextLine);
			}
			reader.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return data;
	}

	public static List<Kotoba> readKotobaFromCSV(String path, String cvsSplitBy, String[] header) {
		List<Kotoba> lstKotoba = new ArrayList<Kotoba>();
		List<String[]> fileData = readCsvFile(path, cvsSplitBy);
		if (fileData != null) {
			Class<Kotoba> cl = Kotoba.class;
			for (int i = 0; i < fileData.size(); i++) {
				Kotoba t = new Kotoba();
				for (int j = 0; j < header.length; j++) {
					try {

						Field f = cl.getDeclaredField(header[j]);
						f.setAccessible(true);
						if (f.getType() == Date.class) {
							f.set(t, fileData.get(i)[j]);
						}
						if (f.getType() == Integer.class) {
							f.set(t, fileData.get(i)[j]);
						} else {
							f.set(t, fileData.get(i)[j]);
						}

					} catch (Exception e) {
						System.out.println("Error at: " + i + "," + j);
						e.printStackTrace();
					}
				}
//				t.normalizeAudioPath();
				lstKotoba.add(t);
			}
		}
		return lstKotoba;
	}
}
