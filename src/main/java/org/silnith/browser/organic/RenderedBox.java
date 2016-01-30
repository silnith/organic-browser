package org.silnith.browser.organic;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Dimension2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import org.silnith.browser.organic.box.BorderInformation;
import org.silnith.browser.organic.box.RenderableContent;
import org.silnith.css.model.data.AbsoluteLength;
import org.silnith.css.model.data.AbsoluteUnit;
import org.silnith.css.model.data.BorderStyle;


public class RenderedBox implements RenderableContent {
    
    private final float width;
    
    private final float height;
    
    private final float borderTop;
    
    private final float borderRight;
    
    private final float borderBottom;
    
    private final float borderLeft;
    
    private final BorderInformation borderInformation;
    
    /**
     * @param width the width of the content inside the box
     * @param height the height of the content inside the box
     * @param borderInformation
     */
    public RenderedBox(final float width, final float height, final BorderInformation borderInformation) {
        super();
        this.width = width;
        this.height = height;
        this.borderInformation = borderInformation;
        
        this.borderTop = getPixels(this.borderInformation.getBorderTopWidth());
        this.borderRight = getPixels(this.borderInformation.getBorderRightWidth());
        this.borderBottom = getPixels(this.borderInformation.getBorderBottomWidth());
        this.borderLeft = getPixels(this.borderInformation.getBorderLeftWidth());
    }
    
    public RenderedBox(final Dimension2D size, final BorderInformation borderInformation) {
        this((float) size.getWidth(), (float) size.getHeight(), borderInformation);
    }
    
    private float getPixels(final AbsoluteLength length) {
        return length.convertTo(AbsoluteUnit.PX).getLength().floatValue();
    }
    
    private boolean isRendered(final BorderStyle borderStyle) {
        return borderStyle != BorderStyle.NONE && borderStyle != BorderStyle.HIDDEN;
    }
    
    @Override
    public void paintComponent(final Point2D startPoint, final Graphics2D g2d) {
        final Point2D.Float point;
        if (startPoint instanceof Point2D.Float) {
            point = (Point2D.Float) startPoint;
        } else {
            point = new Point2D.Float((float) startPoint.getX(), (float) startPoint.getY());
        }
        
        if (isRendered(borderInformation.getBorderLeftStyle())) {
            g2d.setColor(borderInformation.getBorderLeftColor());
            g2d.setStroke(new BasicStroke(borderLeft, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
            final Shape leftBorder = new Line2D.Float(point.x + borderLeft / 2f, point.y, point.x + borderLeft / 2f,
                    point.y + borderTop + height + borderBottom);
            g2d.draw(leftBorder);
        }
        if (isRendered(borderInformation.getBorderRightStyle())) {
            g2d.setColor(borderInformation.getBorderRightColor());
            g2d.setStroke(new BasicStroke(borderRight, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
            final Shape rightBorder = new Line2D.Float(point.x + borderLeft + width + borderRight / 2f, point.y,
                    point.x + borderLeft + width + borderRight / 2f, point.y + borderTop + height + borderBottom);
            g2d.draw(rightBorder);
        }
        if (isRendered(borderInformation.getBorderTopStyle())) {
            g2d.setColor(borderInformation.getBorderTopColor());
            g2d.setStroke(new BasicStroke(borderTop, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
            final Shape topBorder = new Line2D.Float(point.x, point.y + borderTop / 2f,
                    point.x + borderLeft + width + borderRight, point.y + borderTop / 2f);
            g2d.draw(topBorder);
        }
        if (isRendered(borderInformation.getBorderBottomStyle())) {
            g2d.setColor(borderInformation.getBorderBottomColor());
            g2d.setStroke(new BasicStroke(borderBottom, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
            final Shape bottomBorder = new Line2D.Float(point.x, point.y + borderTop + height + borderBottom / 2f,
                    point.x + borderLeft + width + borderRight, point.y + borderTop + height + borderBottom / 2f);
            g2d.draw(bottomBorder);
        }
    }
    
    public float getWidth() {
        return borderLeft + width + borderRight;
    }
    
    public float getHeight() {
        return borderTop + height + borderBottom;
    }
    
    @Override
    public Dimension2D getSize() {
        final Dimension size = new Dimension();
        size.setSize(getWidth(), getHeight());
        return size;
    }
    
}
