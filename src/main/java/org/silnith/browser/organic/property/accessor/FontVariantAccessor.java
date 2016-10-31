package org.silnith.browser.organic.property.accessor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.css.model.data.FontVariant;
import org.silnith.css.model.data.KeywordParser;
import org.silnith.css.model.data.PropertyName;
import org.silnith.parser.css3.Token;


public class FontVariantAccessor extends PropertyAccessor<FontVariant> {
    
    private final KeywordParser<FontVariant> parser;
    
    public FontVariantAccessor() {
        super(PropertyName.FONT_VARIANT, true);
        this.parser = new KeywordParser<>(FontVariant.class);
    }

    @Override
    public FontVariant getInitialValue(StyleData styleData) {
        return FontVariant.NORMAL;
    }
    
    @Override
    protected FontVariant parse(StyleData styleData, List<Token> specifiedValue) throws IOException {
        return parser.parse(specifiedValue);
    }
    
    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.emptySet();
    }
    
}
