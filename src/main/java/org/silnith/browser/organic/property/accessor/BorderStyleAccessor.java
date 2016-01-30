package org.silnith.browser.organic.property.accessor;

import java.util.Collections;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.css.model.data.BorderStyle;
import org.silnith.css.model.data.PropertyName;


public abstract class BorderStyleAccessor extends PropertyAccessor<BorderStyle> {
    
    public BorderStyleAccessor(final PropertyName propertyName) {
        super(propertyName, false);
    }
    
    @Override
    public BorderStyle getInitialValue(final StyleData styleData) {
        return BorderStyle.NONE;
    }
    
    @Override
    protected BorderStyle parse(final StyleData styleData, final String specifiedValue) {
        final BorderStyle borderStyle = BorderStyle.getFromValue(specifiedValue);
        if (borderStyle == null) {
            throw new IllegalArgumentException(
                    "Border style illegal value: " + getPropertyName() + ": " + specifiedValue);
        }
        return borderStyle;
    }
    
    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.emptySet();
    }
    
}
