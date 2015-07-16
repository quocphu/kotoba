package demo;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class KeyShorcut {
	public static void main(final String args[]) {
	    final JFrame frame = new JFrame("Frame Key");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    Action actionListener = new AbstractAction() {
	      public void actionPerformed(ActionEvent actionEvent) {
	        System.out.println("Got an M");
	      }
	    };

	    JPanel content = (JPanel) frame.getContentPane();
	    KeyStroke stroke = KeyStroke.getKeyStroke("M");

	    InputMap inputMap = content.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
	    inputMap.put(stroke, "OPEN");
	    content.getActionMap().put("OPEN", actionListener);

	    frame.setSize(300, 300);
	    frame.setVisible(true);
	  }
}
