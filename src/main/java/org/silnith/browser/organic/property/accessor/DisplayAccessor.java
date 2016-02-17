package org.silnith.browser.organic.property.accessor;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.browser.organic.parser.css3.Token;
import org.silnith.css.model.data.Display;
import org.silnith.css.model.data.PropertyName;


/**
 * A property accessor for computing the value of the "display" property.
 * 
 * @author kent
 * @see PropertyName#DISPLAY
 * @see Display
 */
public class DisplayAccessor extends PropertyAccessor<Display> {
    
    public DisplayAccessor() {
        super(PropertyName.DISPLAY, false);
    }
    
    @Override
    public Display getInitialValue(final StyleData styleData) {
        return Display.INLINE;
    }
    
    @Override
    protected Display parse(final StyleData styleData, final String specifiedValue) {
        final Display display = Display.getFromValue(specifiedValue);
        if (display == null) {
            throw new IllegalArgumentException("Display value invalid: " + specifiedValue);
        }
        return display;
    }
    
    @Override
    protected Display parse(StyleData styleData, List<Token> specifiedValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.emptySet();
    }
    
}
