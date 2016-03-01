package org.silnith.browser.organic.property.accessor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.browser.organic.parser.css3.Token;
import org.silnith.css.model.data.AbsoluteLength;
import org.silnith.css.model.data.Length;
import org.silnith.css.model.data.LengthParser;
import org.silnith.css.model.data.PropertyName;


public class TextIndentAccessor extends PropertyAccessor<Length<?>> {
    
    private final LengthParser<?> lengthParser;
    
    public TextIndentAccessor(final LengthParser<?> lengthParser) {
        super(PropertyName.TEXT_INDENT, true);
        this.lengthParser = lengthParser;
    }

    @Override
    public Length<?> getInitialValue(StyleData styleData) {
        return AbsoluteLength.ZERO;
    }
    
    @Override
    protected Length<?> parse(StyleData styleData, List<Token> specifiedValue) throws IOException {
        return lengthParser.parse(specifiedValue);
    }
    
    @Override
    public Set<PropertyName> getDependencies() {
        // also depends on containing block width
        return Collections.singleton(PropertyName.FONT_SIZE);
    }
    
}
