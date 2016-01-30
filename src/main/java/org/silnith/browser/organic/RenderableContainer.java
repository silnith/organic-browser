package org.silnith.browser.organic;

import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.silnith.browser.organic.box.RenderableContent;


public class RenderableContainer implements RenderableContent {
    
    private final List<? extends Point2D> childLocations;
    
    private final List<? extends RenderableContent> childRenderables;
    
    private final Dimension2D size;
    
    public RenderableContainer(final List<? extends Point2D> childLocations,
            final List<? extends RenderableContent> childRenderables, final Dimension2D size) {
        super();
        this.childLocations = new ArrayList<>(childLocations);
        this.childRenderables = new ArrayList<>(childRenderables);
        this.size = size;
    }
    
    @Override
    public Dimension2D getSize() {
        return size;
    }
    
    @Override
    public void paintComponent(final Point2D startPoint, final Graphics2D graphics) {
        final Point2D childStart = new Point2D.Float();
        final Iterator<? extends Point2D> iterator = childLocations.iterator();
        for (final RenderableContent child : childRenderables) {
            final Point2D childOffset = iterator.next();
            childStart.setLocation(startPoint.getX() + childOffset.getX(), startPoint.getY() + childOffset.getY());
            child.paintComponent(childStart, graphics);
        }
    }
    
}
