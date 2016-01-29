package org.silnith.browser.organic.ui;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Browser {

	public Browser() {
	}

	public static void main(final String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		final JFrame frame = new BrowserWindow();
		
		frame.pack();
		frame.setVisible(true);
	}

}
