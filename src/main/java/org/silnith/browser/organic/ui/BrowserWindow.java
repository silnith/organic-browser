package org.silnith.browser.organic.ui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import org.silnith.browser.organic.network.DownloadManager;
import org.silnith.browser.organic.parser.DocumentParser;
import org.silnith.browser.organic.parser.StyleParser;
import org.silnith.browser.organic.ui.action.CloseTabAction;
import org.silnith.browser.organic.ui.action.NewTabAction;


public class BrowserWindow extends JFrame {
    
    private static final long serialVersionUID = 1L;
    
    private final JTabbedPane tabbedPane;
    
    public BrowserWindow() {
        super("Organic Browser");
        BrowserWindow.this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        BrowserWindow.this.setLocationByPlatform(true);
        
        final JMenuBar menuBar = new JMenuBar();
        BrowserWindow.this.setJMenuBar(menuBar);
        
        final JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        
//		final JMenuItem newWindow = new JMenuItem("New Window");
//		fileMenu.add(newWindow);
        
        final Action newTabAction = new NewTabAction(this);
        final JMenuItem newTabMenuItem = new JMenuItem(newTabAction);
        fileMenu.add(newTabMenuItem);
        
//		final JMenuItem open = new JMenuItem("Open");
//		fileMenu.add(open);

//		final JMenuItem save = new JMenuItem("Save");
//		fileMenu.add(save);
        
        final Action closeTabAction = new CloseTabAction(this);
        final JMenuItem closeMenuItem = new JMenuItem(closeTabAction);
        fileMenu.add(closeMenuItem);
        
        fileMenu.add(new JSeparator());
        
        final JMenuItem exit = new JMenuItem("Exit");
        fileMenu.add(exit);
        
        final JMenu lookAndFeelMenu = new JMenu("Look and Feel");
        menuBar.add(lookAndFeelMenu);
        
        final LookAndFeelInfo[] installedLookAndFeels = UIManager.getInstalledLookAndFeels();
        for (final LookAndFeelInfo lookAndFeelInfo : installedLookAndFeels) {
            final ChangeLookAndFeelAction action = new ChangeLookAndFeelAction(lookAndFeelInfo);
            final JMenuItem lookAndFeel = new JMenuItem(action);
            lookAndFeelMenu.add(lookAndFeel);
        }
        
        tabbedPane = new JTabbedPane();
        BrowserWindow.this.getContentPane().add(tabbedPane);
        
        BrowserWindow.this.addTab();
    }
    
    @Override
    public void dispose() {
        System.out.println("dispose");
        super.dispose();
    }
    
    public BrowserPane getCurrentTab() {
        return (BrowserPane) tabbedPane.getComponentAt(tabbedPane.getSelectedIndex());
    }
    
    public void addTab() {
        BrowserPane pane;
        try {
            pane = new BrowserPane(new DownloadManager(), new DocumentParser(), new StyleParser());
            tabbedPane.addTab("Tab", pane);
        } catch (final ClassNotFoundException e) {
            e.printStackTrace();
        } catch (final InstantiationException e) {
            e.printStackTrace();
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
        } catch (final ClassCastException e) {
            e.printStackTrace();
        }
    }
    
    public void closeTab() {
        tabbedPane.remove(tabbedPane.getSelectedIndex());
    }
    
    private class ChangeLookAndFeelAction extends AbstractAction {
        
        private static final long serialVersionUID = 1L;
        
        private final String className;
        
        private ChangeLookAndFeelAction(final LookAndFeelInfo lookAndFeelInfo) {
            super(lookAndFeelInfo.getName());
            this.className = lookAndFeelInfo.getClassName();
        }
        
        @Override
        public void actionPerformed(final ActionEvent e) {
            try {
                UIManager.setLookAndFeel(className);
                SwingUtilities.updateComponentTreeUI(BrowserWindow.this);
            } catch (final UnsupportedLookAndFeelException ex) {
                ex.printStackTrace();
                return;
            } catch (final ClassNotFoundException ex) {
                ex.printStackTrace();
                return;
            } catch (final InstantiationException ex) {
                ex.printStackTrace();
                return;
            } catch (final IllegalAccessException ex) {
                ex.printStackTrace();
                return;
            }
        }
        
    }
    
}
