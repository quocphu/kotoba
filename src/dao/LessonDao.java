package dao;

import java.util.HashMap;
import java.util.List;

import bean.Kotoba;
import bean.Lesson;

public class LessonDao extends SQLLiteProvider {
	public int insert(Lesson lesson) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("title", lesson.getTitle());
		params.put("priority", lesson.getPriority());
		String sql = "insert into lesson(title, priority, create_date, update_date) values({title}, {priority}, current_timestamp, current_timestamp)";
		
		return super.insert(params, sql);
	}
	public List<Lesson> getAll() {
		String sql = "select * from Lesson";
		List<Lesson> lstLesson = this.select(Lesson.class, sql);
		return lstLesson;
	}
	public List<Lesson> getAllHaveWordCount() {
		String sql = "select l.*, c.num as word_count from lesson l join (SELECT count(k.id) as num, l.id FROM lesson l left join kotoba k on l.id = k.lesson_id group by l.id) c on l.id = c.id";
		
		List<Lesson> lstLesson = this.select(Lesson.class, sql);
		return lstLesson;
	}
	public int update(Lesson lesson) {
		String sql ="";
		sql += "update lesson ";
		sql += "set title={title}, update_date = current_timestamp";
		sql += " where id={id}";
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", lesson.getId());
		params.put("title", lesson.getTitle());
		
		return execute(sql, params);
		
	}
	public void delete(Integer id) {
		delete("lesson", id);
	}
}
