package org.silnith.browser.organic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.Transient;

import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.silnith.browser.organic.box.BlockLevelBox;
import org.silnith.browser.organic.box.RenderableContent;
import org.silnith.css.model.data.AbsoluteLength;
import org.silnith.css.model.data.AbsoluteUnit;


/**
 * A Swing component that can render CSS box content.
 * 
 * @author kent
 */
public class BoxRenderer extends JPanel implements ChangeListener {
    
    private static final long serialVersionUID = 1L;
    
    private final class Listener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            System.out.println(e.getPoint());
            final boolean contains = renderableContent.containsPoint(new Point(), e.getPoint());
            System.out.println(contains);
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
        
    }
    
    private final BlockLevelBox blockLevelBox;
    
    private transient RenderableContent renderableContent = null;
    
    private transient int preferredWidth = 240;
    
    public BoxRenderer(final BlockLevelBox blockLevelBox) {
        super();
        this.blockLevelBox = blockLevelBox;
        
//        this.setOpaque(true);
//        this.setBackground(Color.WHITE);
        this.setOpaque(false);
        this.addMouseListener(new Listener());
    }
    
    @Override
    protected synchronized void paintComponent(final Graphics g) {
        super.paintComponent(g);
        
        renderableContent.paintComponent(new Point(), (Graphics2D) g);
    }
    
    @Override
    @Transient
    public Dimension getPreferredSize() {
        final Dimension dimension = new Dimension();
        if (renderableContent == null) {
            dimension.setSize(800, 600);
        } else {
            dimension.setSize(renderableContent.getSize());
        }
        return dimension;
    }
    
    @Override
    public synchronized void doLayout() {
        final int screenResolution = getToolkit().getScreenResolution();
        System.out.print("Screen resolution: ");
        System.out.println(screenResolution);
        
        System.out.println("doLayout");
        final Graphics2D graphics = (Graphics2D) getGraphics();
        
        final GraphicsConfiguration deviceConfiguration = graphics.getDeviceConfiguration();
        deviceConfiguration.getNormalizingTransform();
        
        /*
         * TODO: Do we want margins on the root box?
         */
        final AbsoluteLength viewWidth = new AbsoluteLength(preferredWidth, AbsoluteUnit.PX);
        AbsoluteLength offeredWidth = viewWidth;
        final long startNegotiateTime = System.currentTimeMillis();
        int negotiationTries = 1;
        AbsoluteLength negotiatedWidth = blockLevelBox.negotiateWidth(offeredWidth, offeredWidth, graphics);
        while (negotiatedWidth.compareTo(offeredWidth) > 0) {
            negotiationTries++ ;
            offeredWidth = negotiatedWidth;
            negotiatedWidth = blockLevelBox.negotiateWidth(offeredWidth, offeredWidth, graphics);
        }
        final long endNegotiateTime = System.currentTimeMillis();
        System.out.println(
                "negotiation: " + negotiationTries + " tries, time: " + (endNegotiateTime - startNegotiateTime));
                
        final long startTime = System.currentTimeMillis();
        renderableContent = blockLevelBox.layoutContents(negotiatedWidth, negotiatedWidth, graphics);
        final long endTime = System.currentTimeMillis();
        System.out.println("layout time: " + (endTime - startTime));
        super.doLayout();
    }
    
    @Override
    public synchronized void stateChanged(final ChangeEvent e) {
        if (e.getSource() instanceof JViewport) {
            final JViewport viewport = (JViewport) e.getSource();
            final Dimension extentSize = viewport.getExtentSize();
            if (extentSize.width != preferredWidth) {
                preferredWidth = extentSize.width;
                invalidate();
            }
        }
    }
    
}
