package org.silnith.browser.organic.box;

/**
 * An object that can be rendered, as returned by {@link InlineLevelBox#layoutContents(int, java.awt.Graphics2D, boolean)}.
 * 
 * @author kent
 */
public interface RenderableLineContent extends RenderableContent {

	/**
	 * Returns where the baseline is for this line of content.  This is needed
	 * for vertical alignment of content.
	 * 
	 * @return the baseline for this line content
	 */
	float getBaseline();

}
