package org.silnith.browser.organic.property.accessor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.css.model.data.CaptionSide;
import org.silnith.css.model.data.KeywordParser;
import org.silnith.css.model.data.PropertyName;
import org.silnith.parser.css3.Token;


public class CaptionSideAccessor extends PropertyAccessor<CaptionSide> {
    
    private final KeywordParser<CaptionSide> parser;
    
    public CaptionSideAccessor() {
        super(PropertyName.CAPTION_SIDE, true);
        this.parser = new KeywordParser<>(CaptionSide.class);
    }

    @Override
    public CaptionSide getInitialValue(StyleData styleData) {
        return CaptionSide.TOP;
    }
    
    @Override
    protected CaptionSide parse(StyleData styleData, List<Token> specifiedValue) throws IOException {
        return parser.parse(specifiedValue);
    }
    
    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.emptySet();
    }
    
}
