package app;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import tablemodel.LessonTableModel;
import bean.Lesson;
import dao.SQLLiteProvider;


public class TestApp {
	public static void main(String[] args) throws Exception {
		JFrame frame = new JFrame("SimpleTableDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        String[] columnNames = new String[]{"col1xxx", "col2xxx", "col3xxx"};
       
        
        SQLLiteProvider p = new SQLLiteProvider();
        String sql = "select l.*, c.num as word_count from lesson l join (SELECT count(*) as num, l.id FROM lesson l join kotoba k on l.id = k.lesson_id group by l.id) c on l.id = c.id";
		List<Lesson> lst = p.select(Lesson.class, sql);
		
		LessonTableModel lessonModel = new LessonTableModel(lst);
		
		JTable table = new JTable(lessonModel);
		table.setPreferredScrollableViewportSize(new Dimension(500, 170));
        table.setFillsViewportHeight(true);
        
        JScrollPane scrollPane = new JScrollPane(table);
        frame.setContentPane(scrollPane);
        //Display the window.
        frame.pack();
        frame.setVisible(true);
        
        
//        SQLLiteProvider p = new SQLLiteProvider();
//		List<Kotoba> lst = p.select(Kotoba.class, "SELECT * FROM kotoba");
//		
//		KotobaTableModel lessonModel = new KotobaTableModel(lst);
//		
//		JTable table = new JTable(lessonModel);
//		table.setPreferredScrollableViewportSize(new Dimension(500, 170));
//        table.setFillsViewportHeight(true);
//        table.getColumn("Audio").setCellRenderer(new AudioRenderer());
//        table.getColumn("Audio").setCellEditor(new AudioEditor(table));
//        table.setRowHeight(36);
//        JScrollPane scrollPane = new JScrollPane(table);
//        frame.setContentPane(scrollPane);
//        //Display the window.
//        frame.pack();
//        frame.setVisible(true);
	}
}
