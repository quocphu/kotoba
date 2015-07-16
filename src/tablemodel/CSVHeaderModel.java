package tablemodel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;

public class CSVHeaderModel<T> extends DefaultListModel<T> {
	private static final long serialVersionUID = 1L;
	private List<T> data;
	@Override
	public int getSize() {
		return data.size();
	}

	@Override
	public T getElementAt(int index) {
		return data.get(index);
	}
	public void setData(List<T> data) {
		this.data =data;
	}
	public CSVHeaderModel(){
		super();
		data =  new ArrayList<T>();
	}
	
	public CSVHeaderModel(List<T> data){
		super();
		this.data = data;
	}
	
	public void addElement(T item) {
		int index = this.data.size();
		this.data.add(item);
		fireIntervalAdded(this, index, index);
	}
	
	public void removeElement(int index) {
		this.data.remove(index);
		fireIntervalRemoved(this, index, index);
	}
	
	public void setElement( int index, T item) {
		this.data.set(index, item);
		fireIntervalRemoved(this, index, index);
	}

	public List<T> getData() {
		return data;
	}
	
}
