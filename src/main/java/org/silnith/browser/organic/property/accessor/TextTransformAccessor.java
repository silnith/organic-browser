package org.silnith.browser.organic.property.accessor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.browser.organic.parser.css3.Token;
import org.silnith.css.model.data.KeywordParser;
import org.silnith.css.model.data.PropertyName;
import org.silnith.css.model.data.TextTransform;


public class TextTransformAccessor extends PropertyAccessor<TextTransform> {
    
    private final KeywordParser<TextTransform> parser;
    
    public TextTransformAccessor() {
        super(PropertyName.TEXT_TRANSFORM, true);
        this.parser = new KeywordParser<>(TextTransform.class);
    }

    @Override
    public TextTransform getInitialValue(StyleData styleData) {
        return TextTransform.NONE;
    }
    
    @Override
    protected TextTransform parse(StyleData styleData, List<Token> specifiedValue) throws IOException {
        return parser.parse(specifiedValue);
    }
    
    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.emptySet();
    }
    
}
