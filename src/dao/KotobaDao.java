package dao;

import java.util.HashMap;
import java.util.List;

import bean.Kotoba;

public class KotobaDao extends SQLLiteProvider {
	public int update(Kotoba kotoba) {
		String sql ="";
		sql += "update kotoba ";
		sql += "set lesson_id={lesson_id},";
		sql += "japanese = {japanese},";
		sql += "kanji= {kanji},";
		sql += "mean={mean},";
		sql += "audio={audio},";
		sql += "update_date=current_timestamp";
		sql += " where id={id}";
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", kotoba.getLessonId());
		params.put("lesson_id", kotoba.getLessonId());
		params.put("japanese", kotoba.getJapanese());
		params.put("kanji", kotoba.getKanji());
		params.put("mean", kotoba.getMean());
		params.put("audio", kotoba.getAudio());
		
		return execute(sql, params);
		
	}
	public void delete(Integer kotobaId) {
		delete("kotoba", kotobaId);
	}
	public int insert(Kotoba kotoba) {
		String sql ="insert into kotoba(lesson_id, japanese, kanji, mean, audio, create_date, update_date) values({lesson_id}, {japanese}, {kanji}, {mean}, {audio}, current_date, current_date );";
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("lesson_id", kotoba.getLessonId());
		params.put("japanese", kotoba.getJapanese());
		params.put("kanji", kotoba.getKanji());
		params.put("mean", kotoba.getMean());
		params.put("audio", kotoba.getAudio());
		
		return execute(sql, params);
	}
	public List<Kotoba> getByLessonId(Integer lessonId) {
		String sql = "select * from kotoba where lesson_id=" + lessonId;
		return select(Kotoba.class, sql);
		
	}
}
