package org.silnith.browser.organic.box;

import java.awt.Graphics2D;

import org.silnith.css.model.data.AbsoluteLength;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


/**
 * This represents an inline element that can be layed out inside the CSS box
 * model.
 * <p>
 * Inline-level elements are those elements of the source document that do not form new blocks of content; the content is distributed in lines (e.g., emphasized pieces of text within a paragraph, inline images, etc.). The following values of the 'display' property make an element inline-level: 'inline', 'inline-table', and 'inline-block'. Inline-level elements generate inline-level boxes, which are boxes that participate in an inline formatting context.
 * <p>
 * An inline box is one that is both inline-level and whose contents participate in its containing inline formatting context. A non-replaced element with a 'display' value of 'inline' generates an inline box. Inline-level boxes that are not inline boxes (such as replaced inline-level elements, inline-block elements, and inline-table elements) are called atomic inline-level boxes because they participate in their inline formatting context as a single opaque box.
 * 
 * @author kent
 * @see <a href="https://www.w3.org/TR/CSS2/visuren.html#inline-boxes">9.2.2 Inline-level elements and inline boxes</a>
 */
public interface InlineLevelBox {
    
    public interface LayoutResults {
        
        /**
         * Returns a renderable box that will render the portion of the parent
         * box that fits inside the allocated width. If the entire contents fit,
         * this will contain all the contents of the original box. If the
         * original box had to be split, this will contain the portion of the
         * original box before the split.
         * 
         * @return
         */
        RenderableLineContent getLayedOutContent();
        
        boolean isSplit();
        
        /**
         * If {@link #isSplit()} returns {@code true}, this will return a box
         * containing the remainder of the content that was not included in
         * {@link #getLayedOutContent()}. If {@link #isSplit()} is {@code false}
         * , this returns {@code null}.
         * 
         * @return
         */
        InlineLevelBox getRemainingContent();
        
    }
    
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
     * Attempt to lay itself out inside the allocated target width. If the box
     * will only partially fit inside the allocated width, it will split itself
     * into two boxes and lay out the first part, deferring the second to the
     * next line. If the box cannot fit even the first content inside the target
     * width, it will either fail or overflow depending on {@code allowFailure}.
     * If {@code canOverflow} is {@code false}, this will return {@code null}.
     * If {@code canOverflow} is {@code true}, this will return a layout that
     * exceeds {@code targetWidth}.
     * 
     * @param containingBlockWidth the width of the containing block for this
     *        element
     * @param targetWidth the width allocated to this element
     * @param graphics
     * @param canOverflow
     * @return a {@link LayoutResults} object, or {@code null} if
     *         {@code allowFailure} is {@code true} and the box was unable to
     *         fit anything inside of {@code targetWidth}
     */
    LayoutResults layoutContents(AbsoluteLength containingBlockWidth, AbsoluteLength targetWidth, Graphics2D graphics,
            boolean canOverflow);
            
//	LayoutResults tryLayoutContents(AbsoluteLength parentWidth, AbsoluteLength targetWidth, Graphics2D graphics);

//	LayoutResults forceLayoutContents(AbsoluteLength parentWidth, AbsoluteLength targetWidth, Graphics2D graphics);
    
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
