package tablemodel;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.EventObject;

import javax.swing.AbstractAction;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableCellEditor;

import audio.AudioPlayer;
import common.Common;

public class AudioEditor extends AudioPanel implements TableCellEditor {
	private static final long serialVersionUID = 1L;
	protected transient ChangeEvent changeEvent;
	private final JTable table;

	private class EditAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		private String lastDirectory = "";
		private final JTable table;
	    public EditAction(JTable table) {
	        super("");
	        this.table = table;
	    }
	    @Override public void actionPerformed(ActionEvent e) {
	        int row = table.convertRowIndexToModel(table.getEditingRow());
	        int column = table.convertColumnIndexToModel(table.getEditingColumn());
System.out.println("cell: " + row +","+column);
	        JFileChooser fc = new JFileChooser();
	        if (lastDirectory != "") {
	        	fc = new JFileChooser(lastDirectory);
	        }
	        fc.addChoosableFileFilter(new FileFilter() {
				@Override
				public String getDescription() {
					return null;
				}
				
				@Override
				public boolean accept(File f) {
					int indexOfDot = f.getName().lastIndexOf(".");
					String extendFileName = f.getName().substring(indexOfDot + 1);
					if(extendFileName.toLowerCase().equals("mp3")) {
						return true;
					}
					return false;
				}
			});
	        
	        if (fc.showOpenDialog(table) == JFileChooser.APPROVE_OPTION) {
	        	File f = fc.getSelectedFile();
	        	table.setValueAt(f.getPath(), row, column);
	        	btnPlay.setText(f.getName());
	        	path = f.getPath();
	        	lastDirectory = f.getAbsolutePath();
	        }
	    }
	}
	
	private class PlayAudioAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		
		private final JTable table;
	    public PlayAudioAction(JTable table) {
	        super("");
	        this.table = table;
	    }
	    @Override
	    public void actionPerformed(ActionEvent e) {
	        int row = table.convertRowIndexToModel(table.getEditingRow());
	        int column = table.convertColumnIndexToModel(table.getEditingColumn());
	        System.out.println("cell: " + row +","+column);
	        Object audioPath = table.getModel().getValueAt(row, column);
	        try {
	        	AudioPlayer.play((String)audioPath);
	        } catch(Exception ex) {
	        	System.out.println("Cannot play audio: " + audioPath);
	        	ex.printStackTrace();
	        }
	    }
	}
	private class EditingStopHandler extends MouseAdapter implements ActionListener {
		@Override
		public void mousePressed(MouseEvent e) {
			Object o = e.getSource();
			if (o instanceof TableCellEditor) {
				actionPerformed(null);
			} else if (o instanceof JButton) {
				// DEBUG: view button click -> control key down + edit
				// button(same cell) press -> remain selection color
				ButtonModel m = ((JButton) e.getComponent()).getModel();
				if (m.isPressed() && table.isRowSelected(table.getEditingRow()) && e.isControlDown()) {
					setBackground(table.getBackground());
				}
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					fireEditingStopped();
				}
			});
		}
	}

	public AudioEditor(final JTable table) {
		super("AudioEditor");
		this.table = table;
		// buttons.get(0).setAction(new ViewAction(table));
		// buttons.get(1).setAction(new EditAction(table));

		btnBrowser.setAction(new EditAction(table));
		btnPlay.setAction(new PlayAudioAction(table));
		EditingStopHandler handler = new EditingStopHandler();
		// for (JButton b: buttons) {
		// b.addMouseListener(handler);
		// b.addActionListener(handler);
		// }
		btnBrowser.addMouseListener(handler);
		addMouseListener(handler);
	}

	@Override
	public Object getCellEditorValue() {

		return super.getPath();
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		this.setBackground(table.getSelectionBackground());
		this.btnPlay.setText(Common.getFileNameFromPath((String)value));
		btnBrowser.setText(AudioPanel.BTN_BROWSER_TEXT);
		return this;
	}

	@Override
	public boolean isCellEditable(EventObject e) {
		return true;
	}

	@Override
	public boolean shouldSelectCell(EventObject anEvent) {
		return true;
	}

	@Override
	public boolean stopCellEditing() {
		fireEditingStopped();
		return true;
	}

	@Override
	public void cancelCellEditing() {
		fireEditingCanceled();
	}

	@Override
	public void addCellEditorListener(CellEditorListener l) {
		listenerList.add(CellEditorListener.class, l);
	}

	@Override
	public void removeCellEditorListener(CellEditorListener l) {
		listenerList.remove(CellEditorListener.class, l);
	}

	public CellEditorListener[] getCellEditorListeners() {
		return listenerList.getListeners(CellEditorListener.class);
	}

	protected void fireEditingStopped() {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == CellEditorListener.class) {
				// Lazily create the event:
				if (changeEvent == null) {
					changeEvent = new ChangeEvent(this);
				}
				((CellEditorListener) listeners[i + 1]).editingStopped(changeEvent);
			}
		}
	}

	protected void fireEditingCanceled() {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == CellEditorListener.class) {
				// Lazily create the event:
				if (changeEvent == null) {
					changeEvent = new ChangeEvent(this);
				}
				((CellEditorListener) listeners[i + 1]).editingCanceled(changeEvent);
			}
		}
	}
}
