package org.silnith.browser.organic.box;

import java.util.List;


/**
 * A block container box. This either contains block-level boxes or inline-level
 * boxes.
 * 
 * @author kent
 * @param <T>
 */
public interface FormattingContext<T> {
    
    void addChild(T child);
    
    List<T> getChildren();
    
}
