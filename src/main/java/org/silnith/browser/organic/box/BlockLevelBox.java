package org.silnith.browser.organic.box;

import java.awt.Graphics2D;

import org.silnith.css.model.data.AbsoluteLength;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


/**
 * This represents a box element that can be layed out inside the CSS box model.
 * 
 * @author kent
 */
public interface BlockLevelBox {
    
    /**
     * Returns the margin information for a block, resolved against the
     * containing block&rsquo;s width.
     * 
     * @param containingBlockWidth the width of the containing block
     * @return this block&rsquo;s margins
     */
    ResolvedMarginInformation getMarginInformation(final AbsoluteLength containingBlockWidth);
    
    /**
     * Ask the box what it would do if given the provided width in which to lay
     * itself out. This will return the width that this box would make itself if
     * layed out in the provided target width with the provided parent width. If
     * the returned length is longer than {@code targetWidth}, then the parent
     * will probably need to renegotiate with a wider width, possibly exceeding
     * the viewport width.
     * 
     * @param containingBlockWidth the width of the containing block. This is
     *        needed for calculating percentages on margins and padding.
     * @param targetWidth the width allocated for this box
     * @param graphics a graphics object for obtaining font metrics and other
     *        rendering information
     * @return the width that the box would end up being
     */
    AbsoluteLength negotiateWidth(AbsoluteLength containingBlockWidth, AbsoluteLength targetWidth, Graphics2D graphics);
    
    /**
     * Lay out the contents and return an object capable of rendering the
     * contents as layed out.
     * <p>
     * This will attempt to fit within {@code targetWidth}. However, it is not
     * guaranteed that the final rendering will always meet this requirement.
     * Use {@link RenderableContent#getSize()} to get the true dimensions.
     * <p>
     * Neither this object nor the returned object will maintain a reference to
     * the {@link Graphics2D} object. However, if the {@link Graphics2D} object
     * later passed to
     * {@link RenderableContent#paintComponent(java.awt.geom.Point2D, Graphics2D)}
     * is different, the rendering results may be undefined.
     * 
     * @param containingBlockWidth the width of the containing block. This is
     *        needed for calculating percentages on margins and padding.
     * @param targetWidth the width allocated for this box
     * @param graphics a graphics object for obtaining font metrics and other
     *        rendering information
     * @return an object that can render the contents of the box
     */
    RenderableContent layoutContents(AbsoluteLength containingBlockWidth, AbsoluteLength targetWidth,
            Graphics2D graphics);
            
    /**
     * Creates a DOM Node to represent the state of this box. This is primarily
     * used for debugging, so that the state of the layout information can be
     * output in an easily-readable format.
     * 
     * @param document the owning document, needed for creating new nodes
     * @return a Node representing this styled content
     */
    Node createDOM(final Document document);
    
}
