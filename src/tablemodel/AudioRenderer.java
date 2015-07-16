package tablemodel;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

import common.Common;

public class AudioRenderer extends JLabel implements TableCellRenderer {

	private static final long serialVersionUID = 1L;
	public AudioRenderer() {
		super("");
		this.setOpaque(true);
	}
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if (isSelected) {
			setForeground(table.getSelectionForeground());
			setBackground(table.getSelectionBackground());
		} else {
			setForeground(table.getForeground());
			setBackground(UIManager.getColor("Button.background"));
		}
		setBackground(isSelected?table.getSelectionBackground():table.getBackground());
		this.setText(Common.getFileNameFromPath((String)value));
	    return this;
	}
}
