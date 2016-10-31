package org.silnith.browser.organic.property.accessor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.css.model.data.CSSFloat;
import org.silnith.css.model.data.KeywordParser;
import org.silnith.css.model.data.PropertyName;
import org.silnith.parser.css3.Token;


public class FloatAccessor extends PropertyAccessor<CSSFloat> {
    
    private final KeywordParser<CSSFloat> parser;
    
    public FloatAccessor() {
        super(PropertyName.FLOAT, false);
        this.parser = new KeywordParser<>(CSSFloat.class);
    }

    @Override
    public CSSFloat getInitialValue(StyleData styleData) {
        return CSSFloat.NONE;
    }
    
    @Override
    protected CSSFloat parse(StyleData styleData, List<Token> specifiedValue) throws IOException {
        return parser.parse(specifiedValue);
    }
    
    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.emptySet();
    }
    
}
