package org.silnith.browser.organic.property.accessor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.css.model.data.KeywordParser;
import org.silnith.css.model.data.PropertyName;
import org.silnith.css.model.data.TableLayout;
import org.silnith.parser.css3.Token;


public class TableLayoutAccessor extends PropertyAccessor<TableLayout> {
    
    private final KeywordParser<TableLayout> parser;
    
    public TableLayoutAccessor() {
        super(PropertyName.TABLE_LAYOUT, false);
        this.parser = new KeywordParser<>(TableLayout.class);
    }

    @Override
    public TableLayout getInitialValue(StyleData styleData) {
        return TableLayout.AUTO;
    }
    
    @Override
    protected TableLayout parse(StyleData styleData, List<Token> specifiedValue) throws IOException {
        return parser.parse(specifiedValue);
    }
    
    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.emptySet();
    }
    
}
