package org.silnith.browser.organic.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import org.silnith.browser.organic.ui.BrowserWindow;

public class NewTabAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private final BrowserWindow window;

	public NewTabAction(final BrowserWindow browserWindow) {
		super("New Tab");
		this.window = browserWindow;
	}

	public NewTabAction(final BrowserWindow browserWindow, final Icon icon) {
		super("New Tab", icon);
		this.window = browserWindow;
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		window.addTab();
	}

}
