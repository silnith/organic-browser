package org.silnith.browser.organic.box;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.List;

import org.silnith.browser.organic.RenderedBackground;
import org.silnith.browser.organic.RenderedBox;
import org.silnith.css.model.data.AbsoluteLength;
import org.silnith.css.model.data.AbsoluteUnit;

public class WrappedLineBox extends LineBox {

	private final Point2D offset;

	private final float baseline;

	private final Dimension2D size;

	private final RenderableContent background;

	private final RenderableContent box;

	public WrappedLineBox(final List<RenderableLineContent> renderableLineContents,
			final boolean openAtStart, final boolean openAtEnd,
			final BackgroundInformation backgroundInformation, BorderInformation borderInformation, final ResolvedPaddingInformation resolvedPaddingInformation) {
		super(renderableLineContents);
		
		if (openAtStart) {
			borderInformation = borderInformation.getOpenAtStart();
		}
		if (openAtEnd) {
			borderInformation = borderInformation.getOpenAtEnd();
		}
		
		final float borderTop = getPixels(borderInformation.getBorderTopWidth());
		final float borderRight = getPixels(borderInformation.getBorderRightWidth());
		final float borderBottom = getPixels(borderInformation.getBorderBottomWidth());
		final float borderLeft = getPixels(borderInformation.getBorderLeftWidth());
		
		final float paddingTop = getPixels(resolvedPaddingInformation.getPaddingTop());
		final float paddingRight;
		if (openAtEnd) {
			paddingRight = 0;
		} else {
			paddingRight = getPixels(resolvedPaddingInformation.getPaddingRight());
		}
		final float paddingBottom = getPixels(resolvedPaddingInformation.getPaddingBottom());
		final float paddingLeft;
		if (openAtStart) {
			paddingLeft = 0;
		} else {
			paddingLeft = getPixels(resolvedPaddingInformation.getPaddingLeft());
		}
		
		this.offset = new Point2D.Float(borderLeft + paddingLeft, borderTop + paddingTop);
		
		this.baseline = borderTop + paddingTop + super.getBaseline();
		
		final Dimension2D contentSize = super.getSize();
		this.size = new Dimension();
		this.size.setSize(borderLeft + paddingLeft + contentSize.getWidth() + paddingRight + borderRight,
				borderTop + paddingTop + contentSize.getHeight() + paddingBottom + borderBottom);
		
		this.background = new RenderedBackground(size, backgroundInformation.getColor());
		
		final Dimension2D paddedSize = new Dimension();
		paddedSize.setSize(paddingLeft + contentSize.getWidth() + paddingRight, paddingTop + contentSize.getHeight() + paddingBottom);
		this.box = new RenderedBox(paddedSize, borderInformation);
	}

	private float getPixels(final AbsoluteLength absoluteLength) {
		return absoluteLength.convertTo(AbsoluteUnit.PX).getLength().floatValue();
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
		background.paintComponent(startPoint, graphics);
		box.paintComponent(startPoint, graphics);
		
		final Point2D childStart = new Point2D.Float();
		childStart.setLocation(startPoint.getX() + offset.getX(), startPoint.getY() + offset.getY());
		super.paintComponent(childStart, graphics);
	}

}
