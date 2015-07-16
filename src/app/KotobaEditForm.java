package app;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import common.Common;
import tablemodel.AudioRenderer;
import tablemodel.KotobaTableModel;
import tablemodel.LessonComboboxModel;
import audio.AudioPlayer;
import bean.CSVHeader;
import bean.Kotoba;
import bean.Lesson;
import dao.FileDao;
import dao.KotobaDao;
import dao.LessonDao;
import dao.SQLLiteProvider;

public class KotobaEditForm extends JFrame {
	private static final long serialVersionUID = 1L;
	private Integer AUDIO_COLUMN_INDEX = 4;
	private JTable table;
	private List<Kotoba> lstKotoba;
	private List<Lesson> lstLesson;
	private Integer lessonId;
	private JPanel top, bottom;
	private JScrollPane scrollPane;
	String BTN_SAVE_TEXT = "Save";
	String BTN_RESET_TEXT = "Reset";
	String BTN_RESET_ALL_TEXT = "Reset All";
	String BTN_LOAD_AUDIO_TEXT = "Load Audio";
	String BTN_LOAD_DATA_FROM_FILE = "Load file";
	private JButton btnSave, btnReset, btnResetAll, btnLoadAudio, btnLoadDataFromFile;
	private JButton btnAudioChoose, btnAudioInsert, btnAudioDelete, btnAudioMoveUp, btnAudioMoveDown;
	private JPanel bottomForm, bottomButton, pnAudio;
	private JComboBox<Lesson> cmbLesson;
	private JTextField txtWord, txtKanji, txtMean, txtAudio;

	private LessonDao lessonDao;
	private KotobaDao kotobaDao;

	private JButton btnAudioPlay;

	private void reload() {
		KotobaTableModel kotobaModel = new KotobaTableModel(loadData());
		table.setModel(kotobaModel);
		this.repaint();
	}

	private List<Kotoba> loadData() {
		SQLLiteProvider p = new SQLLiteProvider();
//		this.lstKotoba = p.select(Kotoba.class, "SELECT k.*,l.title as lessonName FROM kotoba k join lesson l on k.lesson_Id = l.id");
		this.lstKotoba = kotobaDao.getByLessonId(lessonId);
		return lstKotoba;
	}

	public KotobaEditForm(Integer lessonId) {
		lessonDao = new LessonDao();
		kotobaDao = new KotobaDao();
		this.lessonId = lessonId;
		this.lstLesson = lessonDao.getAll();
		
		this.setFocusable(true);
		// Load data
		loadData();

		// Create table
		KotobaTableModel kotobaModel = new KotobaTableModel(lstKotoba);
		table = new JTable(kotobaModel);
		table.setPreferredScrollableViewportSize(new Dimension(500, 170));
		table.setFillsViewportHeight(true);
		table.setRowHeight(36);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// DefaultTableCellRenderer audioRender = new
		// DefaultTableCellRenderer();
		// audioRender.setHorizontalAlignment(SwingConstants.RIGHT);
		table.getColumn("Audio").setCellRenderer(new AudioRenderer());
		// Layout
		// Add table to scroll panel
		scrollPane = new JScrollPane(table);
		scrollPane.setSize(400, 200);

		// Add scroll panel to top
		top = new JPanel();
		top.setSize(400, 200);
		top.setLayout(new GridLayout(1, 1));
		top.add(scrollPane);

		bottomForm = new JPanel(new BorderLayout());
		JPanel labelPanel = new JPanel(new GridLayout(5, 1));
		JPanel fieldPanel = new JPanel(new GridLayout(5, 1));

		labelPanel.add(new JLabel("Lesson"));
		labelPanel.add(new JLabel("Word"));
		labelPanel.add(new JLabel("Kanji"));
		labelPanel.add(new JLabel("Mean"));
		labelPanel.add(new JLabel("Audio"));

		cmbLesson = new JComboBox<Lesson>(new LessonComboboxModel(lstLesson));

		txtWord = new JTextField();
		txtKanji = new JTextField();
		txtMean = new JTextField();
		txtAudio = new JTextField();

		pnAudio = new JPanel(new GridLayout());
		pnAudio.add(txtAudio);
		initAudioButton();

		fieldPanel.add(cmbLesson);
		fieldPanel.add(txtWord);
		fieldPanel.add(txtKanji);
		fieldPanel.add(txtMean);
		fieldPanel.add(pnAudio);

		bottomForm.add(labelPanel, BorderLayout.WEST);
		bottomForm.add(fieldPanel, BorderLayout.CENTER);

		bottom = new JPanel();
		bottom.setLayout(new BorderLayout(2, 0));

		// Initialize button
		initBottomButtons();

		bottom.add(bottomForm, BorderLayout.CENTER);
		bottom.add(bottomButton, BorderLayout.SOUTH);

		// Create layout
		BorderLayout layout = new BorderLayout(2, 0);
		layout.setHgap(10);
		layout.setVgap(10);

		this.setLayout(layout);

		this.add(top, BorderLayout.CENTER);
		this.add(bottom, BorderLayout.SOUTH);
		this.setSize(400, 250);

		setTableListener();
		List<Kotoba> ktbFromCSV = FileDao.readKotobaFromCSV("bai1.csv", ",", new String[] { "japanese", "kanji", "mean" });

		// for (Kotoba ktb : ktbFromCSV) {
		// System.out.println("add " + ktb.getJapanese());
		// insertTableRow(ktb);
		// }
		Action actionListener = new AbstractAction() {
			public void actionPerformed(ActionEvent actionEvent) {
				System.out.println("Got an CTRL + S");
				setTextFromFormToTable();
			}
		};

		JPanel content = (JPanel) this.getContentPane();
		KeyStroke strokeSave = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK);
		KeyStroke strokeNewRow = KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK);

		InputMap inputMap = content.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(strokeSave, "SAVE");
		content.getActionMap().put("SAVE", actionListener);
		
		inputMap.put(strokeNewRow, "NEWROW");
		content.getActionMap().put("NEWROW", new AbstractAction(){
			public void actionPerformed(ActionEvent actionEvent) {
				System.out.println("Got an CTRL + N");
				insertTableRow(new Kotoba());
			}
		});
	}

	public static void main(String... args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGUI();
			}
		});
	}

	public static void createAndShowGUI() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		}

		KotobaEditForm frame = new KotobaEditForm(1);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);

		frame.pack();
		frame.setVisible(true);

	}

	private void setFormText(Kotoba kotoba) {
		cmbLesson.setSelectedItem(null);
		for (int i = 0; i < this.lstLesson.size(); i++) {
			if (this.lstLesson.get(i).getId() == kotoba.getLessonId()) {
				cmbLesson.setSelectedItem(lstLesson.get(i));
				break;
			}
		}

		txtAudio.setText(kotoba.getAudio());
		txtKanji.setText(kotoba.getKanji());
		txtMean.setText(kotoba.getMean());
		txtWord.setText(kotoba.getJapanese());
	}

	private void setTableListener() {
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				if (table.getSelectedRow() > -1) {
					KotobaTableModel model = (KotobaTableModel) table.getModel();
					Kotoba ktb = model.getDataAt(table.getSelectedRow());
					setFormText(ktb);
				}
			}
		});
	}

	private void insertTableRow(Kotoba ktb) {
		KotobaTableModel model = (KotobaTableModel) table.getModel();
		model.addData(ktb);
	}

	private void setValueAt(Object value, int row, int col) {
		KotobaTableModel model = (KotobaTableModel) table.getModel();
		model.setValueAt(value, row, col);
	}

	private void initAudioButton() {
		btnAudioPlay = new JButton("Play");
		btnAudioChoose = new JButton("Choose...");
		btnAudioInsert = new JButton("Insert");
		btnAudioDelete = new JButton("Delete");
		btnAudioMoveUp = new JButton("Move up");
		btnAudioMoveDown = new JButton("Move down");
		pnAudio.add(btnAudioChoose);
		pnAudio.add(btnAudioPlay);
		pnAudio.add(btnAudioInsert);
		pnAudio.add(btnAudioDelete);
		pnAudio.add(btnAudioMoveUp);
		pnAudio.add(btnAudioMoveDown);

		btnAudioInsert.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if ((e.getModifiers() & InputEvent.CTRL_MASK) != 0) {
					KotobaTableModel kotobaModel = (KotobaTableModel) table.getModel();

					int selectedRowIndex = table.getSelectedRow();
					Kotoba ktb = new Kotoba();
					if(selectedRowIndex >= 0 && selectedRowIndex <= kotobaModel.getRowCount()) {
						kotobaModel.insertData(ktb, selectedRowIndex);
					} else {
						kotobaModel.addData(ktb);
						table.setRowSelectionInterval(kotobaModel.getRowCount() - 1, kotobaModel.getRowCount() - 1);
					}
				} else {
					KotobaTableModel kotobaModel = (KotobaTableModel) table.getModel();

					int selectedRowIndex = table.getSelectedRow();
					Kotoba ktb = new Kotoba();
					kotobaModel.addData(ktb);
					String audio = "";
					for (int i = kotobaModel.getRowCount() - 1; i > selectedRowIndex; i--) {
						audio = (String) kotobaModel.getValueAt(i - 1, AUDIO_COLUMN_INDEX);
						kotobaModel.setValueAt(audio, i, AUDIO_COLUMN_INDEX);
					}
					kotobaModel.setValueAt(null, selectedRowIndex, AUDIO_COLUMN_INDEX);
					table.setRowSelectionInterval(selectedRowIndex, selectedRowIndex);
				}
			}
		});

		btnAudioDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				KotobaTableModel kotobaModel = (KotobaTableModel) table.getModel();
				int selectedRowIndex = table.getSelectedRow();

				if ((e.getModifiers() & InputEvent.CTRL_MASK) != 0) {
					kotobaModel.deleteDataAt(selectedRowIndex);
					if (selectedRowIndex >= kotobaModel.getColumnCount()) {
						table.setRowSelectionInterval(kotobaModel.getColumnCount() - 1, kotobaModel.getColumnCount() - 1);
					} else if (selectedRowIndex == 0) {
						table.setRowSelectionInterval(selectedRowIndex, selectedRowIndex);
					} else {
						table.setRowSelectionInterval(selectedRowIndex - 1, selectedRowIndex - 1);
					}
				} else {
					String audio = "";
					for (int i = selectedRowIndex; i < kotobaModel.getRowCount() - 1; i++) {
						audio = (String) kotobaModel.getValueAt(i + 1, AUDIO_COLUMN_INDEX);
						kotobaModel.setValueAt(audio, i, AUDIO_COLUMN_INDEX);
					}
					table.setRowSelectionInterval(selectedRowIndex, selectedRowIndex);
				}
			}
		});

		// Move up one row or audio only
		btnAudioMoveUp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				KotobaTableModel kotobaModel = (KotobaTableModel) table.getModel();
				int selectedRowIndex = table.getSelectedRow();
				if (selectedRowIndex == 0) {
					return;
				}
				Object audio = kotobaModel.getValueAt(selectedRowIndex, AUDIO_COLUMN_INDEX);
				kotobaModel.setValueAt(kotobaModel.getValueAt(selectedRowIndex - 1, AUDIO_COLUMN_INDEX), selectedRowIndex, AUDIO_COLUMN_INDEX);
				kotobaModel.setValueAt(audio, selectedRowIndex - 1, AUDIO_COLUMN_INDEX);
				table.setRowSelectionInterval(selectedRowIndex - 1, selectedRowIndex - 1);
			}
		});

		btnAudioMoveDown.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				KotobaTableModel kotobaModel = (KotobaTableModel) table.getModel();
				int selectedRowIndex = table.getSelectedRow();
				if (selectedRowIndex >= kotobaModel.getRowCount() - 1) {
					return;
				}
				Object audio = kotobaModel.getValueAt(selectedRowIndex, AUDIO_COLUMN_INDEX);
				kotobaModel.setValueAt(kotobaModel.getValueAt(selectedRowIndex + 1, AUDIO_COLUMN_INDEX), selectedRowIndex, AUDIO_COLUMN_INDEX);
				kotobaModel.setValueAt(audio, selectedRowIndex + 1, AUDIO_COLUMN_INDEX);
				table.setRowSelectionInterval(selectedRowIndex + 1, selectedRowIndex + 1);
			}
		});
		
		btnAudioChoose.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fc.setCurrentDirectory(new File("/Users/letoan/Documents/nihongo/soft/"));
				if (fc.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
					File f = fc.getSelectedFile();
					txtAudio.setText(f.getPath());
				}
			}
		});
		
		btnAudioPlay.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String path = txtAudio.getText();
				try {
					AudioPlayer.play(path);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}

	private void initBottomButtons() {
		btnSave = new JButton(BTN_SAVE_TEXT);
		btnReset = new JButton(BTN_RESET_TEXT);
		btnResetAll = new JButton(BTN_RESET_ALL_TEXT);
		btnLoadAudio = new JButton(BTN_LOAD_AUDIO_TEXT);
		btnLoadDataFromFile = new JButton(BTN_LOAD_DATA_FROM_FILE);

		bottomButton = new JPanel();
		bottomButton.setLayout(new GridLayout(0, 5));

		bottomButton.add(btnReset);
		bottomButton.add(btnSave);
		bottomButton.add(btnResetAll);
		bottomButton.add(btnLoadAudio);
		bottomButton.add(btnLoadDataFromFile);

		btnReset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				KotobaTableModel kotobaModel = (KotobaTableModel) table.getModel();

				table.setModel(kotobaModel);
				kotobaModel.fireTableDataChanged();
				reload();
				table.repaint();
			}
		});

		btnLoadAudio.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fc.setCurrentDirectory(new File("/Users/letoan/Documents/nihongo/soft/"));
				if (fc.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
					File f = fc.getSelectedFile();
					File[] files = f.listFiles(new FileFilter() {
						@Override
						public boolean accept(File file) {
							int indexOfDot = file.getName().lastIndexOf(".");
							String extendFileName = file.getName().substring(indexOfDot + 1);
							if (extendFileName.toLowerCase().equals("mp3")) {
								return true;
							}
							return false;
						}
					});

					if (files.length == 0) {
						JOptionPane.showMessageDialog(null, "Have no mp3 file!");
						return;
					}
					for (int i = 0; i < files.length; i++) {
						System.out.println(files[i].getPath());
						setValueAt(files[i].getPath(), i, 4);
					}
				}
			}
		});
		
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				KotobaTableModel model = (KotobaTableModel) table.getModel();
				HashMap<Integer, Kotoba> editedItems = model.getEditedItems();
				HashMap<Integer, Kotoba> deletedItems = model.getDeletedItems();
				for(Integer kotobaId : editedItems.keySet()) {
					kotobaDao.update(editedItems.get(kotobaId));
				}
				
				for(Integer kotobaId : deletedItems.keySet()) {
					kotobaDao.delete(kotobaId);
				}
				
				for(Kotoba ktb : model.getData()) {
					if(ktb.getId() <= 0) {
						ktb.setLessonId(lessonId);
						kotobaDao.insert(ktb);
					}
				}
				
				reload();
			}
		});
		
		btnLoadDataFromFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File("/Users/letoan/Documents/nihongo/soft/"));
				fc.setFileFilter(Common.createFileFilter("csv"));
				if (fc.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
					File f = fc.getSelectedFile();
					
					List<CSVHeader> headers = new ArrayList<CSVHeader>();
					headers.add(new CSVHeader("Lesson ID", "lessonId"));
					headers.add(new CSVHeader("Japanse", "japanese"));
					headers.add(new CSVHeader("Kanji", "kanji"));
					headers.add(new CSVHeader("Mean", "mean"));
					headers.add(new CSVHeader("Audio", "audio"));
					
					CSVHeaderForm frame = new CSVHeaderForm((Frame) getThis(), headers);
					
					frame.pack();
					frame.setVisible(true);
					
					headers = frame.getData();
					String[] header = new String[headers.size()];
					for(int i = 0; i < header.length; i++) {
						header[i] = headers.get(i).getProperty();
						System.out.println(header[i]);
					}
					List<Kotoba> ktbFromCSV = FileDao.readKotobaFromCSV(f.getPath(), ",", header);
					 for (Kotoba ktb : ktbFromCSV) {
					 	insertTableRow(ktb);
					 }
				}
			}
		});
	}
	
	private void setTextFromFormToTable() {
		Integer lessonId = ((Lesson)cmbLesson.getSelectedItem()).getId();
		String japanese = txtWord.getText();
		String kanji = txtKanji.getText();
		String mean = txtMean.getText();
		String audio = txtAudio.getText();
		
		int selectedRowIndex = table.getSelectedRow();
		
		table.setValueAt(lessonId, selectedRowIndex, 0);
		table.setValueAt(japanese, selectedRowIndex, 1);
		table.setValueAt(kanji, selectedRowIndex, 2);
		table.setValueAt(mean, selectedRowIndex, 3);
		table.setValueAt(audio, selectedRowIndex, 4);
		
	}
	private Component getThis(){
		return this;
	}
}
