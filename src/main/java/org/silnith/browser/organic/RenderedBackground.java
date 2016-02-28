package org.silnith.browser.organic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.silnith.browser.organic.box.RenderableContent;


public class RenderedBackground implements RenderableContent {
    
    private final Dimension2D size;
    
    private final Color color;
    
    public RenderedBackground(final Dimension2D size, final Color color) {
        super();
        this.size = size;
        this.color = color;
    }
    
    @Override
    public boolean containsPoint(Point2D startPoint, Point2D clickPoint) {
        final Rectangle2D bounds = new Rectangle2D.Double(startPoint.getX(), startPoint.getY(),
                size.getWidth(), size.getHeight());
        final boolean contains = bounds.contains(clickPoint);
        if (contains) {
            System.out.println(this);
        }
        return contains;
    }

    @Override
    public Dimension2D getSize() {
        return size;
    }
    
    @Override
    public void paintComponent(final Point2D startPoint, final Graphics2D graphics) {
        final Shape background = new Rectangle2D.Float((float) startPoint.getX(), (float) startPoint.getY(),
                (float) size.getWidth(), (float) size.getHeight());
        
        final int alpha = color.getAlpha();
        if (alpha > 0) {
            graphics.setColor(color);
            graphics.fill(background);
        }
    }
    
    @Override
    public String toString() {
        return "RenderedBackground [size=" + size + ", color=" + color + "]";
    }
    
}
