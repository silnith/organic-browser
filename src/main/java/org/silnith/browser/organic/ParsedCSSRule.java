package org.silnith.browser.organic;

import java.util.Collection;
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
    
    private final boolean inherit;
    
    private final boolean important;
    
    public ParsedCSSRule(final Selector selector, final PropertyName propertyName, final List<Token> styleValue, final boolean important) {
        this.selector = selector;
        this.propertyName = propertyName;
        this.styleValue = styleValue;
        this.inherit = false;
        this.important = important;
    }
    
    public ParsedCSSRule(final Selector selector, final PropertyName propertyName, final boolean important) {
        this.selector = selector;
        this.propertyName = propertyName;
        this.styleValue = null;
        this.inherit = true;
        this.important = important;
    }
    
    public PropertyName getPropertyName() {
        return propertyName;
    }
    
    public boolean isImportant() {
        return important;
    }

    public boolean isInherit() {
        return inherit;
    }

    @Override
    public void apply(Collection<StyledDOMElement> elements) {
        final Collection<StyledDOMElement> subject = selector.select(elements);
        for (final StyledDOMElement element : subject) {
            final StyleData styleData = element.getStyleData();
            
            if (inherit) {
                styleData.setInherit(propertyName);
            } else {
                styleData.setParsedSpecifiedValue(propertyName, styleValue);
            }
        }
    }

    @Override
    public String toString() {
        final String impStr;
        if (important) {
            impStr = " ! important";
        } else {
            impStr = "";
        }
        if (inherit) {
            return selector + " { " + propertyName.getKey() + " : inherit" + impStr + " }";
        } else {
            return selector + " { " + propertyName.getKey() + " : " + styleValue + impStr + " }";
        }
    }
    
}
