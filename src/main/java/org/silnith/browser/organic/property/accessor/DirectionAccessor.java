package org.silnith.browser.organic.property.accessor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.css.model.data.Direction;
import org.silnith.css.model.data.KeywordParser;
import org.silnith.css.model.data.PropertyName;
import org.silnith.parser.css3.Token;


public class DirectionAccessor extends PropertyAccessor<Direction> {
    
    private final KeywordParser<Direction> parser;
    
    public DirectionAccessor() {
        super(PropertyName.DIRECTION, true);
        this.parser = new KeywordParser<>(Direction.class);
    }

    @Override
    public Direction getInitialValue(StyleData styleData) {
        return Direction.LTR;
    }
    
    @Override
    protected Direction parse(StyleData styleData, List<Token> specifiedValue) throws IOException {
        return parser.parse(specifiedValue);
    }
    
    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.emptySet();
    }
    
}
