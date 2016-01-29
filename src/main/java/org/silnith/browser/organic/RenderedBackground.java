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
	public Dimension2D getSize() {
		return size;
	}

	@Override
	public void paintComponent(final Point2D startPoint, final Graphics2D graphics) {
		final Shape background = new Rectangle2D.Float(
				(float) startPoint.getX(), (float) startPoint.getY(),
				(float) size.getWidth(), (float) size.getHeight());
		
		graphics.setColor(color);
		graphics.fill(background);
	}

}
