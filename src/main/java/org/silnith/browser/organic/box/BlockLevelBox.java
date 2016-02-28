package org.silnith.browser.organic.box;

import java.awt.Graphics2D;

import org.silnith.css.model.data.AbsoluteLength;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


/**
 * This represents a box element that can be layed out inside the CSS box model.
 * <p>
 * Block-level elements are those elements of the source document that are formatted visually as blocks (e.g., paragraphs). The following values of the 'display' property make an element block-level: 'block', 'list-item', and 'table'.
 * <p>
 * Block-level boxes are boxes that participate in a block formatting context. Each block-level element generates a principal block-level box that contains descendant boxes and generated content and is also the box involved in any positioning scheme. Some block-level elements may generate additional boxes in addition to the principal box: 'list-item' elements. These additional boxes are placed with respect to the principal box.
 * <p>
 * Except for table boxes, which are described in a later chapter, and replaced elements, a block-level box is also a block container box. A block container box either contains only block-level boxes or establishes an inline formatting context and thus contains only inline-level boxes. Not all block container boxes are block-level boxes: non-replaced inline blocks and non-replaced table cells are block containers but not block-level boxes. Block-level boxes that are also block containers are called block boxes.
 * <p>
 * The three terms "block-level box," "block container box," and "block box" are sometimes abbreviated as "block" where unambiguous.
 * 
 * @author kent
 * @see <a href="https://www.w3.org/TR/CSS2/visuren.html#block-boxes">9.2.1 Block-level elements and block boxes</a>
 * @see <a href="https://www.w3.org/TR/CSS2/visuren.html#block-level">Block-level elements</a>
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
