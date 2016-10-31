package org.silnith.browser.organic.property.accessor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.browser.organic.parser.css3.Token;
import org.silnith.css.model.data.KeywordParser;
import org.silnith.css.model.data.PropertyName;
import org.silnith.css.model.data.UnicodeBidi;


public class UnicodeBidiAccessor extends PropertyAccessor<UnicodeBidi> {
    
    private final KeywordParser<UnicodeBidi> parser;
    
    public UnicodeBidiAccessor() {
        super(PropertyName.UNICODE_BIDI, false);
        this.parser = new KeywordParser<>(UnicodeBidi.class);
    }

    @Override
    public UnicodeBidi getInitialValue(StyleData styleData) {
        return UnicodeBidi.NORMAL;
    }
    
    @Override
    protected UnicodeBidi parse(StyleData styleData, List<Token> specifiedValue) throws IOException {
        return parser.parse(specifiedValue);
    }
    
    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.emptySet();
    }
    
}
