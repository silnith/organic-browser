package org.silnith.browser.organic.box;

import java.util.List;


/**
 * A block container box. This either contains block-level boxes or inline-level
 * boxes.
 * 
 * @author kent
 * @param <T> the type of children this formatting context contains, either
 *         {@link BlockLevelBox} or {@link InlineLevelBox}
 */
public interface FormattingContext<T> {
    
    void addChild(T child);
    
    List<T> getChildren();
    
}
