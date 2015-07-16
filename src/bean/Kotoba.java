package bean;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Kotoba {
	private int id;
	private int lessonId;
	private String japanese;
	private String kanji;
	private String mean;
	private String audio;
	private Integer priority;
	private Date createDate;
	private Date updateDate;
	
	private String lessonName;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLessonId() {
		return lessonId;
	}
	public void setLessonId(int lessonId) {
		this.lessonId = lessonId;
	}
	public String getJapanese() {
		return japanese;
	}
	public void setJapanese(String japanse) {
		this.japanese = japanse;
	}
	public String getKanji() {
		return kanji;
	}
	public void setKanji(String kanji) {
		this.kanji = kanji;
	}
	public String getMean() {
		return mean;
	}
	public void setMean(String mean) {
		this.mean = mean;
	}
	public String getAudio() {
		return audio;
	}
	public void setAudio(String audio) {
		this.audio = audio;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getLessonName() {
		return lessonName;
	}
	public void setLessonName(String lessonName) {
		this.lessonName = lessonName;
	}
	public Kotoba(){
		
	}

	public Kotoba(String[] data) {
		id = Integer.parseInt(data[0]);
		lessonId = Integer.parseInt(data[1]);
		lessonName = data[2];
		japanese = data[3];
		kanji = data[4];
		mean = data[5];
		audio = data[6];
		priority =  Integer.parseInt(data[7]);
		
	}
}
