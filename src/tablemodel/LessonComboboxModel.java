package tablemodel;

import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import bean.Lesson;

@SuppressWarnings("rawtypes")
public class LessonComboboxModel extends AbstractListModel implements ComboBoxModel {
	private static final long serialVersionUID = 1L;
	
	private List<Lesson> data;
	private Lesson selection;
	
	public LessonComboboxModel(List<Lesson> data) {
		this.data = data;
		this.selection = null;
	}
	@Override
	public int getSize() {
		return data.size();
	}

	@Override
	public Object getElementAt(int index) {
		return data.get(index);
	}

	@Override
	public void setSelectedItem(Object anItem) {
		selection = (Lesson) anItem;
		fireContentsChanged(this, -1, -1);
	}

	@Override
	public Object getSelectedItem() {
		return selection;
	}
	
}
