package app;

import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JTable;

public class GridView<T> extends JPanel {
	private static final long serialVersionUID = 1L;
	
	Vector<String> columnNames;
	Vector data;
	JTable tableView;
	
	public GridView(Vector<String> columnNames, Vector data) {
		this.data = data;
		this.columnNames = columnNames;
		tableView = new JTable(this.data, this.columnNames);
	}
	
	public T getRow(int row) {
		T t = (T) data.get(row);
		return t;
	}
	
	public T getSelectedRow() {
		int row = tableView.getSelectedRow();
		T t= (T) data.get(row);
		return t;
	}
	
}
