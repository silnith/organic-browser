package org.silnith.browser.organic.box;

/**
 * Either establishes a {@link BlockFormattingContext} or
 * {@link InlineFormattingContext}.
 * <p>
 * In CSS 2.1, many box positions and sizes are calculated with respect to the edges of a rectangular box called a containing block. In general, generated boxes act as containing blocks for descendant boxes; we say that a box "establishes" the containing block for its descendants. The phrase "a box's containing block" means "the containing block in which the box lives," not the one it generates.
 * <p>
 * Each box is given a position with respect to its containing block, but it is not confined by this containing block; it may overflow.
 * 
 * @author kent
 * @see <a href="https://www.w3.org/TR/CSS2/visuren.html#containing-block">9.1.2 Containing blocks</a>
 */
public interface BlockContainerBox {

}
