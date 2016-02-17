package org.silnith.browser.organic;

import java.io.IOException;
import java.io.StringReader;

import org.silnith.browser.organic.parser.css3.grammar.Parser;
import org.silnith.browser.organic.parser.css3.lexical.Tokenizer;
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
public class RawCSSRule implements CSSRule {
    
    private final String elementName;
    
    private final PropertyName propertyName;
    
    private final String styleValue;
    
    public RawCSSRule(final String elementName, final PropertyName propertyName, final String styleValue) {
        this.elementName = elementName;
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
        final String tagName = styledElement.getTagName();
        
        if (tagName.equalsIgnoreCase(elementName)) {
            return true;
        }
        
        return false;
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
        
        styleData.setSpecifiedValue(propertyName, styleValue);
        final Parser cssParser = new Parser(new Tokenizer(new StringReader(styleValue)));
        try {
            cssParser.prime();
            styleData.setParsedSpecifiedValue(propertyName, cssParser.parseListOfComponentValues());
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public String toString() {
        return elementName + " { " + propertyName.getKey() + " : " + styleValue + " }";
    }
    
}
