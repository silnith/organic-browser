package org.silnith.browser.organic.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.silnith.browser.organic.ui.BrowserWindow;

public class CloseTabAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private final BrowserWindow window;

	public CloseTabAction(final BrowserWindow browserWindow) {
		super("Close Tab");
		this.window = browserWindow;
	}

//	public CloseTabAction(String name, Icon icon) {
//		super(name, icon);
//	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		window.closeTab();
	}

}
