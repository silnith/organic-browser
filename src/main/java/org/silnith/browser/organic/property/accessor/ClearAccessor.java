package org.silnith.browser.organic.property.accessor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.css.model.data.Clear;
import org.silnith.css.model.data.KeywordParser;
import org.silnith.css.model.data.PropertyName;
import org.silnith.parser.css3.Token;


public class ClearAccessor extends PropertyAccessor<Clear> {
    
    private final KeywordParser<Clear> parser;
    
    public ClearAccessor() {
        super(PropertyName.CLEAR, false);
        this.parser = new KeywordParser<>(Clear.class);
    }

    @Override
    public Clear getInitialValue(StyleData styleData) {
        return Clear.NONE;
    }
    
    @Override
    protected Clear parse(StyleData styleData, List<Token> specifiedValue) throws IOException {
        return parser.parse(specifiedValue);
    }
    
    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.emptySet();
    }
    
}
