package tablemodel;

import java.util.HashMap;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import bean.Kotoba;

public class KotobaTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;

	private String[] columnNames;
	private List<Kotoba> data;

	private HashMap<Integer, Kotoba> deletedItems;
	private HashMap<Integer, Kotoba> editedItems;
	
	public KotobaTableModel(List<Kotoba> data) {
		columnNames = new String[] { "Lesson", "Word", "Kanji", "Mean", "Audio" };
		this.data = data;
		this.deletedItems = new HashMap<Integer, Kotoba>();
		this.editedItems = new HashMap<Integer, Kotoba>();
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
		Kotoba ktb = data.get(rowIndex);
		Object rs = null;
		switch (columnIndex) {
			case 0:
				rs = ktb.getLessonId();
//				rs = ktb.getLessonName();
				break;
			case 1:
				rs = ktb.getJapanese();
				break;
			case 2:
				rs = ktb.getKanji();
				break;
			case 3:
				rs = ktb.getMean();
				break;
			case 4:
//				rs = Common.getFileNameFromPath(ktb.getAudio());
				rs = ktb.getAudio();
				break;
			case 5:
				rs = ktb.getLessonName();
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

//	public Class<?> getColumnClass(int c) {
//		return getValueAt(0, c).getClass();
//	}

	public boolean isCellEditable(int row, int col) {
//		if (col > 0) {
//			return false;
//		} else {
//			return true;
//		}
		return false;
	}
	 public void setValueAt(Object value, int row, int col) {
	        
		 Kotoba ktb = data.get(row);
		 if (ktb.getId() > 0) {
			 this.editedItems.put(ktb.getId(), ktb);
		 }
		 switch (col) {
		 case 0:
				ktb.setLessonId(Integer.parseInt(String.valueOf(value)));
				break;
			case 1:
				ktb.setJapanese((String)value);
				break;
			case 2:
				ktb.setKanji((String)value);
				break;
			case 3:
				ktb.setMean((String)value);
				break;
			case 4:
				ktb.setAudio((String)value);
				break;
			case 5:
				ktb.setLessonName((String)value);
				break;
			default:
				break;
		}
         fireTableCellUpdated(row, col);
     }
	 public Kotoba getDataAt(int row){
		 return this.data.get(row);
	 }
	 public void addData(Kotoba ktb){
		  this.data.add(ktb);
		  fireTableDataChanged();
	 }
	 public void insertData(Kotoba ktb, int row){
		  this.data.add(row, ktb);
		  fireTableDataChanged();
	 }
	 public void deleteDataAt(int row) {
		 if (this.data.get(row).getId() > 0) {
			 this.deletedItems.put(this.data.get(row).getId(), this.data.get(row));
			 this.editedItems.remove(this.data.get(row).getId());
		 }
		 this.data.remove(row);
		 fireTableDataChanged();
	 }
	
	 public HashMap<Integer, Kotoba> getDeletedItems(){
		 return this.deletedItems;
	 }
	 
	 public HashMap<Integer, Kotoba> getEditedItems(){
		 return this.editedItems;
	 }
	 public List<Kotoba> getData() {
		 return this.data;
	 }
}
