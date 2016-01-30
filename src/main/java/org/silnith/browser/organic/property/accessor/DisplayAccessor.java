package org.silnith.browser.organic.property.accessor;

import java.util.Collections;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.css.model.data.Display;
import org.silnith.css.model.data.PropertyName;


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
    public Set<PropertyName> getDependencies() {
        return Collections.emptySet();
    }
    
}
