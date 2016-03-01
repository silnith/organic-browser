package org.silnith.browser.organic.property.accessor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.browser.organic.parser.css3.Token;
import org.silnith.css.model.data.KeywordParser;
import org.silnith.css.model.data.Overflow;
import org.silnith.css.model.data.PropertyName;


public class OverflowAccessor extends PropertyAccessor<Overflow> {
    
    private final KeywordParser<Overflow> parser;
    
    public OverflowAccessor() {
        super(PropertyName.OVERFLOW, false);
        this.parser = new KeywordParser<>(Overflow.class);
    }

    @Override
    public Overflow getInitialValue(StyleData styleData) {
        return Overflow.VISIBLE;
    }
    
    @Override
    protected Overflow parse(StyleData styleData, List<Token> specifiedValue) throws IOException {
        return parser.parse(specifiedValue);
    }
    
    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.emptySet();
    }
    
}
