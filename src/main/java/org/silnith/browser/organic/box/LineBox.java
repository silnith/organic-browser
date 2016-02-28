package org.silnith.browser.organic.box;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * This is a wrapper around a single horizontal line of content inside an
 * {@link InlineFormattingContext}.
 * 
 * @author kent
 * @see <a href="https://www.w3.org/TR/CSS2/visuren.html#inline-formatting">9.4.2 Inline formatting contexts</a>
 * @see <a href="https://www.w3.org/TR/CSS2/visuren.html#line-box">line box</a>
 */
public class LineBox implements RenderableLineContent {
    
    private final List<RenderableLineContent> children;
    
    private final List<Point2D> childLocations;
    
    private final Dimension2D size;
    
    private final float baseline;
    
    public LineBox(final List<RenderableLineContent> renderableLineContents) {
        super();
        this.children = new ArrayList<>(renderableLineContents);
        this.size = new Dimension();
        this.childLocations = new ArrayList<>();
        
        float tempBaseline = 0;
        for (final RenderableLineContent child : this.children) {
            final float childBaseline = child.getBaseline();
            tempBaseline = Math.max(tempBaseline, childBaseline);
        }
        this.baseline = tempBaseline;
        float offset = 0;
        float maxHeight = 0;
        for (final RenderableLineContent child : this.children) {
            final Dimension2D childSize = child.getSize();
            final Point2D childLocation = new Point2D.Float();
            final float childDrop = this.baseline - child.getBaseline();
            childLocation.setLocation(offset, childDrop);
            this.childLocations.add(childLocation);
            offset += childSize.getWidth();
            maxHeight = Math.max(maxHeight, childDrop + (float) childSize.getHeight());
        }
        this.size.setSize(offset, maxHeight);
    }
    
    @Override
    public boolean containsPoint(Point2D startPoint, Point2D clickPoint) {
        final Rectangle2D bounds = new Rectangle2D.Double(startPoint.getX(), startPoint.getY(), size.getWidth(), size.getHeight());
        final boolean contains = bounds.contains(clickPoint);
        if (contains) {
            System.out.println(this);
        }
        
        final Point2D childLocation = new Point2D.Double(startPoint.getX(), startPoint.getY());
        final Iterator<Point2D> iterator = childLocations.iterator();
        for (final RenderableLineContent child : children) {
            final Point2D nextOffset = iterator.next();
            childLocation.setLocation(startPoint.getX() + nextOffset.getX(), startPoint.getY() + nextOffset.getY());
            child.containsPoint(childLocation, clickPoint);
        }
        return contains;
    }

    @Override
    public Dimension2D getSize() {
        return size;
    }
    
    @Override
    public float getBaseline() {
        return baseline;
    }
    
    @Override
    public void paintComponent(final Point2D startPoint, final Graphics2D graphics) {
        final Point2D childLocation = new Point2D.Float();
        final Iterator<Point2D> iterator = childLocations.iterator();
        for (final RenderableLineContent child : children) {
            final Point2D nextOffset = iterator.next();
            childLocation.setLocation(startPoint.getX() + nextOffset.getX(), startPoint.getY() + nextOffset.getY());
            child.paintComponent(childLocation, graphics);
        }
        
//		final RenderedBox box = new RenderedBox(getSize().width, getSize().height, 1, 1, 1, 1, Color.BLACK);
//		box.paintComponent(startPoint, graphics);
    }

    @Override
    public String toString() {
        return "LineBox [children=" + children + ", childLocations=" + childLocations + ", size=" + size + ", baseline="
                + baseline + "]";
    }
    
}
