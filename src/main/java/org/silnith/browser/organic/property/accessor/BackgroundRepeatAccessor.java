package org.silnith.browser.organic.property.accessor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.css.model.data.BackgroundRepeat;
import org.silnith.css.model.data.KeywordParser;
import org.silnith.css.model.data.PropertyName;
import org.silnith.parser.css3.Token;


public class BackgroundRepeatAccessor extends PropertyAccessor<BackgroundRepeat> {
    
    private final KeywordParser<BackgroundRepeat> parser;
    
    public BackgroundRepeatAccessor() {
        super(PropertyName.BACKGROUND_REPEAT, false);
        this.parser = new KeywordParser<>(BackgroundRepeat.class);
    }

    @Override
    public BackgroundRepeat getInitialValue(StyleData styleData) {
        return BackgroundRepeat.REPEAT;
    }
    
    @Override
    protected BackgroundRepeat parse(StyleData styleData, List<Token> specifiedValue) throws IOException {
        return parser.parse(specifiedValue);
    }
    
    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.emptySet();
    }
    
}
