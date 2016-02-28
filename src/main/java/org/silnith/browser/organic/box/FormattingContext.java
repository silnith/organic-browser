package org.silnith.browser.organic.box;

import java.util.List;


/**
 * A block container box. This either contains block-level boxes or inline-level
 * boxes.
 * <p>
 * Boxes in the normal flow belong to a formatting context, which may be block or inline, but not both simultaneously. Block-level boxes participate in a block formatting context. Inline-level boxes participate in an inline formatting context.
 * 
 * @author kent
 * @param <T> the type of children this formatting context contains, either
 *         {@link BlockLevelBox} or {@link InlineLevelBox}
 * @see <a href="https://www.w3.org/TR/CSS2/visuren.html#normal-flow">9.4 Normal flow</a>
 */
public interface FormattingContext<T> {
    
    void addChild(T child);
    
    List<T> getChildren();
    
}
