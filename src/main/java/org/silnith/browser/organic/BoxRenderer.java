package org.silnith.browser.organic;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Point;
import java.awt.RenderingHints;
import java.beans.Transient;
import java.util.HashMap;
import java.util.Map;

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
    
    private final BlockLevelBox blockLevelBox;
    
    private transient RenderableContent renderableContent = null;
    
    private transient int preferredWidth = 240;
    
    public BoxRenderer(final BlockLevelBox blockLevelBox) {
        super();
        this.blockLevelBox = blockLevelBox;
        
        this.setOpaque(false);
    }
    
    @Override
    protected synchronized void paintComponent(final Graphics g) {
        super.paintComponent(g);
        
        final Graphics2D g2 = (Graphics2D) g;
        
        final Map<Object, Object> hints = new HashMap<>();
        hints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.addRenderingHints(hints);
        
        renderableContent.paintComponent(new Point(), g2);
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
