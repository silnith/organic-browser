package org.silnith.browser.organic.property.accessor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.css.model.data.EmptyCells;
import org.silnith.css.model.data.KeywordParser;
import org.silnith.css.model.data.PropertyName;
import org.silnith.parser.css3.Token;


public class EmptyCellsAccessor extends PropertyAccessor<EmptyCells> {
    
    private final KeywordParser<EmptyCells> parser;
    
    public EmptyCellsAccessor() {
        super(PropertyName.EMPTY_CELLS, true);
        this.parser = new KeywordParser<>(EmptyCells.class);
    }

    @Override
    public EmptyCells getInitialValue(StyleData styleData) {
        return EmptyCells.SHOW;
    }
    
    @Override
    protected EmptyCells parse(StyleData styleData, List<Token> specifiedValue) throws IOException {
        return parser.parse(specifiedValue);
    }
    
    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.emptySet();
    }
    
}
