package org.silnith.browser.organic.box;

import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

/**
 * An object that can be rendered, as returned by
 * {@link BlockLevelBox#layoutContents(org.silnith.css.model.data.AbsoluteLength, org.silnith.css.model.data.AbsoluteLength, Graphics2D)}.
 * 
 * @author kent
 */
public interface RenderableContent {

	Dimension2D getSize();

	void paintComponent(Point2D startPoint, Graphics2D graphics);

}
