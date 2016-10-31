package org.silnith.browser.organic.property.accessor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.browser.organic.parser.css3.Token;
import org.silnith.css.model.data.KeywordParser;
import org.silnith.css.model.data.PropertyName;
import org.silnith.css.model.data.WhiteSpace;


public class WhiteSpaceAccessor extends PropertyAccessor<WhiteSpace> {
    
    private final KeywordParser<WhiteSpace> parser;
    
    public WhiteSpaceAccessor() {
        super(PropertyName.WHITE_SPACE, true);
        this.parser = new KeywordParser<>(WhiteSpace.class);
    }

    @Override
    public WhiteSpace getInitialValue(StyleData styleData) {
        return WhiteSpace.NORMAL;
    }
    
    @Override
    protected WhiteSpace parse(StyleData styleData, List<Token> specifiedValue) throws IOException {
        return parser.parse(specifiedValue);
    }
    
    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.emptySet();
    }
    
}
