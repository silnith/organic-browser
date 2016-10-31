package org.silnith.browser.organic.property.accessor;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.css.model.data.PropertyName;
import org.silnith.css.model.data.TextDecoration;
import org.silnith.parser.css3.Token;


public class TextDecorationAccessor extends PropertyAccessor<Collection<TextDecoration>> {
    
    public TextDecorationAccessor() {
        super(PropertyName.TEXT_DECORATION, false);
    }

    @Override
    public Collection<TextDecoration> getInitialValue(StyleData styleData) {
        return Collections.singleton(TextDecoration.NONE);
    }
    
    @Override
    protected Collection<TextDecoration> parse(StyleData styleData, List<Token> specifiedValue) throws IOException {
        final Set<TextDecoration> values = EnumSet.noneOf(TextDecoration.class);
        // TODO: parse specified value as list of values
        return values;
    }
    
    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.emptySet();
    }
    
}
