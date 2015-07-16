package dao;

import java.util.List;

import bean.Lesson;

public class SqlTest {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		LessonDao dao = new LessonDao();
		Lesson ls = new Lesson();
		ls.setTitle("test lesson 1");
		ls.setPriority(1);
		
		dao.insert(ls);
		
		
		SQLLiteProvider p = new SQLLiteProvider();
		List<Lesson> lst = p.select(Lesson.class, "SELECT * FROM lesson");
		
		for(Lesson al:lst) {
			System.out.println(al.getTitle());
		}
	}

}
