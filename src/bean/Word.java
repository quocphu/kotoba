package bean;

public class Word {
	String hira;
	String kanji;
	String english;
	
	public Word(){};
	public Word(String hira, String kanji, String english){
		this.hira = hira;
		this.kanji = kanji;
		this.english = english;
	};
	public String getHira() {
		return hira;
	}
	public void setHira(String hira) {
		this.hira = hira;
	}
	public String getKanji() {
		return kanji;
	}
	public void setKanji(String kanji) {
		this.kanji = kanji;
	}
	public String getEnglish() {
		return english;
	}
	public void setEnglish(String english) {
		this.english = english;
	}
	
}
