package org.silnith.browser.organic;

import java.util.List;

import org.silnith.browser.organic.parser.css3.Token;
import org.silnith.browser.organic.parser.css3.selector.Selector;
import org.silnith.css.model.data.PropertyName;


/**
 * A single CSS rule.  This is a simple representation.  The matching logic is
 * simply the name of an element to match.  The data applied is a single
 * property name and value to apply if the rule matches an element.
 * 
 * @author kent
 */
/*
 * TODO: replace the matching logic with something more complete
 */
public class ParsedCSSRule implements CSSRule {
    
    private final Selector selector;
    
    private final PropertyName propertyName;
    
    private final List<Token> styleValue;
    
    public ParsedCSSRule(final Selector selector, final PropertyName propertyName, final List<Token> styleValue) {
        this.selector = selector;
        this.propertyName = propertyName;
        this.styleValue = styleValue;
    }
    
    public PropertyName getPropertyName() {
        return propertyName;
    }
    
    /**
     * Checks whether the CSS rule will apply to the given element.
     * If {code true}, then {@link #apply(StyledContent)} should be called.
     * 
     * @param styledElement
     * @return
     */
    public boolean shouldApply(final StyledElement styledElement) {
        if (styledElement instanceof StyledDOMElement) {
            final StyledDOMElement styledDOMElement = (StyledDOMElement) styledElement;
            return selector.matches(styledDOMElement.getElement());
        } else {
            return false;
        }
    }
    
    /**
     * Sets the specified value for the element according to the CSS rule.
     * This should only be called if {@link #shouldApply(StyledElement)} returns
     * {@code true}.
     * <p>
     * Note that this only applies the rule to the provided element by setting
     * the specified value.  It does not resolve the computed value nor does it
     * apply the cascade logic.
     * 
     * @param styledElement
     */
    public void apply(final StyledContent styledElement) {
        final StyleData styleData = styledElement.getStyleData();
        
        styleData.setParsedSpecifiedValue(propertyName, styleValue);
    }
    
    @Override
    public String toString() {
        return selector + " { " + propertyName.getKey() + " : " + styleValue + " }";
    }
    
}
