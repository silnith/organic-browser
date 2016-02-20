package org.silnith.browser.organic;

import java.util.Collection;

import org.silnith.css.model.data.PropertyName;


public interface CSSRule {
    
    PropertyName getPropertyName();
    
    boolean shouldApply(StyledElement styledElement);
    
    void apply(StyledContent styledElement);
    
    void apply(Collection<StyledDOMElement> elements);
    
}
