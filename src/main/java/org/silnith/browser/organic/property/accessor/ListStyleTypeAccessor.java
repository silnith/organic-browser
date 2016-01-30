package org.silnith.browser.organic.property.accessor;

import java.util.Collections;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.css.model.data.ListStyleType;
import org.silnith.css.model.data.PropertyName;


public class ListStyleTypeAccessor extends PropertyAccessor<ListStyleType> {
    
    public ListStyleTypeAccessor() {
        super(PropertyName.LIST_STYLE_TYPE, true);
    }
    
    @Override
    public ListStyleType getInitialValue(final StyleData styleData) {
        return ListStyleType.DISC;
    }
    
    @Override
    protected ListStyleType parse(final StyleData styleData, final String specifiedValue) {
        return ListStyleType.getFromValue(specifiedValue);
    }
    
    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.emptySet();
    }
    
}
