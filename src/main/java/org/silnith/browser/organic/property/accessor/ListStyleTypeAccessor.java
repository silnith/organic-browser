package org.silnith.browser.organic.property.accessor;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.browser.organic.parser.css3.Token;
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
    protected ListStyleType parse(StyleData styleData, List<Token> specifiedValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.emptySet();
    }
    
}
