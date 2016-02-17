package org.silnith.browser.organic;

import org.silnith.css.model.data.PropertyName;


public interface CSSRule {
    
    PropertyName getPropertyName();
    
    boolean shouldApply(final StyledElement styledElement);
    
    void apply(final StyledContent styledElement);
    
}
