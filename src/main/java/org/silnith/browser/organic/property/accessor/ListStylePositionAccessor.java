package org.silnith.browser.organic.property.accessor;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.browser.organic.parser.css3.Token;
import org.silnith.css.model.data.ListStylePosition;
import org.silnith.css.model.data.PropertyName;


public class ListStylePositionAccessor extends PropertyAccessor<ListStylePosition> {
    
    public ListStylePositionAccessor() {
        super(PropertyName.LIST_STYLE_POSITION, true);
    }
    
    @Override
    public ListStylePosition getInitialValue(final StyleData styleData) {
        return ListStylePosition.OUTSIDE;
    }
    
    @Override
    protected ListStylePosition parse(final StyleData styleData, final String specifiedValue) {
        return ListStylePosition.getFromValue(specifiedValue);
    }
    
    @Override
    protected ListStylePosition parse(StyleData styleData, List<Token> specifiedValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.emptySet();
    }
    
}
