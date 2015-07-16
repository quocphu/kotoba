package tablemodel;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import bean.Lesson;

public class LessonTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private String[] columnNames;
	private List<Lesson> data;

	public LessonTableModel( List<Lesson> data) {
		this.columnNames = new String[]{"Title", "Word count", "Finished"};
		this.data = data;
	}
	
	public LessonTableModel(String[] columnNames, List<Lesson> data) {
		this.columnNames = columnNames;
		this.data = data;
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Lesson lesson = data.get(rowIndex);
		Object rs = null;
		switch (columnIndex) {
		case 0:
			rs = lesson.getTitle();
			break;
		case 1:
			rs = lesson.getWordCount();
			break;
		case 2:
			rs = lesson.isFinish();
			break;
		default:
			rs = null;
			break;
		}
		return rs;
	}
	@Override
	 public String getColumnName(int column) {
         return this.columnNames[column];
     }
	
	 public Class<?> getColumnClass(int c) {
         return getValueAt(0, c).getClass();
     }
	 public boolean isCellEditable(int row, int col) {
//         if (col > 0) {
//             return false;
//         } else {
//             return true;
//         }
		 return false;
     }
	 public void setValueAt(Object value, int row, int col) {
        
		 Lesson lesson = data.get(row);
        
		 switch (col) {
			case 0:
				lesson.setTitle((String)value);
				break;
			default:
				break;
		}
         fireTableCellUpdated(row, col);
     }

	public Lesson getDataAt(int row){
		return this.data.get(row);
	}
}
