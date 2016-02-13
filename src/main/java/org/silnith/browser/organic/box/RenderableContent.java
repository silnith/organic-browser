package org.silnith.browser.organic.box;

import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;


/**
 * An object that can be rendered, as returned by
 * {@link BlockLevelBox#layoutContents(org.silnith.css.model.data.AbsoluteLength, org.silnith.css.model.data.AbsoluteLength, Graphics2D)}.
 * This is similar to {@link javax.swing.text.View} except it can represent both
 * block-level and inline-level content as defined in the CSS rendering model.
 * 
 * @author kent
 */
public interface RenderableContent {
    
    Dimension2D getSize();
    
    void paintComponent(Point2D startPoint, Graphics2D graphics);
    
}
