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
		String sql = "select l.*, c.num as word_count from lesson l join (SELECT count(*) as num, l.id FROM lesson l join kotoba k on l.id = k.lesson_id group by l.id) c on l.id = c.id";
		
		List<Lesson> lstLesson = this.select(Lesson.class, sql);
		return lstLesson;
	}
	public int update(Kotoba kotoba) {
		return 0;
	}
	
}
