package app;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import tablemodel.CSVHeaderModel;
import bean.CSVHeader;

public class CSVHeaderForm extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private JPanel pnLeft, pnRight, pnCenter, pnBottom;
	private JButton btnUp, btnDown, btnAdd, btnRemove, btnAddAll, btnRemoveAll, btnOK, btnCancel;
	private JList<CSVHeader> lLeft, lRight;
	private List<CSVHeader> data;
	public CSVHeaderForm(Frame parent, List<CSVHeader> data) {
		super(parent, Dialog.ModalityType.DOCUMENT_MODAL);
		this.data = data;
		// Set layout
		this.setLayout(new BorderLayout());

		// Button
		btnUp = new JButton("^");
		btnDown = new JButton("v");
		btnAdd = new JButton(">");
		btnRemove = new JButton("<");
		btnAddAll = new JButton(">>");
		btnRemoveAll = new JButton("<<");
		btnOK = new JButton("OK");
		btnCancel = new JButton("Cancel");
		
		// Panel
		pnCenter = new JPanel();

		// Center panel
		pnCenter.setLayout(new GridLayout(6, 1));
		pnCenter.add(btnAdd);
		pnCenter.add(btnAddAll);
		pnCenter.add(btnRemove);
		pnCenter.add(btnRemoveAll);
		pnCenter.add(btnUp);
		pnCenter.add(btnDown);
		
		// Left panel
		pnLeft = new JPanel(new BorderLayout());
		lLeft = new JList<CSVHeader>();
		CSVHeaderModel<CSVHeader> leftModel = new CSVHeaderModel<CSVHeader>();
		lLeft.setModel(leftModel);
		JScrollPane scrollLeft = new JScrollPane(lLeft);
		pnLeft.add(scrollLeft);
		lLeft.setModel(new CSVHeaderModel<CSVHeader>(data));
		// Right panel
		pnRight = new JPanel(new BorderLayout());
		lRight = new JList<CSVHeader>();
		CSVHeaderModel<CSVHeader> rightModel = new CSVHeaderModel<CSVHeader>();
		lRight.setModel(rightModel);
		JScrollPane scrollRight = new JScrollPane(lRight);
		pnRight.add(scrollRight);
		
		// Bottom pancel
		pnBottom = new JPanel(new GridLayout(1,0));
		pnBottom.add(btnOK);
		pnBottom.add(btnCancel);
		
		
		add(pnLeft, BorderLayout.WEST);
		add(pnRight, BorderLayout.EAST);
		add(pnCenter, BorderLayout.CENTER);
		add(pnBottom, BorderLayout.SOUTH);
		
		setButtonListeners();
		
		 this.addWindowListener(new WindowAdapter() {
			    @Override
			    public void windowClosed(WindowEvent e) {
			        close();
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

		List<CSVHeader> headers = new ArrayList<CSVHeader>();
		headers.add(new CSVHeader("Lesson ID", "lessonId"));
		headers.add(new CSVHeader("Japanse", "Japanse"));
		headers.add(new CSVHeader("Kanji", "kanji"));
		headers.add(new CSVHeader("Mean", "mean"));
		headers.add(new CSVHeader("Audio", "audio"));
		
		CSVHeaderForm frame = new CSVHeaderForm(null,headers);
//		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//		frame.setLocationRelativeTo(null);

		frame.pack();
		frame.setVisible(true);
		
	}
	
	private void setButtonListeners(){
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Add");
				int selectIndex = lLeft.getSelectedIndex();
				CSVHeaderModel<CSVHeader> lModel =  (CSVHeaderModel<CSVHeader>) lLeft.getModel();
				CSVHeaderModel<CSVHeader> rModel =  (CSVHeaderModel<CSVHeader>) lRight.getModel();
				if (selectIndex >= 0) {
					Object selectItem = lModel.getElementAt(selectIndex);
					rModel.addElement((CSVHeader) selectItem);
					lModel.removeElement(selectIndex);
				}
			}
		});
		
		btnAddAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CSVHeaderModel<CSVHeader> lModel =  (CSVHeaderModel<CSVHeader>) lLeft.getModel();
				CSVHeaderModel<CSVHeader> rModel =  (CSVHeaderModel<CSVHeader>) lRight.getModel();
				
				for(CSVHeader header : lModel.getData()) {
					rModel.addElement(header);
				}
				
				while(lModel.getSize() > 0){
					lModel.removeElement(0);
				}
			}
		});
		
		btnRemoveAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CSVHeaderModel<CSVHeader> lModel =  (CSVHeaderModel<CSVHeader>) lLeft.getModel();
				CSVHeaderModel<CSVHeader> rModel =  (CSVHeaderModel<CSVHeader>) lRight.getModel();
				
				for(CSVHeader header : rModel.getData()) {
					lModel.addElement(header);
				}
				
				while(rModel.getSize() > 0){
					rModel.removeElement(0);
				}
			}
		});
		
		btnRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectIndex = lRight.getSelectedIndex();
				CSVHeaderModel<CSVHeader> lModel =  (CSVHeaderModel<CSVHeader>) lLeft.getModel();
				CSVHeaderModel<CSVHeader> rModel =  (CSVHeaderModel<CSVHeader>) lRight.getModel();
				if (selectIndex >= 0) {
					Object selectItem = rModel.getElementAt(selectIndex);
					lModel.addElement((CSVHeader) selectItem);
					rModel.removeElement(selectIndex);
				}
			}
		});
		
		btnUp.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectIndex = lRight.getSelectedIndex();
				CSVHeaderModel<CSVHeader> rModel =  (CSVHeaderModel<CSVHeader>) lRight.getModel();
				if(selectIndex <= 0) {
					return;
				}
				
				CSVHeader t = rModel.getElementAt(selectIndex);
				rModel.setElement(selectIndex, rModel.getElementAt(selectIndex - 1));
				rModel.setElement(selectIndex - 1, t);
				lRight.setSelectedIndex(selectIndex-1);
			}
		});
		
		btnDown.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectIndex = lRight.getSelectedIndex();
				CSVHeaderModel<CSVHeader> rModel =  (CSVHeaderModel<CSVHeader>) lRight.getModel();
				if(selectIndex < 0 || selectIndex >= rModel.getSize() - 1) {
					return;
				}
				
				CSVHeader t = rModel.getElementAt(selectIndex);
				
				rModel.setElement(selectIndex, rModel.getElementAt(selectIndex + 1));
				rModel.setElement(selectIndex + 1, t);
				
				lRight.setSelectedIndex(selectIndex + 1);
			}
		});
		
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		
		btnOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CSVHeaderModel<CSVHeader> rModel =  (CSVHeaderModel<CSVHeader>) lRight.getModel();
				if (rModel.getSize() <= 0) {
					JOptionPane.showMessageDialog(getContentPane(), "Please select at least one column!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				data = rModel.getData();
				close();
			}
		});
	}
	private void close() {
		this.dispose();
		this.getRootPane().setVisible(false);
		this.data = null;
	}
	public List<CSVHeader> getData(){
		return this.data;
	}
	
}
