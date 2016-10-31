package org.silnith.browser.organic.property.accessor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.css.model.data.KeywordParser;
import org.silnith.css.model.data.PropertyName;
import org.silnith.css.model.data.TextAlign;
import org.silnith.parser.css3.Token;


public class TextAlignAccessor extends PropertyAccessor<TextAlign> {
    
    private final KeywordParser<TextAlign> parser;
    
    public TextAlignAccessor() {
        super(PropertyName.TEXT_ALIGN, true);
        this.parser = new KeywordParser<>(TextAlign.class);
    }

    @Override
    public TextAlign getInitialValue(StyleData styleData) {
        return TextAlign.NAMELESS;
    }
    
    @Override
    protected TextAlign parse(StyleData styleData, List<Token> specifiedValue) throws IOException {
        return parser.parse(specifiedValue);
    }
    
    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.emptySet();
    }
    
}
