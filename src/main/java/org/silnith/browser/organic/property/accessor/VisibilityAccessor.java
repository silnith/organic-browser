package org.silnith.browser.organic.property.accessor;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.browser.organic.parser.css3.Token;
import org.silnith.css.model.data.PropertyName;
import org.silnith.css.model.data.Visibility;


public class VisibilityAccessor extends PropertyAccessor<Visibility> {
    
    public VisibilityAccessor() {
        super(PropertyName.VISIBILITY, true);
    }
    
    @Override
    public Visibility getInitialValue(final StyleData styleData) {
        return Visibility.VISIBLE;
    }
    
    @Override
    protected Visibility parse(final StyleData styleData, final String specifiedValue) {
        final Visibility visibility = Visibility.getFromValue(specifiedValue);
        if (visibility == null) {
            throw new IllegalArgumentException("Illegal property value: " + getPropertyName() + ": " + specifiedValue);
        }
        return visibility;
    }
    
    @Override
    protected Visibility parse(StyleData styleData, List<Token> specifiedValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.emptySet();
    }
    
}
