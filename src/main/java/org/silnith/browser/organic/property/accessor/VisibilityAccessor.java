package org.silnith.browser.organic.property.accessor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.browser.organic.parser.css3.Token;
import org.silnith.css.model.data.KeywordParser;
import org.silnith.css.model.data.PropertyName;
import org.silnith.css.model.data.Visibility;


public class VisibilityAccessor extends PropertyAccessor<Visibility> {
    
    private final KeywordParser<Visibility> parser;
    
    public VisibilityAccessor() {
        super(PropertyName.VISIBILITY, true);
        this.parser = new KeywordParser<>(Visibility.class);
    }
    
    @Override
    public Visibility getInitialValue(final StyleData styleData) {
        return Visibility.VISIBLE;
    }
    
    @Override
    protected Visibility parse(StyleData styleData, List<Token> specifiedValue) throws IOException {
        return parser.parse(specifiedValue);
    }

    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.emptySet();
    }
    
}
